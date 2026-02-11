package alexthw.ars_elemental.registry;

import alexthw.ars_elemental.common.blocks.EverfullUrnTile;
import alexthw.ars_elemental.common.blocks.mermaid_block.MermaidTile;
import alexthw.ars_elemental.common.blocks.prism.AdvancedPrismTile;
import alexthw.ars_elemental.common.blocks.relays.AirWarperRelayTile;
import alexthw.ars_elemental.common.blocks.relays.EarthDepositorRelayTile;
import alexthw.ars_elemental.common.blocks.relays.FireCollectorRelayTile;
import alexthw.ars_elemental.common.blocks.relays.WaterSplitterRelayTile;
import alexthw.ars_elemental.common.blocks.upstream.AirUpstreamTile;
import alexthw.ars_elemental.common.blocks.upstream.MagmaUpstreamTile;
import alexthw.ars_elemental.common.blocks.upstream.WaterUpstreamTile;
import com.alexthw.sauce.registry.ModRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static alexthw.ars_elemental.ArsElemental.MODID;

@SuppressWarnings("DataFlowIssue")
public class ModTiles {

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MermaidTile>> MERMAID_TILE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WaterUpstreamTile>> WATER_UPSTREAM_TILE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MagmaUpstreamTile>> LAVA_UPSTREAM_TILE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AirUpstreamTile>> AIR_UPSTREAM_TILE;

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WaterSplitterRelayTile>> ADVANCED_SPLITTER_RELAY;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AirWarperRelayTile>> ADVANCED_WARP_RELAY;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EarthDepositorRelayTile>> ADVANCED_DEPOSITOR_RELAY;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FireCollectorRelayTile>> ADVANCED_COLLECTOR_RELAY;

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AdvancedPrismTile>> ADVANCED_PRISM;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EverfullUrnTile>> URN_TILE;
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID);

    static {
        MERMAID_TILE = TILES.register("mermaid_tile", () -> BlockEntityType.Builder.of(MermaidTile::new, ModItems.MERMAID_ROCK.get()).build(null));
        WATER_UPSTREAM_TILE = TILES.register("upstream_tile", () -> BlockEntityType.Builder.of(WaterUpstreamTile::new, ModItems.WATER_UPSTREAM_BLOCK.get()).build(null));
        LAVA_UPSTREAM_TILE = TILES.register("magma_upstream_tile", () -> BlockEntityType.Builder.of(MagmaUpstreamTile::new, ModItems.LAVA_UPSTREAM_BLOCK.get()).build(null));
        AIR_UPSTREAM_TILE = TILES.register("air_upstream_tile", () -> BlockEntityType.Builder.of(AirUpstreamTile::new, ModItems.AIR_UPSTREAM_BLOCK.get()).build(null));
        URN_TILE = TILES.register("everfull_urn", () -> BlockEntityType.Builder.of(EverfullUrnTile::new, ModItems.WATER_URN.get()).build(null));
        ADVANCED_PRISM = TILES.register("advanced_prism", () -> BlockEntityType.Builder.of(AdvancedPrismTile::new, ModItems.ADVANCED_PRISM.get()).build(null));
        ADVANCED_SPLITTER_RELAY = TILES.register("adv_splitter_relay", () -> BlockEntityType.Builder.of(WaterSplitterRelayTile::new, ModItems.WATER_RELAY.get()).build(null));
        ADVANCED_WARP_RELAY = TILES.register("adv_warp_relay", () -> BlockEntityType.Builder.of(AirWarperRelayTile::new, ModItems.AIR_RELAY.get()).build(null));
        ADVANCED_COLLECTOR_RELAY = TILES.register("adv_collect_relay", () -> BlockEntityType.Builder.of(FireCollectorRelayTile::new, ModItems.FIRE_RELAY.get()).build(null));
        ADVANCED_DEPOSITOR_RELAY = TILES.register("adv_deposit_relay", () -> BlockEntityType.Builder.of(EarthDepositorRelayTile::new, ModItems.EARTH_RELAY.get()).build(null));

    }

    public static void addBlocksToTiles(BlockEntityTypeAddBlocksEvent event) {
        event.modify(ModRegistry.FOCUS_TURRET.get(), ModItems.FIRE_TURRET.get(), ModItems.WATER_TURRET.get(), ModItems.AIR_TURRET.get(), ModItems.EARTH_TURRET.get(), ModItems.SHAPING_TURRET.get());
    }

}
