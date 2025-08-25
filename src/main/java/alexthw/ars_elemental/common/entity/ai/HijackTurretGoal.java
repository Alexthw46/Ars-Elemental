package alexthw.ars_elemental.common.entity.ai;

import alexthw.ars_elemental.common.entity.FlashjackEntity;
import com.hollingsworth.arsnouveau.api.ANFakePlayer;
import com.hollingsworth.arsnouveau.common.block.tile.RotatingTurretTile;
import com.hollingsworth.arsnouveau.common.entity.WealdWalker;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
        // Detect nearby adjustable turret tiles
        var turrets = pedestalList(mob.blockPosition(), (int) detectionRange, mob.level());
        if (!turrets.isEmpty()) {
            for (RotatingTurretTile turret : turrets) {
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

    List<RotatingTurretTile> pedestalList(BlockPos blockPos, int offset, @NotNull Level level) {
        ArrayList<RotatingTurretTile> posList = new ArrayList<>();
        for (BlockPos b : BlockPos.betweenClosed(blockPos.offset(offset, -offset, offset), blockPos.offset(-offset, offset, -offset))) {
            if (level.getBlockEntity(b) instanceof RotatingTurretTile tile) {
                posList.add(tile);
            }
        }
        return posList;
    }
}
