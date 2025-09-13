package alexthw.ars_elemental.common.mob_effects;

import alexthw.ars_elemental.registry.ModPotions;
import com.alexthw.sauce.util.PosCarryMEI;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class RepelEffect extends MobEffect {
    public RepelEffect() {
        super(MobEffectCategory.NEUTRAL, new ParticleColor(255, 255, 0).getColor());
    }

    private static void repel(LivingEntity entity, BlockPos origin) {
        Vec3 dist = new Vec3(entity.getX() - origin.getX(), entity.getY() - origin.getY(), entity.getZ() - origin.getZ());
        if (dist.length() < 15) {
            entity.setDeltaMovement(entity.getDeltaMovement().add(dist.normalize()).scale(0.5F));
            entity.hurtMarked = true;
        }
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int pAmplifier) {
        if (!(entity.getEffect(ModPotions.REPEL) instanceof PosCarryMEI mei)) {
            return false;
        }
        for (var blockPos : mei.getAllOrigins()) {
            repel(entity, blockPos);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }
}
