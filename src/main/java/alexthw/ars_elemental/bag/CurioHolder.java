package alexthw.ars_elemental.bag;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CurioHolder extends Item {
    public CurioHolder(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new InventoryCapability(stack);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity playerIn, Hand handIn) {
        if (!level.isClientSide)
        {
            ItemStack stack = playerIn.getItemInHand(handIn);
            INamedContainerProvider container =
                    new SimpleNamedContainerProvider((w, p, pl) -> new CurioHolderContainer(w, p, stack), stack.getHoverName());
            NetworkHooks.openGui((ServerPlayerEntity) playerIn, container, b -> b.writeItem(stack));
            playerIn.level.playSound(null, playerIn.blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1, 1);
        }
        return ActionResult.success(playerIn.getItemInHand(handIn));
    }

    private static class InventoryCapability implements ICapabilityProvider {
        private final LazyOptional<IItemHandler> opt;

        public InventoryCapability(ItemStack stack) {
            opt = LazyOptional.of(() -> new InvWrapper(getInventory(stack)));
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, opt);
        }
    }

    public static ItemInventory getInventory(ItemStack stack) {
        return new ItemInventory(stack, 27);
    }
}
