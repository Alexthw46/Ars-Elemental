package alexthw.ars_elemental.common.mob_effects;

import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMobEffect;

import java.util.List;

public class HellFireEffect extends MobEffect implements IForgeMobEffect {

    public HellFireEffect() {
        super(MobEffectCategory.HARMFUL, 14981690);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return List.of(BlockRegistry.FROSTAYA_POD.asItem().getDefaultInstance());
    }

}
