package alexthw.ars_elemental.common.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;

public class SlimyFeetEffect extends MobEffect {
    public SlimyFeetEffect() {
        super(MobEffectCategory.NEUTRAL, MobEffects.POISON.value().getColor());
    }

}
