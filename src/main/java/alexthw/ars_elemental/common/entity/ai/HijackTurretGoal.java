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
        if (mob.getTarget() == null) return;
        mob.getTarget().addEffect(new MobEffectInstance(MobEffects.GLOWING, 40));
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
                // Hijack the turret to target the mob's target
                turret.aim(mob.getTarget().blockPosition(), ANFakePlayer.getPlayer((ServerLevel) mob.level()));
                turret.shootSpell();
            }
            cooldown += 20; // Cooldown to prevent constant retargeting
        }
        var walkers = mob.level().getEntitiesOfClass(WealdWalker.class, mob.getBoundingBox().inflate(detectionRange), LivingEntity::isAlive);
        for (WealdWalker walker : walkers) {
            walker.setTarget(mob.getTarget());
        }
    }

}
