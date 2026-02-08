package alexthw.ars_elemental.common.entity.ai;

import alexthw.ars_elemental.common.entity.FlashjackEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class HoverAroundTargetGoal extends Goal {
    private final FlashjackEntity flashjack;
    private final double speedModifier;
    private final float maxDistance;
    private final float minDistance;
    private int pathfindingTimer;

    public HoverAroundTargetGoal(FlashjackEntity flashjack, double speedModifier, float minDistance, float maxDistance) {
        this.flashjack = flashjack;
        this.speedModifier = speedModifier;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.flashjack.getTarget() != null && this.flashjack.getTarget().isAlive() && !this.flashjack.getTurrets().isEmpty();
    }

    @Override
    public void start() {
        this.pathfindingTimer = 0;
    }

    @Override
    public void tick() {
        var target = this.flashjack.getTarget();
        if (target == null) return;

        this.flashjack.getLookControl().setLookAt(target.getX(), target.getY() + target.getEyeHeight(), target.getZ());

        double distance = this.flashjack.distanceTo(target);

        if (distance < this.minDistance) {
            this.moveAwayFromTarget(target);
        } else if (distance > this.maxDistance) {
            this.moveTowardsTarget(target);
        } else {
            this.orbitTarget(target);
        }
    }

    @Override
    public void stop() {
        this.flashjack.getNavigation().stop();
    }

    private void moveTowardsTarget(LivingEntity target) {
        this.flashjack.getNavigation().moveTo(target, this.speedModifier);
    }

    private void moveAwayFromTarget(LivingEntity target) {
        Vec3 direction = this.flashjack.position().subtract(target.position()).normalize();
        Vec3 awayPos = this.flashjack.position().add(direction.scale(5));
        this.flashjack.getNavigation().moveTo(awayPos.x, awayPos.y, awayPos.z, this.speedModifier);
    }

    private void orbitTarget(LivingEntity target) {
        if (this.pathfindingTimer-- <= 0) {
            this.pathfindingTimer = 10; // Ricalcola il percorso ogni 10 tick

            double angle = Math.toRadians(this.flashjack.getRandom().nextDouble() * 360.0);
            double radius = Math.max(this.minDistance, Math.min(this.maxDistance, (this.minDistance + this.maxDistance) / 2.0));

            double offsetX = Math.cos(angle) * radius;
            double offsetZ = Math.sin(angle) * radius;
            double offsetY = this.flashjack.getRandom().nextInt(5) + 1;

            double targetY = target.getY() + target.getEyeHeight() + 1.0 + offsetY;
            Vec3 targetPos = new Vec3(target.getX() + offsetX, targetY, target.getZ() + offsetZ);

            this.flashjack.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, this.speedModifier);
        }
    }

}
