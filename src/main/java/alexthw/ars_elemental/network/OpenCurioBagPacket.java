package alexthw.ars_elemental.network;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.common.items.CurioHolder;
import com.hollingsworth.arsnouveau.api.item.inv.SlotReference;
import com.hollingsworth.arsnouveau.common.network.AbstractPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

public class OpenCurioBagPacket extends AbstractPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenCurioBagPacket> CODEC = StreamCodec.ofMember(OpenCurioBagPacket::toBytes, OpenCurioBagPacket::new);
    public static final CustomPacketPayload.Type<OpenCurioBagPacket> TYPE = new CustomPacketPayload.Type<>(ArsElemental.prefix("open_curio_bag"));

    public OpenCurioBagPacket() {
    }

    public void toBytes(FriendlyByteBuf friendlyByteBuf) {
    }

    public OpenCurioBagPacket(FriendlyByteBuf friendlyByteBuf) {
        this();
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {

        SlotReference bag = CurioHolder.isEquipped(player);

        if (!bag.isEmpty() && bag.getHandler() != null && player != null) {
            ItemStack stack = bag.getHandler().getStackInSlot(bag.getSlot());
            if (stack.getItem() instanceof CurioHolder bagItem)
                bagItem.openContainer(player.level(), player, stack, bag.getHandler() instanceof ICurioStacksHandler ? -1 : bag.getSlot());
        }

    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
