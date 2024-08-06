package alexthw.ars_elemental.common.mob_effects;

import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.util.DamageUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class VenomEffect extends MobEffect {
    public VenomEffect() {
        super(MobEffectCategory.HARMFUL, MobEffects.POISON.value().getColor());
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity living, int pAmplifier) {
        living.hurt(DamageUtil.source(living.level(), ModRegistry.POISON), 2 + (0.25F * pAmplifier));
        return true;
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
