package alexthw.ars_elemental.registry;

import alexthw.ars_elemental.common.MagmaUpstreamTile;
import alexthw.ars_elemental.common.blocks.ElementalSpellTurretTile;
import alexthw.ars_elemental.common.blocks.EverfullUrnTile;
import alexthw.ars_elemental.common.blocks.WaterUpstreamTile;
import alexthw.ars_elemental.common.blocks.mermaid_block.MermaidTile;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static alexthw.ars_elemental.ArsElemental.MODID;

@SuppressWarnings("DataFlowIssue")
public class ModTiles {

    public static final RegistryObject<BlockEntityType<MermaidTile>> MERMAID_TILE;
    public static final RegistryObject<BlockEntityType<WaterUpstreamTile>> WATER_UPSTREAM_TILE;
    public static final RegistryObject<BlockEntityType<MagmaUpstreamTile>> LAVA_UPSTREAM_TILE;
    public static final RegistryObject<BlockEntityType<WaterUpstreamTile>> AIR_UPSTREAM_TILE;

    public static final RegistryObject<BlockEntityType<EverfullUrnTile>> URN_TILE;
    public static final RegistryObject<BlockEntityType<ElementalSpellTurretTile>> ELEMENTAL_TURRET;
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

    static {
        MERMAID_TILE = TILES.register("mermaid_tile", () -> BlockEntityType.Builder.of(MermaidTile::new, ModItems.MERMAID_ROCK.get()).build(null));
        WATER_UPSTREAM_TILE = TILES.register("upstream_tile", () -> BlockEntityType.Builder.of(WaterUpstreamTile::new, ModItems.WATER_UPSTREAM_BLOCK.get()).build(null));
        LAVA_UPSTREAM_TILE = TILES.register("magma_upstream_tile", () -> BlockEntityType.Builder.of(MagmaUpstreamTile::new, ModItems.LAVA_UPSTREAM_BLOCK.get()).build(null));
        AIR_UPSTREAM_TILE = TILES.register("air_upstream_tile", () -> BlockEntityType.Builder.of(WaterUpstreamTile::new, ModItems.AIR_UPSTREAM_BLOCK.get()).build(null));
        ELEMENTAL_TURRET = TILES.register("elemental_turret_tile", () -> BlockEntityType.Builder.of(ElementalSpellTurretTile::new, ModItems.FIRE_TURRET.get(), ModItems.WATER_TURRET.get(), ModItems.AIR_TURRET.get(), ModItems.EARTH_TURRET.get()).build(null));
        URN_TILE = TILES.register("everfull_urn", () -> BlockEntityType.Builder.of(EverfullUrnTile::new, ModItems.WATER_URN.get()).build(null));
    }

}
