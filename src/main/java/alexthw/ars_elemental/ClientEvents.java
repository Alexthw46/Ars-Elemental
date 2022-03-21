package alexthw.ars_elemental;

import alexthw.ars_elemental.bag.CurioHolderScreen;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.client.renderer.entity.RenderSpell;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.client.renderer.entity.VexRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static alexthw.ars_elemental.datagen.Datagen.prefix;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    static ResourceLocation SkeleHorseTexture = new ResourceLocation("textures/entity/horse/horse_skeleton.png");
    static ResourceLocation DireWolfTexture = prefix("textures/entity/direwolf.png");
    static ResourceLocation VhexTexture = prefix("textures/entity/vhex.png");

    @SubscribeEvent
    public static void attachEntityRenderers(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ModRegistry.SKELEHORSE_SUMMON.get(), manager -> new UndeadHorseRenderer(manager) {
            @Override
            public ResourceLocation getTextureLocation(AbstractHorseEntity pEntity) {
                return SkeleHorseTexture;
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(ModRegistry.DIREWOLF_SUMMON.get(), manager -> new WolfRenderer(manager){
            @Override
            public ResourceLocation getTextureLocation(WolfEntity entity) {
                return DireWolfTexture;
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(ModRegistry.VHEX_SUMMON.get(), manager -> new VexRenderer(manager)
        {
            @Override
            public ResourceLocation getTextureLocation(VexEntity p_110775_1_) {
                return VhexTexture;
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(ModRegistry.ARC_PROJECTILE.get(), manager -> new RenderSpell(manager, new ResourceLocation(ArsNouveau.MODID, "textures/entity/spell_proj.png")));
        RenderingRegistry.registerEntityRenderingHandler(ModRegistry.HOMING_PROJECTILE.get(), manager -> new RenderSpell(manager, new ResourceLocation(ArsNouveau.MODID, "textures/entity/spell_proj.png")));

    }

    @SubscribeEvent
    public static void bindContainerRenderers(FMLClientSetupEvent event) {
        ScreenManager.register(ModRegistry.CURIO_POUCH.get(), CurioHolderScreen::new);
    }
}
