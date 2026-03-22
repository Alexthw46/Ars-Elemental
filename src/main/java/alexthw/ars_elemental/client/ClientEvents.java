package alexthw.ars_elemental.client;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.client.firenando.FirenandoFamiliarRenderer;
import alexthw.ars_elemental.client.firenando.FirenandoRenderer;
import alexthw.ars_elemental.client.flashjack.FlashJackFamiliarRenderer;
import alexthw.ars_elemental.client.flashjack.FlashJackRenderer;
import alexthw.ars_elemental.client.mages.MageRenderer;
import alexthw.ars_elemental.client.mermaid.MermaidRenderer;
import alexthw.ars_elemental.client.particle.ShockwaveParticle;
import alexthw.ars_elemental.client.particle.SparkParticle;
import alexthw.ars_elemental.client.particle.VenomParticle;
import alexthw.ars_elemental.client.summons.DireWolfRenderer;
import alexthw.ars_elemental.common.CasterHolderContainer;
import alexthw.ars_elemental.common.CurioHolderContainer;
import alexthw.ars_elemental.common.blocks.relays.AirWarperRelayTile;
import alexthw.ars_elemental.common.blocks.relays.EarthDepositorRelayTile;
import alexthw.ars_elemental.common.blocks.relays.FireCollectorRelayTile;
import alexthw.ars_elemental.common.blocks.relays.WaterSplitterRelayTile;
import alexthw.ars_elemental.common.entity.spells.EntityGeyser;
import alexthw.ars_elemental.common.entity.spells.EntityLavaGeyser;
import alexthw.ars_elemental.common.entity.spells.EntityLerpedProjectile;
import alexthw.ars_elemental.common.entity.spells.EntityMistCloud;
import alexthw.ars_elemental.common.entity.spells.EntityWaterJet;
import alexthw.ars_elemental.common.entity.summon.SummonSlime;
import alexthw.ars_elemental.common.items.CurioHolder;
import alexthw.ars_elemental.network.OpenCurioBagPacket;
import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.registry.ModParticles;
import alexthw.ars_elemental.registry.ModPotions;
import alexthw.ars_elemental.registry.ModRegistry;
import alexthw.ars_elemental.registry.ModTiles;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.item.inv.SlotReference;
import com.hollingsworth.arsnouveau.client.particle.WrappedProvider;
import com.hollingsworth.arsnouveau.client.renderer.entity.RenderSpell;
import com.hollingsworth.arsnouveau.client.renderer.entity.RenderSummonSkeleton;
import com.hollingsworth.arsnouveau.client.renderer.entity.StyledSpellRender;
import com.hollingsworth.arsnouveau.client.renderer.entity.WealdWalkerModel;
import com.hollingsworth.arsnouveau.client.renderer.tile.GenericTileRenderer;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import static alexthw.ars_elemental.ArsElemental.prefix;

@EventBusSubscriber(modid = ArsElemental.MODID, value = Dist.CLIENT)
public class ClientEvents {

    static final ResourceLocation SkeletalHorseTexture = ResourceLocation.withDefaultNamespace("textures/entity/horse/horse_skeleton.png");
    static final ResourceLocation VhexTexture = prefix("textures/entity/vhex.png");

    public static final KeyMapping CURIO_BAG_KEYBINDING = new KeyMapping("key.ars_elemental.open_pouch", GLFW.GLFW_KEY_J, "key.category.ars_nouveau.general");

    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {

    }

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        if (event.getCamera().getEntity() instanceof LivingEntity living && living.hasEffect(ModPotions.MIST)) {

            // 'Near' is where fog starts (0 = right at your eyes)
            // 'Far' is where fog becomes 100% opaque (5 blocks away = you can't see past 5 blocks)
            event.setNearPlaneDistance(0.20f);
            event.setFarPlaneDistance(8.0f);

            // Sphere shape makes it feel like proper volumetric fog,
            // Cylinder is the old vanilla render style
            event.setFogShape(FogShape.SPHERE);

            // Essential: Cancel the event to prevent Vanilla/Other mods from overwriting your values
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        var player = event.getCamera().getEntity();
        if (player instanceof LivingEntity living && living.hasEffect(ModPotions.MIST)) {
            event.setRed(0.85f);
            event.setGreen(0.9f);
            event.setBlue(0.95f);
        }
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.SPARK.get(), SparkParticle::factory);
        event.registerSpriteSet(ModParticles.VENOM.get(), VenomParticle::factory);
        event.registerSpriteSet(ModParticles.SPARK_2.get(), (sprites -> new WrappedProvider(ModParticles.SPARK.get(), SparkParticle::factory)));
        event.registerSpriteSet(ModParticles.VENOM_2.get(), (sprites -> new WrappedProvider(ModParticles.VENOM.get(), VenomParticle::factory)));
        event.registerSpriteSet(ModParticles.SHOCKWAVE.get(), ShockwaveParticle.Provider::new);
        event.registerSpriteSet(ModParticles.SHOCKWAVE_SMALL.get(), ShockwaveParticle.ProviderSmall::new);

    }

    @SubscribeEvent
    public static void bindRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        //ModelProperty.resources.add(new ModelProperty.Model(ArsElemental.prefix("carian"), DocAssets.STYLE_ICON_SPHERE, true));

        event.registerBlockEntityRenderer(ModTiles.ADVANCED_COLLECTOR_RELAY.get(), (t) -> new GenericTileRenderer<>(t, "source_collector") {
            static final ResourceLocation FIRE = prefix("textures/block/fire_relay.png");

            @Override
            public ResourceLocation getTextureLocation(FireCollectorRelayTile animatable) {
                return FIRE;
            }
        });
        event.registerBlockEntityRenderer(ModTiles.ADVANCED_SPLITTER_RELAY.get(), (t) -> new GenericTileRenderer<>(t, "source_splitter") {
            static final ResourceLocation WATER = prefix("textures/block/water_relay.png");

            @Override
            public ResourceLocation getTextureLocation(WaterSplitterRelayTile animatable) {
                return WATER;
            }
        });
        event.registerBlockEntityRenderer(ModTiles.ADVANCED_WARP_RELAY.get(), (t) -> new GenericTileRenderer<>(t, "source_warp") {
            static final ResourceLocation AIR = prefix("textures/block/air_relay.png");

            @Override
            public ResourceLocation getTextureLocation(AirWarperRelayTile animatable) {
                return AIR;
            }
        });
        event.registerBlockEntityRenderer(ModTiles.ADVANCED_DEPOSITOR_RELAY.get(), (t) -> new GenericTileRenderer<>(t, "source_deposit") {
            static final ResourceLocation EARTH = prefix("textures/block/earth_relay.png");

            @Override
            public ResourceLocation getTextureLocation(EarthDepositorRelayTile animatable) {
                return EARTH;
            }
        });

        event.registerBlockEntityRenderer(ModTiles.ADVANCED_PRISM.get(), PrismRenderer::new);
        event.registerEntityRenderer(ModEntities.SIREN_ENTITY.get(), MermaidRenderer::new);
        event.registerEntityRenderer(ModEntities.SIREN_FAMILIAR.get(), MermaidRenderer::new);

        event.registerEntityRenderer(ModEntities.FIRENANDO_ENTITY.get(), FirenandoRenderer::new);
        event.registerEntityRenderer(ModEntities.FIRENANDO_FAMILIAR.get(), FirenandoFamiliarRenderer::new);

        event.registerEntityRenderer(ModEntities.FLASHJACK_ENTITY.get(), FlashJackRenderer::new);
        event.registerEntityRenderer(ModEntities.FLASHJACK_FAMILIAR.get(), FlashJackFamiliarRenderer::new);

        event.registerEntityRenderer(ModEntities.SKELEHORSE_SUMMON.get(), manager -> new UndeadHorseRenderer(manager, ModelLayers.SKELETON_HORSE) {
            @Override
            public @NotNull ResourceLocation getTextureLocation(@NotNull AbstractHorse pEntity) {
                return SkeletalHorseTexture;
            }
        });

        event.registerEntityRenderer(ModEntities.CAMEL_SUMMON.get(), manager -> new CamelRenderer(manager, ModelLayers.CAMEL));

        event.registerEntityRenderer(ModEntities.DIREWOLF_SUMMON.get(), DireWolfRenderer::new);
        event.registerEntityRenderer(ModEntities.WSKELETON_SUMMON.get(), renderManagerIn -> new RenderSummonSkeleton(renderManagerIn) {
            @Override
            public @NotNull ResourceLocation getTextureLocation(@NotNull AbstractSkeleton entity) {
                return ResourceLocation.withDefaultNamespace("textures/entity/skeleton/wither_skeleton.png");
            }
        });
        event.registerEntityRenderer(ModEntities.SLIME_SUMMON.get(), p_174391_ -> new SlimeRenderer(p_174391_) {
            static final ResourceLocation defLoc = prefix("textures/entity/slime/slime.png");
            static final ResourceLocation waterLoc = prefix("textures/entity/slime/slime_water.png");
            static final ResourceLocation fireLoc = prefix("textures/entity/slime/slime_fire.png");
            static final ResourceLocation earthLoc = prefix("textures/entity/slime/slime_earth.png");
            static final ResourceLocation airLoc = prefix("textures/entity/slime/slime_air.png");
            static final ResourceLocation manipLoc = prefix("textures/entity/slime/slime_manipulation.png");

            @Override
            public @NotNull ResourceLocation getTextureLocation(@NotNull Slime entity) {
                if (!(entity instanceof SummonSlime summon))
                    return super.getTextureLocation(entity);
                var variant = summon.getVariant();
                return switch (variant) {
                    case "summon" -> defLoc;
                    case "earth" -> earthLoc;
                    case "fire" -> fireLoc;
                    case "water" -> waterLoc;
                    case "air" -> airLoc;
                    case "manipulation" -> manipLoc;

                    default -> super.getTextureLocation(entity);
                };
            }

            @Override
            protected int getBlockLightLevel(@NotNull Slime entity, @NotNull BlockPos pos) {
                return 15;
            }

        });

        event.registerEntityRenderer(ModEntities.BEE_SUMMON.get(), p_173931_ -> new BeeRenderer(p_173931_) {
            private static final ResourceLocation ANGRY_BEE_TEXTURE = prefix("textures/entity/bee_angry.png");
            private static final ResourceLocation BEE_TEXTURE = prefix("textures/entity/bee.png");

            public @NotNull ResourceLocation getTextureLocation(@NotNull Bee entity) {
                return entity.isAngry() ? ANGRY_BEE_TEXTURE : BEE_TEXTURE;
            }

        });
        event.registerEntityRenderer(ModEntities.DOLPHIN_SUMMON.get(), DolphinRenderer::new);
        event.registerEntityRenderer(ModEntities.STRIDER_SUMMON.get(), StriderRenderer::new);
        event.registerEntityRenderer(ModEntities.VHEX_SUMMON.get(), manager -> new VexRenderer(manager) {
            @Override
            public @NotNull ResourceLocation getTextureLocation(@NotNull Vex p_110775_1_) {
                return VhexTexture;
            }
        });

        event.registerEntityRenderer(ModEntities.FLASHING_WEALD_WALKER.get(), v -> new GeoEntityRenderer<>(v, new WealdWalkerModel<>("flashing_weald")));

        event.registerEntityRenderer(ModEntities.FIRE_MAGE.get(), MageRenderer::new);
        event.registerEntityRenderer(ModEntities.WATER_MAGE.get(), MageRenderer::new);
        event.registerEntityRenderer(ModEntities.AIR_MAGE.get(), MageRenderer::new);
        event.registerEntityRenderer(ModEntities.EARTH_MAGE.get(), MageRenderer::new);

        event.registerEntityRenderer(ModEntities.LINGER_MAGNET.get(), ClientEvents::projectileRender);
        event.registerEntityRenderer(ModEntities.FLASH_LIGHTNING.get(), LightningBoltRenderer::new);
        event.registerEntityRenderer(ModEntities.DRIPSTONE_SPIKE.get(), GeoSpikeRenderer::new);
        event.registerEntityRenderer(ModEntities.ICE_SPIKE.get(), renderManager -> new GeoSpikeRenderer(renderManager, prefix("textures/entity/ice_spike.png")));
        event.registerEntityRenderer(ModEntities.THROWN_SPIKE.get(), FallingSpikeRenderer::new);
        event.registerEntityRenderer(ModEntities.THROWN_ICE_SPIKE.get(), renderManager -> new FallingSpikeRenderer(renderManager, prefix("textures/entity/ice_spike.png")));

        event.registerEntityRenderer(ModEntities.LERP_PROJECTILE.get(), (m) -> new EntityRenderer<>(m) {
            @Override
            public @NotNull ResourceLocation getTextureLocation(@NotNull EntityLerpedProjectile pEntity) {
                return ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "textures/entity/spell_proj.png");
            }
        });
        event.registerEntityRenderer(ModEntities.WATER_JET_MARKER.get(), (m) -> new EntityRenderer<>(m) {
            @Override
            public @NotNull ResourceLocation getTextureLocation(@NotNull EntityWaterJet pEntity) {
                return ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "textures/entity/spell_proj.png");
            }
        });
        event.registerEntityRenderer(ModEntities.GEYSER.get(), (m) -> new EntityRenderer<>(m) {
            @Override
            public @NotNull ResourceLocation getTextureLocation(@NotNull EntityGeyser pEntity) {
                return ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "textures/entity/spell_proj.png");
            }
        });
        event.registerEntityRenderer(ModEntities.FIRE_GEYSER.get(), (m) -> new EntityRenderer<>(m) {
            @Override
            public @NotNull ResourceLocation getTextureLocation(@NotNull EntityLavaGeyser pEntity) {
                return ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "textures/entity/spell_proj.png");
            }
        });
        event.registerEntityRenderer(ModEntities.MIST_CLOUD.get(), (m) -> new EntityRenderer<>(m) {
            @Override
            public @NotNull ResourceLocation getTextureLocation(@NotNull EntityMistCloud pEntity) {
                return ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "textures/entity/spell_proj.png");
            }
        });

        event.registerEntityRenderer(ModEntities.PHALANX_PROJ.get(), StyledSpellRender::new);

    }

    @SubscribeEvent
    public static void initItemColors(final RegisterColorHandlersEvent.Item event) {
        event.register((stack, color) -> color > 0 ? -1 :
                        stack.getOrDefault(DataComponents.BASE_COLOR, DyeColor.RED).getTextureDiffuseColor() + 0xFF000000,
                ModItems.CASTER_BAG.get());
    }

    //keybinding
    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(CURIO_BAG_KEYBINDING);
    }

    //Curio bag stuff
    @SubscribeEvent
    public static void bindContainerRenderers(RegisterMenuScreensEvent event) {
        event.register(ModRegistry.CURIO_HOLDER.get(), (CurioHolderContainer screenContainer, Inventory inv, Component titleIn) -> new CurioHolderScreen<>(screenContainer, inv, titleIn, prefix("textures/gui/curio_bag.png"), 175, 163));
        event.register(ModRegistry.CASTER_HOLDER.get(), (CasterHolderContainer screenContainer, Inventory inv, Component titleIn) -> new CurioHolderScreen<>(screenContainer, inv, titleIn, prefix("textures/gui/curio_bag_2.png"), 175, 217));
    }

    private static @NotNull EntityRenderer<EntityProjectileSpell> projectileRender(EntityRendererProvider.Context renderManager) {
        return new RenderSpell(renderManager, ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "textures/entity/spell_proj.png"));
    }

    public void openBackpackGui(ClientTickEvent.Post event) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            Minecraft minecraft = Minecraft.getInstance();
            Player playerEntity = minecraft.player;
            if (!(minecraft.screen instanceof CurioHolderScreen) && (playerEntity != null)) {
                if (CURIO_BAG_KEYBINDING.isDown()) {
                    SlotReference backpack = CurioHolder.isEquipped(playerEntity);

                    if (!backpack.isEmpty()) {
                        Networking.sendToServer(new OpenCurioBagPacket());
                    }
                }
            }
        }
    }

}
