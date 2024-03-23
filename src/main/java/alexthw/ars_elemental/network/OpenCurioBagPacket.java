package alexthw.ars_elemental.network;

import alexthw.ars_elemental.common.items.CurioHolder;
import com.hollingsworth.arsnouveau.api.item.inv.SlotReference;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.function.Supplier;

public class OpenCurioBagPacket {

    public OpenCurioBagPacket() {
    }

    public void encode(FriendlyByteBuf friendlyByteBuf) {
    }

    public static OpenCurioBagPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new OpenCurioBagPacket();
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> contextSupplier) {

        contextSupplier.get().enqueueWork(() -> {
            Player player = contextSupplier.get().getSender();
            SlotReference bag = CurioHolder.isEquipped(player);

            if (!bag.isEmpty() && bag.getHandler() != null && player != null) {
                ItemStack stack = bag.getHandler().getStackInSlot(bag.getSlot());
                if (stack.getItem() instanceof CurioHolder bagItem)
                    bagItem.openContainer(player.level(), player, stack, bag.getHandler() instanceof ICurioStacksHandler ? -1 : bag.getSlot());
            }

        });


    }
}
