package alexthw.ars_elemental.common.mob_effects;

import alexthw.ars_elemental.registry.ModParticles;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.util.DamageUtil;
import com.hollingsworth.arsnouveau.common.potions.ParticleMobEffect;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class VenomEffect extends ParticleMobEffect {
    public VenomEffect() {
        super(MobEffectCategory.HARMFUL, MobEffects.POISON.value().getColor());
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity living, int pAmplifier) {
        if (living.level().isClientSide)
            //3 loops
            for (int i = 0; i < 3; i++) {
                living.level().addParticle(getParticle(), living.getRandomX(.75D), 0.35 + living.getRandomY(), living.getRandomZ(.75D), 0.0D, 0.0D, 0.0D);
            }
        else
            living.hurt(DamageUtil.source(living.level(), ModRegistry.POISON), 2 + (0.25F * pAmplifier));
        return true;

    }

    @Override
    public ParticleOptions getParticle() {
        return ModParticles.VENOM.get();
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        int j = switch (pAmplifier) {
            case 0 -> 20;
            case 1 -> 10;
            default -> 32 >> pAmplifier;
        };
        return j == 0 || pDuration % j == 0;
    }

}
