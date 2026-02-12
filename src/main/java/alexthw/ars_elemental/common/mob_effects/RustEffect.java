package alexthw.ars_elemental.common.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;

public class RustEffect extends MobEffect {
    public RustEffect() {
        super(MobEffectCategory.HARMFUL, MobEffects.DAMAGE_RESISTANCE.value().getColor());
    }
}
