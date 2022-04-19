package alexthw.ars_elemental.client;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.client.firenando.FirenandoRenderer;
import alexthw.ars_elemental.client.mages.MageRenderer;
import alexthw.ars_elemental.client.mermaid.MermaidRenderer;
import alexthw.ars_elemental.common.items.CurioHolder;
import alexthw.ars_elemental.network.NetworkManager;
import alexthw.ars_elemental.network.OpenCurioBagPacket;
import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.client.renderer.entity.RenderSpell;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.client.renderer.entity.VexRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import static alexthw.ars_elemental.ArsElemental.prefix;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    static final ResourceLocation SkeletalHorseTexture = new ResourceLocation("textures/entity/horse/horse_skeleton.png");
    static final ResourceLocation VhexTexture = prefix("textures/entity/vhex.png");

    public static final KeyMapping CURIO_BAG_KEYBINDING = new KeyMapping("key.ars_elemental.open_pouch", GLFW.GLFW_KEY_J, "key.category.ars_nouveau.general");


    @SubscribeEvent
    public static void bindRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SIREN_ENTITY.get(), MermaidRenderer::new);
        event.registerEntityRenderer(ModEntities.SIREN_FAMILIAR.get(), MermaidRenderer::new);

        event.registerEntityRenderer(ModEntities.FIRENANDO_ENTITY.get(), FirenandoRenderer::new);
        event.registerEntityRenderer(ModEntities.FIRENANDO_FAMILIAR.get(), FirenandoRenderer::new);

        event.registerEntityRenderer(ModEntities.SKELEHORSE_SUMMON.get(), manager -> new UndeadHorseRenderer(manager, ModelLayers.SKELETON_HORSE) {
            @Override
            public @NotNull ResourceLocation getTextureLocation(@NotNull AbstractHorse pEntity) {
                return SkeletalHorseTexture;
            }
        });
        event.registerEntityRenderer(ModEntities.DIREWOLF_SUMMON.get(), DireWolfRenderer::new);
        event.registerEntityRenderer(ModEntities.VHEX_SUMMON.get(), manager -> new VexRenderer(manager) {
            @Override
            public @NotNull ResourceLocation getTextureLocation(@NotNull Vex p_110775_1_) {
                return VhexTexture;
            }
        });

        event.registerEntityRenderer(ModEntities.FIRE_MAGE.get(), MageRenderer::new);
        event.registerEntityRenderer(ModEntities.WATER_MAGE.get(), MageRenderer::new);
        event.registerEntityRenderer(ModEntities.AIR_MAGE.get(), MageRenderer::new);
        event.registerEntityRenderer(ModEntities.EARTH_MAGE.get(), MageRenderer::new);

        event.registerEntityRenderer(ModEntities.HOMING_PROJECTILE.get(), renderManager -> new RenderSpell(renderManager, new ResourceLocation(ArsNouveau.MODID, "textures/entity/spell_proj.png")));
        event.registerEntityRenderer(ModEntities.CURVED_PROJECTILE.get(), renderManager -> new RenderSpell(renderManager, new ResourceLocation(ArsNouveau.MODID, "textures/entity/spell_proj.png")));
        event.registerEntityRenderer(ModEntities.LINGER_MAGNET.get(), renderManager -> new RenderSpell(renderManager, new ResourceLocation(ArsNouveau.MODID, "textures/entity/spell_proj.png")));

    }

    //keybinding
    @SubscribeEvent
    public static void registerKeybinding(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(CURIO_BAG_KEYBINDING);
    }

    //Curio bag stuff
    @SubscribeEvent
    public static void bindContainerRenderers(FMLClientSetupEvent event) {
        MenuScreens.register(ModRegistry.CURIO_HOLDER.get(), CurioHolderScreen::new);
    }

    @SubscribeEvent
    public void openBackpackGui(TickEvent.ClientTickEvent event)
    {
        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            Minecraft minecraft = Minecraft.getInstance();
            Player playerEntity = minecraft.player;
            if (!(minecraft.screen instanceof CurioHolderScreen) && (playerEntity != null))
            {
                if (CURIO_BAG_KEYBINDING.isDown())
                {
                    ItemStack backpack = CurioHolder.isEquipped(playerEntity);

                    if (backpack.getItem() instanceof CurioHolder)
                    {
                        NetworkManager.INSTANCE.send(PacketDistributor.SERVER.noArg(), new OpenCurioBagPacket());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void stitch(TextureStitchEvent.Pre event)
    {
        event.addSprite(ArsElemental.FOCUS_SLOT);
    }

}
