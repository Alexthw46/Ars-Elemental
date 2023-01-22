package alexthw.ars_elemental.common.blocks.prism;

import alexthw.ars_elemental.api.item.SpellPrismLens;
import alexthw.ars_elemental.client.TooltipUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractPrismLens extends Item implements SpellPrismLens {

    final String key;

    public AbstractPrismLens(Properties properties, String key) {
        super(properties);
        this.key = key;
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, tooltip, pIsAdvanced);
        tooltip.add(Component.translatable("tooltip.ars_elemental.lens"));
        TooltipUtils.addOnShift(tooltip, () -> tooltip.add(Component.translatable(getDescriptionKey())), "lens");

    }

    protected String getDescriptionKey() {
        return "ars_elemental.lens." + key;
    }
}
