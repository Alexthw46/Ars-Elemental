package alexthw.ars_elemental.common.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class ElementalCurio extends Item implements ICurioItem {
    public ElementalCurio(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    /*
     * Do not equip if in offhand
     */
    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return stack != slotContext.entity().getItemInHand(InteractionHand.OFF_HAND);
    }

}
