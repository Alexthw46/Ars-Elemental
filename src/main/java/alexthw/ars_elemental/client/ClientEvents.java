package alexthw.ars_elemental.client;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.ModRegistry;
import alexthw.ars_elemental.client.mermaid.MermaidRenderer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.client.renderer.entity.VexRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Vex;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static alexthw.ars_elemental.Datagen.prefix;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    static ResourceLocation SkeleHorseTexture = new ResourceLocation("textures/entity/horse/horse_skeleton.png");
    static ResourceLocation DireWolfTexture = prefix("textures/entity/direwolf.png");
    static ResourceLocation VhexTexture = prefix("textures/entity/vhex.png");


    @SubscribeEvent
    public static void bindRenderers(final EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(ModRegistry.SIREN_ENTITY.get(), MermaidRenderer::new);
        event.registerEntityRenderer(ModRegistry.SIREN_FAMILIAR.get(), MermaidRenderer::new);

        event.registerEntityRenderer(ModRegistry.SKELEHORSE_SUMMON.get(), manager -> new UndeadHorseRenderer(manager, ModelLayers.SKELETON_HORSE) {
            @Override
            public ResourceLocation getTextureLocation(AbstractHorse pEntity) {
                return SkeleHorseTexture;
            }
        });
        event.registerEntityRenderer(ModRegistry.DIREWOLF_SUMMON.get(), manager -> new WolfRenderer(manager){
            @Override
            public ResourceLocation getTextureLocation(Wolf entity) {
                return DireWolfTexture;
            }
        });
        event.registerEntityRenderer(ModRegistry.VHEX_SUMMON.get(), manager -> new VexRenderer(manager)
        {
            @Override
            public ResourceLocation getTextureLocation(Vex p_110775_1_) {
                return VhexTexture;
            }
        });
    }

}
