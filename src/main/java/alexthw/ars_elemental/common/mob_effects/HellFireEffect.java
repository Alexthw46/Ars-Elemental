package alexthw.ars_elemental.common.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.common.extensions.IForgeMobEffect;

public class HellFireEffect extends MobEffect implements IForgeMobEffect {

    public HellFireEffect() {
        super(MobEffectCategory.HARMFUL, 14981690);
    }

}
