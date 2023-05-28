package alexthw.ars_elemental.common.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FrozenEffect extends MobEffect {
    public FrozenEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return List.of();
    }
}
