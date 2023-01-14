package alexthw.ars_elemental.client;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.client.armor.ElementalArmorModel;
import alexthw.ars_elemental.client.armor.ElementalArmorRenderer;
import alexthw.ars_elemental.client.firenando.FirenandoFamiliarRenderer;
import alexthw.ars_elemental.client.firenando.FirenandoRenderer;
import alexthw.ars_elemental.client.mages.MageRenderer;
import alexthw.ars_elemental.client.mermaid.MermaidRenderer;
import alexthw.ars_elemental.common.entity.spells.EntityLerpedProjectile;
import alexthw.ars_elemental.common.items.CurioHolder;
import alexthw.ars_elemental.common.items.armor.ElementalArmor;
import alexthw.ars_elemental.network.NetworkManager;
import alexthw.ars_elemental.network.OpenCurioBagPacket;
import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModRegistry;
import alexthw.ars_elemental.registry.ModTiles;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.client.renderer.entity.RenderSpell;
import com.hollingsworth.arsnouveau.client.renderer.entity.WealdWalkerRenderer;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import software.bernie.ars_nouveau.geckolib3.renderers.geo.GeoArmorRenderer;

import static alexthw.ars_elemental.ArsElemental.prefix;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    static final ResourceLocation SkeletalHorseTexture = new ResourceLocation("textures/entity/horse/horse_skeleton.png");
    static final ResourceLocation VhexTexture = prefix("textures/entity/vhex.png");

    public static final KeyMapping CURIO_BAG_KEYBINDING = new KeyMapping("key.ars_elemental.open_pouch", GLFW.GLFW_KEY_J, "key.category.ars_nouveau.general");

    @SubscribeEvent
    public static void bindRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModTiles.ELEMENTAL_TURRET.get(), ElementalTurretRenderer::new);
        event.registerBlockEntityRenderer(ModTiles.ADVANCED_PRISM.get(), PrismRenderer::new);
        event.registerEntityRenderer(ModEntities.SIREN_ENTITY.get(), MermaidRenderer::new);
        event.registerEntityRenderer(ModEntities.SIREN_FAMILIAR.get(), MermaidRenderer::new);

        event.registerEntityRenderer(ModEntities.FIRENANDO_ENTITY.get(), FirenandoRenderer::new);
        event.registerEntityRenderer(ModEntities.FIRENANDO_FAMILIAR.get(), FirenandoFamiliarRenderer::new);

        event.registerEntityRenderer(ModEntities.SKELEHORSE_SUMMON.get(), manager -> new UndeadHorseRenderer(manager, ModelLayers.SKELETON_HORSE) {
            @Override
            public @NotNull ResourceLocation getTextureLocation(@NotNull AbstractHorse pEntity) {
                return SkeletalHorseTexture;
            }
        });
        event.registerEntityRenderer(ModEntities.DIREWOLF_SUMMON.get(), DireWolfRenderer::new);
        event.registerEntityRenderer(ModEntities.WSKELETON_SUMMON.get(), WitherSkeletonRenderer::new);
        event.registerEntityRenderer(ModEntities.DOLPHIN_SUMMON.get(), DolphinRenderer::new);
        event.registerEntityRenderer(ModEntities.STRIDER_SUMMON.get(), StriderRenderer::new);
        event.registerEntityRenderer(ModEntities.VHEX_SUMMON.get(), manager -> new VexRenderer(manager) {
            @Override
            public @NotNull ResourceLocation getTextureLocation(@NotNull Vex p_110775_1_) {
                return VhexTexture;
            }
        });

        //noinspection unchecked
        event.registerEntityRenderer(ModEntities.FLASHING_WEALD_WALKER.get(), (manager) -> new WealdWalkerRenderer(manager, "flashing_weald"));

        event.registerEntityRenderer(ModEntities.FIRE_MAGE.get(), MageRenderer::new);
        event.registerEntityRenderer(ModEntities.WATER_MAGE.get(), MageRenderer::new);
        event.registerEntityRenderer(ModEntities.AIR_MAGE.get(), MageRenderer::new);
        event.registerEntityRenderer(ModEntities.EARTH_MAGE.get(), MageRenderer::new);

        event.registerEntityRenderer(ModEntities.HOMING_PROJECTILE.get(), ClientEvents::projectileRender);
        event.registerEntityRenderer(ModEntities.CURVED_PROJECTILE.get(), ClientEvents::projectileRender);
        event.registerEntityRenderer(ModEntities.LINGER_MAGNET.get(), ClientEvents::projectileRender);
        event.registerEntityRenderer(ModEntities.LERP_PROJECTILE.get(), (m) -> new EntityRenderer<>(m) {
            @Override
            public ResourceLocation getTextureLocation(EntityLerpedProjectile pEntity) {
                return new ResourceLocation(ArsNouveau.MODID, "textures/entity/spell_proj.png");
            }
        });

    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.AddLayers event) {
        GeoArmorRenderer.registerArmorRenderer(ElementalArmor.class, () -> new ElementalArmorRenderer<>(new ElementalArmorModel("medium_armor_e").withEmptyAnim()));

    }

    //keybinding
    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(CURIO_BAG_KEYBINDING);
    }

    //Curio bag stuff
    @SubscribeEvent
    public static void bindContainerRenderers(FMLClientSetupEvent event) {
        MenuScreens.register(ModRegistry.CURIO_HOLDER.get(), CurioHolderScreen::new);
        MenuScreens.register(ModRegistry.CASTER_HOLDER.get(), CurioHolderScreen::new);
    }

    private static @NotNull EntityRenderer<EntityProjectileSpell> projectileRender(EntityRendererProvider.Context renderManager) {
        return new RenderSpell(renderManager, new ResourceLocation(ArsNouveau.MODID, "textures/entity/spell_proj.png"));
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

}
