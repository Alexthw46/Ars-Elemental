package alexthw.ars_elemental.bag;

import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.api.item.ArsNouveauCurio;
import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.common.items.DominionWand;
import net.minecraft.client.audio.SoundSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import top.theillusivec4.curios.api.CuriosCapability;

import javax.annotation.Nonnull;

public class CurioHolderContainer extends Container {
    private final IInventory inventory;

    public CurioHolderContainer(int windowId, PlayerInventory playerInv, ItemStack backpack) {
        this(ModRegistry.CURIO_POUCH.get(), windowId, playerInv, CurioHolder.getInventory(backpack));
    }

    public CurioHolderContainer(ContainerType<? extends CurioHolderContainer> containerType, int windowId, PlayerInventory playerInv, IInventory inventory) {
        super(containerType, windowId);
        this.inventory = inventory;
        inventory.startOpen(playerInv.player);
        for (int i = 0; i < inventory.getContainerSize() / 9f; ++i) {
            for (int j = 0; j < 9; ++j) {
                int index = i * 9 + j;
                addSlot(new Slot(inventory, index, 8 + j * 18, 18 + i * 18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return stack.getItem() instanceof ISpellModifierItem || stack.getItem() instanceof ArsNouveauCurio || stack.getItem() instanceof DominionWand || stack.getCapability(CuriosCapability.ITEM).isPresent() ;
                    }
                });
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

    @Override
    public void removed(PlayerEntity playerIn) {
        playerIn.level.playSound(null, playerIn.blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1, 1);
        super.removed(playerIn);
        this.inventory.stopOpen(playerIn);
    }

    public int offset() {
        return 0;
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return this.inventory.stillValid(playerIn);
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
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
