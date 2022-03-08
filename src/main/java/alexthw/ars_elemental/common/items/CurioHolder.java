package alexthw.ars_elemental.common.items;

import alexthw.ars_elemental.common.CurioHolderContainer;
import alexthw.ars_elemental.util.ItemInventory;
import com.hollingsworth.arsnouveau.api.item.ArsNouveauCurio;
import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.api.util.CuriosUtil;
import com.hollingsworth.arsnouveau.common.items.PotionFlask;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.NetworkHooks;
import top.theillusivec4.curios.api.CuriosCapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static alexthw.ars_elemental.ModRegistry.CURIO_BAGGABLE;

public class CurioHolder extends Item {
    public CurioHolder(Properties pProperties) {
        super(pProperties);
    }

    public static ItemStack isEquipped(Player playerEntity) {
        if (playerEntity != null){
            Optional<IItemHandlerModifiable> curios = CuriosUtil.getAllWornItems(playerEntity).resolve();
            if (curios.isPresent()) {
                IItemHandlerModifiable items = curios.get();
                for(int i = 0; i < items.getSlots(); ++i) {
                    ItemStack item = items.getStackInSlot(i);
                    if (item.getItem() instanceof CurioHolder) {
                        return item;
                    }
                }
            }
            Inventory inv = playerEntity.getInventory();
            for(int i = 0; i < 9; ++i) {
                ItemStack stack = inv.items.get((inv.selected + i) % 9);
                if (stack.getItem() instanceof CurioHolder) {
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean canStore(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof CurioHolder) return false;
        return item instanceof ISpellModifierItem || stack.is(CURIO_BAGGABLE) || item instanceof ArsNouveauCurio || item instanceof PotionFlask || stack.getCapability(CuriosCapability.ITEM).isPresent();
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new InventoryCapability(stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
        if (!level.isClientSide) {
            ItemStack stack = playerIn.getItemInHand(handIn);
            openContainer(level,playerIn,stack);
        }
        return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
    }

    public void openContainer(Level level, Player player, ItemStack bag) {
        if (!level.isClientSide)
        {
            MenuProvider container = new SimpleMenuProvider((w, p, pl) -> new CurioHolderContainer(w, p, bag), bag.getHoverName());
            NetworkHooks.openGui((ServerPlayer) player, container, b -> b.writeItem(bag));
            player.level.playSound(null, player.blockPosition(), SoundEvents.BUNDLE_INSERT, SoundSource.PLAYERS, 1, 1);
        }
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

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }

}
