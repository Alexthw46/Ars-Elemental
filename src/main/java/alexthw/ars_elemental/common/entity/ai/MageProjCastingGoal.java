package alexthw.ars_elemental.common.entity.ai;

import alexthw.ars_elemental.common.entity.mages.EntityMageBase;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketAnimEntity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Supplier;

public class MageProjCastingGoal<T extends EntityMageBase> extends ProjCastingGoal<T> {

    // Stuck detection
    private double lastX;
    private double lastY;
    private double lastZ;
    private int stuckTicks = 0;

    public MageProjCastingGoal(T entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
        super(entity, speed, attackRange, canUse, animId, delayTicks);
        this.lastX = entity.getX();
        this.lastY = entity.getY();
        this.lastZ = entity.getZ();
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity == null) return;

        double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
        boolean canSeeEnemy = this.mob.getSensing().hasLineOfSight(livingentity);

        // Engagement band: keep the mage closer to the target
        // attackRadiusSqr == attackRange^2
        // prefer to stay between ~0.6*range and ~1.1*range
        double minDistanceSqr = this.attackRadiusSqr * 0.36; // (0.6 * range)^2
        double maxDistanceSqr = this.attackRadiusSqr * 1.21; // (1.1 * range)^2
        // extreme-close threshold: if inside this, try to path away to avoid getting too cramped
        double extremeCloseSqr = this.attackRadiusSqr * 0.0625; // (0.25 * range)^2
        // Update visibility timer
        if (canSeeEnemy != (this.seeTime > 0)) {
            this.seeTime = 0;
        }
        if (canSeeEnemy) {
            ++this.seeTime;
        } else {
            --this.seeTime;
        }

        // Adjust movement based on distance
        if (d0 > maxDistanceSqr) {
            // If too far, move to a point at a desired engagement distance from the target
            this.strafingBackwards = false;
            double dx = this.mob.getX() - livingentity.getX();
            double dz = this.mob.getZ() - livingentity.getZ();
            double len = Math.sqrt(dx * dx + dz * dz);
            if (len < 0.001) {
                // fallback: direct move
                this.mob.getNavigation().moveTo(livingentity, this.speedModifier);
            } else {
                double nx = dx / len;
                double nz = dz / len;
                double desiredDistance = Math.sqrt(this.attackRadiusSqr) * 0.9; // aim slightly inside attack range
                double destX = livingentity.getX() + nx * desiredDistance;
                double destZ = livingentity.getZ() + nz * desiredDistance;
                double destY = livingentity.getY();
                this.mob.getNavigation().moveTo(destX, destY, destZ, this.speedModifier);
            }
            this.strafingTime = -1;
        } else if (d0 < extremeCloseSqr) {
            // If extremely close, use pathfinding to find some space (avoid being inside the target)
            attemptPathAwayFrom(livingentity);
            this.strafingTime = -1;
        } else if (d0 < minDistanceSqr) {
            // If slightly too close, don't pathfind far away — just strafe/back up a little to keep close engagement
            this.strafingBackwards = true;
            this.mob.getNavigation().stop();
            ++this.strafingTime;
        } else {
            // Within ideal engagement band, stop nav and start strafing
            this.mob.getNavigation().stop();
            ++this.strafingTime;
        }

        // Randomize strafing direction occasionally
        if (this.strafingTime >= 20) {
            if (this.mob.getRandom().nextFloat() < 0.25D) {
                this.strafingClockwise = !this.strafingClockwise;
            }
            if (this.mob.getRandom().nextFloat() < 0.1D) {
                // Rarely switch to backpedaling; prefer staying at engagement distance
                this.strafingBackwards = !this.strafingBackwards;
            }
            this.strafingTime = 0;
        }

        if (this.strafingTime > -1) {
            // distanceRatio in [0,1] where 1 is at attackRange, 0 is at point-blank
            float distanceRatio = (float) (Math.sqrt(d0) / Math.sqrt(this.attackRadiusSqr));
            // strafeSpeed: ensure a decent minimum, and optionally increase when close
            float strafeSpeed = Math.min(0.9F, 0.15F + (1.0F - distanceRatio) * 0.9F);
            this.mob.getMoveControl().strafe(
                    this.strafingBackwards ? -strafeSpeed : strafeSpeed,
                    this.strafingClockwise ? strafeSpeed : -strafeSpeed
            );

        }
        this.mob.lookAt(livingentity, 30.0F, 30.0F);

        // Stuck detection: if trying to strafe but not moving, fallback to pathfinder reposition
        detectAndRecoverFromStuck(livingentity);

        animationChecks(livingentity);

        // update last position for next tick
        this.lastX = this.mob.getX();
        this.lastY = this.mob.getY();
        this.lastZ = this.mob.getZ();
    }

    // Try to path to a point away from the target (uses navigation to handle slopes/obstacles)
    private void attemptPathAwayFrom(LivingEntity target) {
        double dx = this.mob.getX() - target.getX();
        double dz = this.mob.getZ() - target.getZ();
        double len = Math.sqrt(dx * dx + dz * dz);
        if (len < 0.001) {
            // fallback random direction
            double angle = this.mob.getRandom().nextDouble() * Math.PI * 2.0;
            dx = Math.cos(angle);
            dz = Math.sin(angle);
            len = 1.0;
        }
        double nx = dx / len;
        double nz = dz / len;
        double desiredDistance = Math.sqrt(this.attackRadiusSqr) * 0.9; // aim slightly inside attack range
        double destX = target.getX() + nx * desiredDistance;
        double destZ = target.getZ() + nz * desiredDistance;
        double destY = target.getY();

        // Try to navigate to the computed point
        this.mob.getNavigation().moveTo(destX, destY, destZ, this.speedModifier);
    }

    private void detectAndRecoverFromStuck(LivingEntity target) {
        if (this.strafingTime <= -1) {
            this.stuckTicks = 0;
            return;
        }

        double movedSqr = this.mob.distanceToSqr(this.lastX, this.lastY, this.lastZ);
        // If we're attempting to strafe but barely moved in the last tick(s), increment stuck counter
        if (movedSqr < 0.001) {
            this.stuckTicks++;
        } else {
            this.stuckTicks = 0;
        }

        if (this.stuckTicks > 8) {
            // Try a pathfinding-based reposition behind the mob to escape geometry
            attemptPathAwayFrom(target);
            this.strafingTime = -1;
            this.stuckTicks = 0;
        }
    }

    // Faster animation/attack reaction and more predictive aiming for mages
    @Override
    void animationChecks(LivingEntity target) {
        // react faster than base class: half the seeTime threshold
        if (this.seeTime >= 20 && !this.hasAnimated) {
            this.hasAnimated = true;
            // send animation packet
            Networking.sendToNearbyClient(mob.level, mob, new PacketAnimEntity(mob.getId(), animId));
        }

        if (this.hasAnimated) {
            // Stop navigation while finalizing the cast so the mob doesn't pathfind away and shoot while looking elsewhere
            this.mob.getNavigation().stop();
            // continuously lock look at the target while the animation is playing to make sure the mob stays aimed
            this.mob.lookAt(target, 30.0F, 30.0F);
            animatedTicks++;
            if (animatedTicks >= Math.max(1, delayTicks / 2)) {
                // compute a small predictive aim based on target motion
                double px = target.getX() + target.getDeltaMovement().x * 0.5;
                double py = target.getY() + target.getDeltaMovement().y * 0.5 + target.getEyeHeight() * 0.5;
                double pz = target.getZ() + target.getDeltaMovement().z * 0.5;

                double dx = px - this.mob.getX();
                double dy = py - (this.mob.getY() + this.mob.getEyeHeight());
                double dz = pz - this.mob.getZ();
                double horiz = Math.sqrt(dx * dx + dz * dz);
                float yaw = (float) (Math.atan2(dz, dx) * (180.0D / Math.PI)) - 90.0F;
                float pitch = (float) (-(Math.atan2(dy, horiz) * (180.0D / Math.PI)));

                // enforce immediate rotation so projectile spawns in the intended direction
                this.mob.setYRot(yaw);
                this.mob.setXRot(pitch);
                this.mob.yHeadRot = yaw;
                this.mob.yBodyRot = yaw;

                // perform the attack
                mob.performRangedAttack(target, 1);
                this.done = true;
            }
        }
    }

}
