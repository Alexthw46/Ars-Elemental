package alexthw.ars_elemental.common.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;

public class SlipFeetEffect extends MobEffect {
    public SlipFeetEffect() {
        super(MobEffectCategory.NEUTRAL, MobEffects.SLOW_FALLING.value().getColor());
    }

}
