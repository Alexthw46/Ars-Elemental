package alexthw.ars_elemental.network;

import alexthw.ars_elemental.ArsElemental;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static alexthw.ars_elemental.ArsElemental.prefix;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetworkManager {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(prefix("main"), () -> NetworkManager.PROTOCOL_VERSION, NetworkManager.PROTOCOL_VERSION::equals, NetworkManager.PROTOCOL_VERSION::equals);

    @SuppressWarnings("UnusedAssignment")
    @SubscribeEvent
    public static void registerNetworkStuff(FMLCommonSetupEvent event) {
        int index = 0;
        INSTANCE.registerMessage(index++, OpenCurioBagPacket.class, OpenCurioBagPacket::encode, OpenCurioBagPacket::decode, OpenCurioBagPacket::whenThisPacketIsReceived);
        INSTANCE.registerMessage(index++, RayEffectPacket.class, RayEffectPacket::encode, RayEffectPacket::decode, RayEffectPacket::whenThisPacketIsReceived);

    }


}
