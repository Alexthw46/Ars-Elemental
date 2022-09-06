package alexthw.ars_elemental.common;

import alexthw.ars_elemental.common.items.CurioHolder;
import alexthw.ars_elemental.registry.ModRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class CurioHolderContainer extends AbstractContainerMenu {
    private final Container inventory;

    public CurioHolderContainer(int windowId, Inventory playerInv, ItemStack backpack) {
        this(ModRegistry.CURIO_HOLDER.get(), windowId, playerInv, CurioHolder.getInventory(backpack));
    }

    public CurioHolderContainer(MenuType<? extends CurioHolderContainer> containerType, int windowId, Inventory playerInv, Container inventory) {
        super(containerType, windowId);
        this.inventory = inventory;
        inventory.startOpen(playerInv.player);
        for (int i = 0; i < inventory.getContainerSize() / 9f; ++i) {
            for (int j = 0; j < 9; ++j) {
                int index = i * 9 + j;
                addSlot(this.makeSlot(inventory, i, j, index));
            }
        }
        int offset = offset();
        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInv, j1 + (l + 1) * 9, 8 + j1 * 18, offset + 84 + l * 18));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInv, i1, 8 + i1 * 18, offset + 142));
        }
    }

    @NotNull
    protected Slot makeSlot(Container inventory, int i, int j, int index) {
        return new Slot(inventory, index, 8 + j * 18, 18 + i * 18) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return CurioHolder.canStore(stack);
            }

        };
    }

    @Override
    public void removed(Player playerIn) {
        playerIn.level.playSound(null, playerIn.blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1, 1);
        super.removed(playerIn);
        this.inventory.stopOpen(playerIn);
    }

    public int offset() {
        return 0;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.inventory.stillValid(playerIn);
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack copy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack = slot.getItem();
            copy = itemstack.copy();
            if (index < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return copy;
    }

}
