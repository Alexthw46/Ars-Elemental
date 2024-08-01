package alexthw.ars_elemental.common;

import alexthw.ars_elemental.common.items.CasterHolder;
import alexthw.ars_elemental.registry.ModRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerCopySlot;
import org.jetbrains.annotations.NotNull;

public class CasterHolderContainer extends CurioHolderContainer {

    public CasterHolderContainer(int windowId, Inventory playerInv, ItemStack backpack) {
        super(ModRegistry.CASTER_HOLDER.get(), windowId, playerInv, backpack.getCapability(Capabilities.ItemHandler.ITEM), backpack);
    }

    @Override
    protected @NotNull Slot makeSlot(IItemHandler inventory, int i, int j, int index) {
        return new ItemHandlerCopySlot(inventory, index, 8 + j * 18, 18 + i * 18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return CasterHolder.canStore(stack);
            }
        };
    }
}
