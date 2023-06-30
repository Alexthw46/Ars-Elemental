package alexthw.ars_elemental.network;

import alexthw.ars_elemental.common.items.CurioHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenCurioBagPacket {

    public OpenCurioBagPacket(){
    }

    public void encode(FriendlyByteBuf friendlyByteBuf) {
    }

    public static OpenCurioBagPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new OpenCurioBagPacket();
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> contextSupplier) {

        contextSupplier.get().enqueueWork(()->{
            Player player = contextSupplier.get().getSender();
            ItemStack bag = CurioHolder.isEquipped(player);

            if (bag.getItem() instanceof CurioHolder bagItem && player != null)
            {
                bagItem.openContainer(player.level(), player, bag);
            }


        });


    }
}
