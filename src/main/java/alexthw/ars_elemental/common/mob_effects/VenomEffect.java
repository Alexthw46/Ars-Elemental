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
        super(MobEffectCategory.HARMFUL, MobEffects.POISON.getColor());
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {

        pLivingEntity.hurt(DamageUtil.source(pLivingEntity.level(), ModRegistry.POISON), 2);

    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int j = 32 >> pAmplifier;
        return j == 0 || pDuration % j == 0;
    }
}
