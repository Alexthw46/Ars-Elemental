package alexthw.ars_elemental.client;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.ModRegistry;
import alexthw.ars_elemental.client.mermaid.MermaidRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void bindRenderers(final EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(ModRegistry.SIREN_ENTITY.get(), MermaidRenderer::new);
        event.registerEntityRenderer(ModRegistry.SIREN_FAMILIAR.get(), MermaidRenderer::new);

    }

}
