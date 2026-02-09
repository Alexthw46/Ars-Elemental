package alexthw.ars_elemental.common.entity.ai;

import alexthw.ars_elemental.common.entity.FlashjackEntity;
import alexthw.ars_elemental.registry.ModParticles;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.ANFakePlayer;
import com.hollingsworth.arsnouveau.api.util.DamageUtil;
import com.hollingsworth.arsnouveau.common.block.tile.RotatingTurretTile;
import com.hollingsworth.arsnouveau.common.entity.WealdWalker;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketAnimEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;

public class HijackTurretGoal extends Goal {
    private final FlashjackEntity mob;
    private final float detectionRange;
    int cooldown = 0;
    int attackCooldown = 0;

    public HijackTurretGoal(FlashjackEntity mob, float detectionRange) {
        this.mob = mob;
        this.detectionRange = detectionRange;
    }

    @Override
    public boolean canUse() {
        return mob != null && mob.isTamed() && mob.getTarget() != null && mob.getTarget().isAlive();
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = mob.getTarget();
        if (target == null) return;
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 40));

        // If the mob is close enough to the target and the attack cooldown is ready, perform a shock attack
        if (mob.distanceTo(target) <= 7 && attackCooldown <= 0) {
            Networking.sendToNearbyClient(mob.level, mob, new PacketAnimEntity(mob.getId(), 0));
            target.hurt(DamageUtil.source(mob.level, ModRegistry.SPARK, mob), 4);
            // Spawn some electric particles around the target
            for (int i = 0; i < 5; i++) {
                double offsetX = (mob.getRandom().nextDouble() - 0.5) * target.getBbWidth();
                double offsetY = mob.getRandom().nextDouble() * target.getBbHeight();
                double offsetZ = (mob.getRandom().nextDouble() - 0.5) * target.getBbWidth();
                if (mob.level() instanceof ServerLevel sl)
                    sl.sendParticles(ModParticles.SPARK.get(), target.getX() + offsetX, target.getY() + offsetY, target.getZ() + offsetZ, 1, 0, 0, 0, 0);
            }
            attackCooldown = 40; // Cooldown for the shock attack
            return;
        } else if (attackCooldown > 0) {
            attackCooldown--;
        }

        if (cooldown > 0) {
            cooldown--;
            return;
        }

        // Detect nearby adjustable turret tiles
        var turrets = mob.getTurrets();
        if (!turrets.isEmpty()) {
            for (BlockPos turretPos : turrets) {
                if (!(mob.level().getBlockEntity(turretPos) instanceof RotatingTurretTile turret)) continue;
                // Check if the turret has vision of the mob's target, reverse ray trace from the target to the turret
                ClipContext context = new ClipContext(target.getEyePosition(), turret.getBlockPos().getCenter(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, target);
                if (mob.level().clip(context) instanceof BlockHitResult hitResult && !hitResult.getBlockPos().equals(turret.getBlockPos()))
                    continue; // No line of sight, skip this turret
                // Hijack the turret to target the mob's target
                turret.aim(BlockPos.containing(target.getEyePosition()), ANFakePlayer.getPlayer((ServerLevel) mob.level()));
                // Force the turret to rotate immediately
                turret.rotationX = turret.neededRotationX;
                turret.rotationY = turret.neededRotationY;
                turret.shootSpell();
            }
        }
        // Only if the walker is close enough to the flashjack
        var walkers = mob.level().getEntitiesOfClass(WealdWalker.class, mob.getBoundingBox().inflate(detectionRange), LivingEntity::isAlive);
        for (WealdWalker walker : walkers) {
            walker.setTarget(target);
        }
        cooldown = 20; // Cooldown to prevent constant retargeting
    }

}
