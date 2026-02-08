package alexthw.ars_elemental.common.entity.ai;

import alexthw.ars_elemental.common.entity.FlashjackEntity;
import com.hollingsworth.arsnouveau.api.ANFakePlayer;
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
        if (cooldown > 0) {
            cooldown--;
            return;
        }
        Networking.sendToNearbyClient(mob.level, mob, new PacketAnimEntity(mob.getId(), 0));
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
            cooldown += 20; // Cooldown to prevent constant retargeting
        }
        // Only if the walker is close enough to the flashjack
        var walkers = mob.level().getEntitiesOfClass(WealdWalker.class, mob.getBoundingBox().inflate(detectionRange), LivingEntity::isAlive);
        for (WealdWalker walker : walkers) {
            walker.setTarget(target);
        }
    }

}
