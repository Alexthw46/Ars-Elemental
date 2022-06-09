package alexthw.ars_elemental.common.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class ElementalCurio extends Item implements ICurioItem {
    public ElementalCurio(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

}
