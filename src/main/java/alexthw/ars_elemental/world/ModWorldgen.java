package alexthw.ars_elemental.world;

import alexthw.ars_elemental.common.entity.mages.EntityMageBase;
import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.util.SupplierBlockStateProviderAE;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.common.entity.WealdWalker;
import com.hollingsworth.arsnouveau.common.lib.LibBlockNames;
import com.hollingsworth.arsnouveau.common.world.feature.LightFeature;
import com.hollingsworth.arsnouveau.common.world.tree.MagicTrunkPlacer;
import com.hollingsworth.arsnouveau.common.world.tree.SupplierBlockStateProvider;
import com.hollingsworth.arsnouveau.setup.registry.BiomeRegistry;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ModEntities;
import com.hollingsworth.arsnouveau.setup.registry.SoundRegistry;
import com.hollingsworth.arsnouveau.setup.registry.WorldgenRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Musics;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ClampedInt;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.CaveVinesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

import static alexthw.ars_elemental.ArsElemental.MODID;
import static alexthw.ars_elemental.ArsElemental.prefix;
import static alexthw.ars_elemental.registry.ModEntities.*;
import static com.hollingsworth.arsnouveau.setup.registry.BiomeRegistry.globalOverworldGeneration;
import static com.hollingsworth.arsnouveau.setup.registry.BiomeRegistry.softDisks;
import static com.hollingsworth.arsnouveau.setup.registry.WorldgenRegistry.PATCH_BERRY_BUSH;
import static net.minecraft.data.worldgen.placement.CavePlacements.CAVE_VINES;


@SuppressWarnings("OptionalGetWithoutIsPresent")
public class ModWorldgen {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, MODID);

    public static final String SIMPLE_FLASHING_ID = "simple_flashing";
    public static final String COMMON_FLASHING_ID = "common_flashing";

    public static final String RARE_RANDOM_FLASHING_ID = "random_simple_flashing";
    public static final String COMMON_RANDOM_FLASHING_ID = "random_common_flashing";

    public static final String FINAL_RARE_FLASHING = "flashing_archwood";
    public static final String FINAL_COMMON_FLASHING = "common_flashing_archwood";
    public static final String FINAL_CLUSTER_FLASHING = "cluster_flashing_archwood";
    public static final String FINAL_CLUSTER_CASCADING = "cluster_cascading_archwood";
    public static final String FINAL_CLUSTER_BLAZING = "cluster_blazing_archwood";
    public static final String FINAL_CLUSTER_FLOURISHING = "cluster_flourishing_archwood";
    public static final String FINAL_CLUSTER_VEXING = "cluster_vexing_archwood";
    public static final String FINAL_CAVE_VEXING = "cave_vexing_archwood";
    public static final String FINAL_SHORT_VEXING = "short_vexing_archwood";


    public static final ResourceKey<PlacedFeature> PLACED_MOJANK_FLOREST_FLOWERS = registerPlacedKey("mojang_forest_flowers");
    public static final ResourceKey<PlacedFeature> PLACED_MOJANK_PUMPKINS = registerPlacedKey("mojang_pumpkins");
    public static final ResourceKey<PlacedFeature> PLACED_MOJANK_SUGAR_CANE = registerPlacedKey("mojang_sugar_cane");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHORT_VEXING_TREE = registerConfKey("short_vexing_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CAVE_VEXING_TREE = registerConfKey("cave_vexing_tree");
    public static final ResourceKey<PlacedFeature> VEXING_CONFIGURED_CAVE = registerPlacedKey(FINAL_CAVE_VEXING);
    public static final ResourceKey<ConfiguredFeature<?, ?>> FLASHING_TREE_SAPLING = registerConfKey("flashing_tree_sapling");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NATURAL_FLASHING_TREE = registerConfKey("natural_flashing_tree");
    public static final ResourceKey<PlacedFeature> VEXING_CONFIGURED_SHORT = registerPlacedKey(FINAL_SHORT_VEXING);
    public static final ResourceKey<ConfiguredFeature<?, ?>> SOURCESTONE_FORMATION = registerConfKey("sourcestone_formation");
    public static final ResourceKey<PlacedFeature> SIMPLE_FLASHING_PLACED = registerPlacedKey(SIMPLE_FLASHING_ID);
    public static final ResourceKey<PlacedFeature> COMMON_FLASHING_PLACED = registerPlacedKey(COMMON_FLASHING_ID);
    public static final ResourceKey<PlacedFeature> LESS_MANGROVE_PLACED = registerPlacedKey("less_trees_mangrove");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RARE_FLASHING_TREES = registerConfKey(RARE_RANDOM_FLASHING_ID);
    public static final ResourceKey<ConfiguredFeature<?, ?>> COMMON_FLASHING_TREES = registerConfKey(COMMON_RANDOM_FLASHING_ID);
    public static final ResourceKey<PlacedFeature> RARE_FLASHING_CONFIGURED = registerPlacedKey(FINAL_RARE_FLASHING);
    public static final ResourceKey<PlacedFeature> COMMON_FLASHING_CONFIGURED = registerPlacedKey(FINAL_COMMON_FLASHING);
    public static final ResourceKey<PlacedFeature> CLUSTER_FLASHING_CONFIGURED = registerPlacedKey(FINAL_CLUSTER_FLASHING);
    public static final ResourceKey<PlacedFeature> CLUSTER_BLAZING_CONFIGURED = registerPlacedKey(FINAL_CLUSTER_BLAZING);
    public static final ResourceKey<PlacedFeature> CLUSTER_CASCADING_CONFIGURED = registerPlacedKey(FINAL_CLUSTER_CASCADING);
    public static final ResourceKey<PlacedFeature> CLUSTER_FLOURISHING_CONFIGURED = registerPlacedKey(FINAL_CLUSTER_FLOURISHING);
    public static final ResourceKey<PlacedFeature> CLUSTER_VEXING_CONFIGURED = registerPlacedKey(FINAL_CLUSTER_VEXING);
    public static final ResourceKey<PlacedFeature> SOURCESTONE_FORMATION_PLACED = registerPlacedKey("sourcestone_formation_placed");
    public static final DeferredHolder<Feature<?>, SourcestoneFormationFeature> SOURCESTONE_SPIKE = FEATURES.register("sourcestone_spike", () -> new SourcestoneFormationFeature(NoneFeatureConfiguration.CODEC));
    public static final ResourceKey<ConfiguredFeature<?, ?>> POOLS_WITH_DRIP = registerConfKey("pools_with_drip");
    public static final ResourceKey<PlacedFeature> POOLS_WITH_DRIP_PLACED = registerPlacedKey("pools_with_drip_placed");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLACKSTONE_ROCK = registerConfKey("blackstone_rock");
    public static final ResourceKey<PlacedFeature> BLACKSTONE_ROCK_PLACED = registerPlacedKey("blackstone_rock_placed");
    public static final DeferredHolder<Feature<?>, BlackstoneFormation> BLACKSTONE_SPIKE = FEATURES.register("blackstone_formation", () -> new BlackstoneFormation(NoneFeatureConfiguration.CODEC));
    public static final ResourceKey<ConfiguredFeature<?, ?>> QUARTZ_ROCK = registerConfKey("quartz_rock");
    public static final ResourceKey<PlacedFeature> QUARTZ_ROCK_PLACED = registerPlacedKey("quartz_rock_placed");
    public static final DeferredHolder<Feature<?>, QuartzSpikeFeature> QUARTZ_SPIKE = FEATURES.register("quartz_spike", () -> new QuartzSpikeFeature(NoneFeatureConfiguration.CODEC));
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SOURCESTONE = registerConfKey("ore_sourcestone");
    public static final ResourceKey<PlacedFeature> ORE_SOURCESTONE_PLACED = registerPlacedKey("ore_sourcestone_placed");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_CAVE_LIGHTS = registerConfKey("configured_cave_lights");
    public static final ResourceKey<PlacedFeature> PLACED_LIGHTS_UNDERGROUND = registerPlacedKey("placed_lights_underground");
    public static final DeferredHolder<Feature<?>, LightFeature> LIGHT_FEATURE = FEATURES.register("light_feature", () -> new LightFeature(BlockStateConfiguration.CODEC) {

        @Override
        public boolean place(BlockStateConfiguration config, WorldGenLevel seed, ChunkGenerator chunkGenerator, RandomSource rand, BlockPos pos) {
            // get the biome at the position
            int tries = 0;
            while (true) {
                emptyBlockScan:
                {
                    if (tries < 20) {
                        // deeper layer - scan downwards to place on the floor
                        if (pos.getY() < 0) {
                            if (!seed.isEmptyBlock(pos) || seed.isEmptyBlock(pos.below()) || seed.isWaterAt(pos)) {
                                break emptyBlockScan;
                            }
                        } else
                            // upper layer - allow floating lights
                            if (!seed.isEmptyBlock(pos) || seed.isWaterAt(pos)) {
                                break emptyBlockScan;
                            }
                    }
                    if (pos.getY() <= -48 || pos.getY() >= 48 || !seed.isEmptyBlock(pos) || seed.isWaterAt(pos)) {
                        return false;
                    }
                    seed.setBlock(pos, config.state, 4);
                    onStatePlace(seed, chunkGenerator, rand, pos, config);
                    return true;
                }
                tries++;
                pos = pos.below();
            }
        }
    });
    public static final ResourceKey<ConfiguredFeature<?, ?>> SINGLE_TORCHFLOWER = registerConfKey("single_torchflower");
    public static final ResourceKey<PlacedFeature> SCATTERED_TORCHFLOWERS = registerPlacedKey("scattered_torchflowers");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SINGLE_SPARKFLOWER = registerConfKey("single_sparkflower");
    public static final ResourceKey<PlacedFeature> SCATTERED_SPARKFLOWERS = registerPlacedKey("scattered_sparkflowers");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SINGLE_BLOSSOM = registerConfKey("single_blossom");
    public static final ResourceKey<PlacedFeature> SCATTERED_BLOSSOMS = registerPlacedKey("scattered_blossoms");
    public static final ResourceKey<PlacedFeature> LAVA_POOLS = registerPlacedKey("lava_pools");
    public static final ResourceKey<PlacedFeature> SPARSE_JUNGLE = registerPlacedKey("sparse_jungle_trees");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SOURCE_CAVE_VINES = registerConfKey("cave_source_vines");
    public static final ResourceKey<PlacedFeature> CEILING_BERRY_CAVE = registerPlacedKey("ceiling_berry_cave");
    public static final ResourceKey<PlacedFeature> PLACED_BERRY_BUSH_CAVE = registerPlacedKey("placed_berry_bush_cave");
    public static final ResourceKey<ConfiguredWorldCarver<?>> CAVE_CARVER = ResourceKey.create(Registries.CONFIGURED_CARVER, prefix("vexing_cave_carver"));

    public static ResourceKey<ConfiguredFeature<?, ?>> registerConfKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(MODID, name));
    }

    public static ResourceKey<PlacedFeature> registerPlacedKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(MODID, name));
    }

    public static void bootstrapCaveCarvers(BootstrapContext<ConfiguredWorldCarver<?>> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);
        HolderGetter<Block> holdergetter = context.lookup(Registries.BLOCK);

        context.register(CAVE_CARVER, WorldCarver.CAVE.configured(
                new CaveCarverConfiguration(
                        0.20F,
                        UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(180)),
                        UniformFloat.of(2.F, 5.0F),
                        VerticalAnchor.aboveBottom(8),
                        CarverDebugSettings.of(false, BlockRegistry.ARCHWOOD_BUTTON.defaultBlockState()),
                        holdergetter.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),
                        UniformFloat.of(2.0F, 6F),
                        UniformFloat.of(2F, 6F),
                        UniformFloat.of(-1.0F, -0.4F)
                ))
        );
    }

    public static void bootstrapConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredFeature<?, ?>> holdergetter = context.lookup(Registries.CONFIGURED_FEATURE);

        WorldgenRegistry.bootstrapConfiguredFeatures(context);

        context.register(FLASHING_TREE_SAPLING, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                new SupplierBlockStateProviderAE("yellow_archwood_log"),
                new MagicTrunkPlacer(10, 1, 0, false, "ars_elemental:flashpine_pod"),
                new SupplierBlockStateProviderAE("yellow_archwood_leaves"),
                new BlobFoliagePlacer(UniformInt.of(0, 0), UniformInt.of(0, 0), 0),
                new TwoLayersFeatureSize(2, 0, 2)).ignoreVines().build()));

        context.register(NATURAL_FLASHING_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                new SupplierBlockStateProviderAE("yellow_archwood_log"),
                new MagicTrunkPlacer(10, 2, 0, true, "ars_elemental:flashpine_pod"),
                new SupplierBlockStateProviderAE("yellow_archwood_leaves"),
                new BlobFoliagePlacer(UniformInt.of(0, 0), UniformInt.of(0, 0), 0),
                new TwoLayersFeatureSize(2, 0, 2)).build()));

        context.register(RARE_FLASHING_TREES, new ConfiguredFeature<>(Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(HolderSet.direct(placed.getOrThrow(SIMPLE_FLASHING_PLACED)))));
        context.register(COMMON_FLASHING_TREES, new ConfiguredFeature<>(Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(HolderSet.direct(placed.getOrThrow(COMMON_FLASHING_PLACED)))));

        context.register(SHORT_VEXING_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(new SupplierBlockStateProvider(LibBlockNames.VEXING_LOG),
                new MagicTrunkPlacer(1, 3, 2, true, ArsNouveau.prefix(LibBlockNames.BASTION_POD).toString()),
                new SupplierBlockStateProvider(LibBlockNames.VEXING_LEAVES),
                new BlobFoliagePlacer(UniformInt.of(0, 0), UniformInt.of(0, 0), 0),
                new TwoLayersFeatureSize(2, 0, 2)).build()));

        context.register(CAVE_VEXING_TREE, new ConfiguredFeature<>(Feature.ROOT_SYSTEM,
                new RootSystemConfiguration(
                        PlacementUtils.inlinePlaced(holdergetter.getOrThrow(SHORT_VEXING_TREE)),
                        3,
                        3,
                        BlockTags.AZALEA_ROOT_REPLACEABLE,
                        BlockStateProvider.simple(Blocks.ROOTED_DIRT),
                        20,
                        30,
                        3,
                        2,
                        BlockStateProvider.simple(Blocks.HANGING_ROOTS),
                        20,
                        2,
                        BlockPredicate.allOf(
                                BlockPredicate.anyOf(
                                        BlockPredicate.matchesBlocks(List.of(Blocks.AIR, Blocks.CAVE_AIR, Blocks.VOID_AIR)),
                                        BlockPredicate.matchesTag(BlockTags.REPLACEABLE_BY_TREES)
                                ),
                                BlockPredicate.matchesTag(Direction.DOWN.getNormal(), BlockTags.AZALEA_GROWS_ON))
                )
        ));

        context.register(POOLS_WITH_DRIP, new ConfiguredFeature<>(Feature.WATERLOGGED_VEGETATION_PATCH, new VegetationPatchConfiguration(BlockTags.LUSH_GROUND_REPLACEABLE, new WeightedStateProvider(
                SimpleWeightedRandomList.<BlockState>builder()
                        .add(Blocks.GRASS_BLOCK.defaultBlockState(), 75)
                        .add(Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 5)
                        .add(Blocks.WATER.defaultBlockState(), 20)
                        .build()
        ), PlacementUtils.inlinePlaced(holdergetter.getOrThrow(CaveFeatures.DRIPLEAF)), CaveSurface.FLOOR, ConstantInt.of(3), 0.8F, 3, 0.1F, UniformInt.of(4, 7), 0.4F)));

        context.register(BLACKSTONE_ROCK, new ConfiguredFeature<>(BLACKSTONE_SPIKE.get(), NoneFeatureConfiguration.INSTANCE));
        context.register(QUARTZ_ROCK, new ConfiguredFeature<>(QUARTZ_SPIKE.get(), NoneFeatureConfiguration.INSTANCE));
        context.register(SOURCESTONE_FORMATION, new ConfiguredFeature<>(SOURCESTONE_SPIKE.get(), NoneFeatureConfiguration.INSTANCE));
        context.register(SINGLE_TORCHFLOWER, new ConfiguredFeature<>(
                Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.TORCHFLOWER.defaultBlockState()))
        ));
        context.register(SINGLE_SPARKFLOWER, new ConfiguredFeature<>(
                Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModItems.SPARKFLOWER.get().defaultBlockState()))
        ));
        context.register(SINGLE_BLOSSOM, new ConfiguredFeature<>(
                Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModItems.GROUND_BLOSSOM.get().defaultBlockState()))
        ));

        context.register(CONFIGURED_CAVE_LIGHTS, new ConfiguredFeature<>(LIGHT_FEATURE.get(), new BlockStateConfiguration(BlockRegistry.LIGHT_BLOCK.get().defaultBlockState())));
        WeightedStateProvider weightedstateprovider = new WeightedStateProvider(
                SimpleWeightedRandomList.<BlockState>builder()
                        .add(ModItems.SOURCE_VINES.get().defaultBlockState(), 4)
                        .add(ModItems.SOURCE_VINES_PLANT.get().defaultBlockState().setValue(CaveVines.BERRIES, Boolean.TRUE), 1)
        );
        RandomizedIntStateProvider randomizedintstateprovider = new RandomizedIntStateProvider(
                new WeightedStateProvider(
                        SimpleWeightedRandomList.<BlockState>builder()
                                .add(ModItems.SOURCE_VINES.get().defaultBlockState(), 4)
                                .add(ModItems.SOURCE_VINES.get().defaultBlockState().setValue(CaveVines.BERRIES, Boolean.TRUE), 1)
                ),
                CaveVinesBlock.AGE,
                UniformInt.of(23, 25)
        );
        context.register(SOURCE_CAVE_VINES, new ConfiguredFeature<>(
                Feature.BLOCK_COLUMN,
                new BlockColumnConfiguration(
                        List.of(
                                BlockColumnConfiguration.layer(
                                        new WeightedListInt(
                                                SimpleWeightedRandomList.<IntProvider>builder()
                                                        .add(UniformInt.of(0, 19), 2)
                                                        .add(UniformInt.of(0, 2), 3)
                                                        .add(UniformInt.of(0, 6), 10)
                                                        .build()
                                        ),
                                        weightedstateprovider
                                ),
                                BlockColumnConfiguration.layer(ConstantInt.of(1), randomizedintstateprovider)
                        ),
                        Direction.DOWN,
                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                        true
                )));

        context.register(ORE_SOURCESTONE, new ConfiguredFeature<>(
                Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD), BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, LibBlockNames.SOURCESTONE)).defaultBlockState(), 64)));
    }

    public static void bootstrapPlacedFeatures(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);

        WorldgenRegistry.bootstrapPlacedFeatures(context);

        context.register(SIMPLE_FLASHING_PLACED, new PlacedFeature(configured.get(NATURAL_FLASHING_TREE).get(), List.of(PlacementUtils.filteredByBlockSurvival(ModItems.FLASHING_SAPLING.get()))));
        context.register(COMMON_FLASHING_PLACED, new PlacedFeature(configured.get(NATURAL_FLASHING_TREE).get(), List.of(PlacementUtils.countExtra(5, 0.01F, 1), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, PlacementUtils.filteredByBlockSurvival(ModItems.FLASHING_SAPLING.get()))));

        context.register(RARE_FLASHING_CONFIGURED, new PlacedFeature(configured.get(RARE_FLASHING_TREES).get(), VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(200))));
        context.register(COMMON_FLASHING_CONFIGURED, new PlacedFeature(configured.get(COMMON_FLASHING_TREES).get(), VegetationPlacements.treePlacement(CountPlacement.of(3), ModItems.FLASHING_SAPLING.get())));

        context.register(CLUSTER_FLASHING_CONFIGURED, new PlacedFeature(configured.get(COMMON_FLASHING_TREES).get(), VegetationPlacements.treePlacement(CountPlacement.of(4), ModItems.FLASHING_SAPLING.get())));

        context.register(CLUSTER_CASCADING_CONFIGURED, new PlacedFeature(configured.get(WorldgenRegistry.NATURAL_CONFIGURED_CASCADING_TREE).get(), VegetationPlacements.treePlacement(CountPlacement.of(4), BlockRegistry.CASCADING_SAPLING.get())));
        context.register(CLUSTER_BLAZING_CONFIGURED, new PlacedFeature(configured.get(WorldgenRegistry.NATURAL_CONFIGURED_BLAZING_TREE).get(), VegetationPlacements.treePlacement(CountPlacement.of(4), BlockRegistry.BLAZING_SAPLING.get())));
        context.register(CLUSTER_FLOURISHING_CONFIGURED, new PlacedFeature(configured.get(WorldgenRegistry.NATURAL_CONFIGURED_FLOURISHING_TREE).get(), VegetationPlacements.treePlacement(CountPlacement.of(4), BlockRegistry.FLOURISHING_SAPLING.get())));
        context.register(CLUSTER_VEXING_CONFIGURED, new PlacedFeature(configured.get(WorldgenRegistry.NATURAL_CONFIGURED_VEXING_TREE).get(), VegetationPlacements.treePlacement(CountPlacement.of(4), BlockRegistry.VEXING_SAPLING.get())));

        context.register(VEXING_CONFIGURED_CAVE, new PlacedFeature(configured.get(CAVE_VEXING_TREE).get(),
                List.of(new PlacementModifier[]{
                        CountPlacement.of(105),
                        InSquarePlacement.spread(),
                        PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                        EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                        RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                        BiomeFilter.biome()}
                )));

        context.register(VEXING_CONFIGURED_SHORT, new PlacedFeature(configured.get(SHORT_VEXING_TREE).get(),
                List.of(new PlacementModifier[]{
                        CountPlacement.of(75),
                        InSquarePlacement.spread(),
                        PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                        SurfaceWaterDepthFilter.forMaxDepth(3),
                        EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                        RandomOffsetPlacement.vertical(ConstantInt.of(1)),
                        BiomeFilter.biome()}
                )));

        context.register(LESS_MANGROVE_PLACED, new PlacedFeature(configured.get(ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.withDefaultNamespace("mangrove_vegetation"))).get(),
                        List.of(new PlacementModifier[]{
                                        CountPlacement.of(5),
                                        InSquarePlacement.spread(),
                                        SurfaceWaterDepthFilter.forMaxDepth(5),
                                        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                                        BiomeFilter.biome(),
                                        BlockPredicateFilter.forPredicate(
                                                BlockPredicate.allOf(
                                                        BlockPredicate.wouldSurvive(Blocks.MANGROVE_PROPAGULE.defaultBlockState(), BlockPos.ZERO),
                                                        BlockPredicate.matchesFluids(Fluids.WATER)
                                                )
                                        )
                                }
                        )
                )
        );

        context.register(PLACED_BERRY_BUSH_CAVE, new PlacedFeature(configured.get(PATCH_BERRY_BUSH).get(), List.of(
                RarityFilter.onAverageOnceEvery(2),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                BiomeFilter.biome()))
        );

        context.register(PLACED_LIGHTS_UNDERGROUND, new PlacedFeature(configured.get(CONFIGURED_CAVE_LIGHTS).get(),
                List.of(CountPlacement.of(7),
                        InSquarePlacement.spread(),
                        PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                        BiomeFilter.biome()))
        );

        context.register(POOLS_WITH_DRIP_PLACED, new PlacedFeature(configured.get(POOLS_WITH_DRIP).get(), List.of(new PlacementModifier[]{
                CountPlacement.of(15),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(1)),
                BiomeFilter.biome()
        })));

        context.register(BLACKSTONE_ROCK_PLACED, new PlacedFeature(configured.get(BLACKSTONE_ROCK).get(), List.of(new PlacementModifier[]{
                RarityFilter.onAverageOnceEvery(10),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        })));

        context.register(QUARTZ_ROCK_PLACED, new PlacedFeature(configured.get(QUARTZ_ROCK).get(), List.of(new PlacementModifier[]{
                RarityFilter.onAverageOnceEvery(5),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        })));

        context.register(SOURCESTONE_FORMATION_PLACED, new PlacedFeature(configured.get(SOURCESTONE_FORMATION).get(), List.of(new PlacementModifier[]{
                RarityFilter.onAverageOnceEvery(2),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                BiomeFilter.biome()
        })));

        context.register(SCATTERED_TORCHFLOWERS, new PlacedFeature(
                configured.get(SINGLE_TORCHFLOWER).get(),
                List.of(
                        CountPlacement.of(2),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome()
                )
        ));

        context.register(SCATTERED_SPARKFLOWERS, new PlacedFeature(
                configured.get(SINGLE_SPARKFLOWER).get(),
                List.of(
                        CountPlacement.of(4),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome()
                )
        ));

        context.register(SCATTERED_BLOSSOMS, new PlacedFeature(
                configured.get(SINGLE_BLOSSOM).get(),
                List.of(
                        CountPlacement.of(3),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome()
                )
        ));

        context.register(LAVA_POOLS, new PlacedFeature(
                configured.get(MiscOverworldFeatures.LAKE_LAVA).get(),
                List.of(
                        RarityFilter.onAverageOnceEvery(10),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome()
                )
        ));
        context.register(PLACED_MOJANK_FLOREST_FLOWERS, new PlacedFeature(configured.get(VegetationFeatures.FOREST_FLOWERS).get(), List.of(RarityFilter.onAverageOnceEvery(5),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                CountPlacement.of(ClampedInt.of(UniformInt.of(-3, 1), 0, 1)),
                BiomeFilter.biome())));

        context.register(PLACED_MOJANK_PUMPKINS, new PlacedFeature(configured.get(VegetationFeatures.PATCH_PUMPKIN).get(),
                List.of(RarityFilter.onAverageOnceEvery(300),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome()
                )));

        context.register(PLACED_MOJANK_SUGAR_CANE, new PlacedFeature(configured.get(VegetationFeatures.PATCH_SUGAR_CANE).get(),
                List.of(RarityFilter.onAverageOnceEvery(6),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome())
        ));


        context.register(SPARSE_JUNGLE, new PlacedFeature(configured.get(VegetationFeatures.TREES_JUNGLE).get(), VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.1F, 1))));

        context.register(CEILING_BERRY_CAVE, new PlacedFeature(configured.get(SOURCE_CAVE_VINES).get(),
                List.of(CountPlacement.of(88),
                        InSquarePlacement.spread(),
                        PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                        EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.hasSturdyFace(Direction.DOWN), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                        RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                        BiomeFilter.biome())
        ));

        context.register(ORE_SOURCESTONE_PLACED, new PlacedFeature(configured.get(ORE_SOURCESTONE).get(),
                List.of(CountPlacement.of(2), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(30)), BiomeFilter.biome())
        ));

    }

    public static class Biomes {

        public static final String FLASHING_FOREST_ID = "flashing_forest";
        public static final String BLAZING_FOREST_ID = "blazing_forest";
        public static final String CASCADING_FOREST_ID = "cascading_forest";
        public static final String FLOURISHING_FOREST_ID = "flourishing_forest";
        public static final String VEXING_CAVES_ID = "vexing_caves";
        public static final ResourceKey<Biome> FLASHING_FOREST_KEY = register(FLASHING_FOREST_ID);
        public static final ResourceKey<Biome> BLAZING_FOREST_KEY = register(BLAZING_FOREST_ID);
        public static final ResourceKey<Biome> CASCADING_FOREST_KEY = register(CASCADING_FOREST_ID);
        public static final ResourceKey<Biome> FLOURISHING_FOREST_KEY = register(FLOURISHING_FOREST_ID);
        public static final ResourceKey<Biome> VEXING_CAVES_KEY = register(VEXING_CAVES_ID);

        public static final ResourceLocation[] ArchwoodBiomes = new ResourceLocation[]{
                FLASHING_FOREST_KEY.location(),
                BLAZING_FOREST_KEY.location(),
                CASCADING_FOREST_KEY.location(),
                FLOURISHING_FOREST_KEY.location()
        };

        public static ResourceKey<Biome> register(String name) {
            return ResourceKey.create(Registries.BIOME, prefix(name));
        }

        public static void registerBiomes(BootstrapContext<Biome> context) {

            BiomeRegistry.bootstrap(context);

            context.register(FLASHING_FOREST_KEY, flashingArchwoodForest(context));
            context.register(BLAZING_FOREST_KEY, blazingArchwoodForest(context));
            context.register(CASCADING_FOREST_KEY, cascadingArchwoodForest(context));
            context.register(FLOURISHING_FOREST_KEY, flourishArchwoodForest(context));
            context.register(VEXING_CAVES_KEY, vexingCaves(context));

        }

        public static Biome flashingArchwoodForest(BootstrapContext<Biome> context) {
            MobSpawnSettings.Builder spawnBuilder = archwoodSpawns(AIR_MAGE.get(), FLASHING_WEALD_WALKER.get(), null);
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.BREEZE, 5, 1, 1));
            spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.GOAT, 6, 1, 2));
            spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(FLASHJACK_ENTITY.get(), 35, 1, 2));
            BiomeGenerationSettings.Builder biomeBuilder = getArchwoodBiomeBuilder(CLUSTER_FLASHING_CONFIGURED, context, QUARTZ_ROCK_PLACED, VegetationPlacements.TREES_WINDSWEPT_HILLS);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SCATTERED_SPARKFLOWERS);

            return new Biome.BiomeBuilder()
                    .hasPrecipitation(true)
                    .downfall(0.8f)
                    .temperature(0.4f)
                    .generationSettings(biomeBuilder.build())
                    .mobSpawnSettings(spawnBuilder.build())
                    .specialEffects((new BiomeSpecialEffects.Builder())
                            .waterColor(7978751)
                            .waterFogColor(329011)
                            .skyColor(7978751)
                            .grassColorOverride(13414701)
                            .foliageColorOverride(13084948)
                            .fogColor(12638463)
                            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                            .backgroundMusic(Musics.createGameMusic(SoundRegistry.ARIA_BIBLIO)).build())
                    .build();
        }

        private static Biome blazingArchwoodForest(BootstrapContext<Biome> context) {
            MobSpawnSettings.Builder spawnBuilder = archwoodSpawns(FIRE_MAGE.get(), ModEntities.ENTITY_BLAZING_WEALD.get(), EntityType.HUSK);
            spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.ARMADILLO, 6, 1, 2));

            BiomeGenerationSettings.Builder biomeBuilder = getArchwoodBiomeBuilder(CLUSTER_BLAZING_CONFIGURED, context, BLACKSTONE_ROCK_PLACED, VegetationPlacements.TREES_WINDSWEPT_SAVANNA);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SCATTERED_TORCHFLOWERS);
            biomeBuilder.addFeature(GenerationStep.Decoration.LAKES, LAVA_POOLS);

            return new Biome.BiomeBuilder().hasPrecipitation(false)
                    .downfall(0.1f)
                    .temperature(0.9f)
                    .generationSettings(biomeBuilder.build())
                    .mobSpawnSettings(spawnBuilder.build())
                    .specialEffects((new BiomeSpecialEffects.Builder())
                            .waterColor(7978751)
                            .waterFogColor(329011)
                            .skyColor(7978751)
                            .grassColorOverride(13269556)
                            .foliageColorOverride(12679744)
                            .fogColor(12638463)
                            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                            .backgroundMusic(Musics.createGameMusic(SoundRegistry.ARIA_BIBLIO)).build())
                    .build();

        }

        private static Biome cascadingArchwoodForest(BootstrapContext<Biome> context) {
            MobSpawnSettings.Builder spawnBuilder = archwoodSpawns(WATER_MAGE.get(), ModEntities.ENTITY_CASCADING_WEALD.get(), EntityType.DROWNED);
            // add fish
            spawnBuilder.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.COD, 15, 3, 6));
            spawnBuilder.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 15, 1, 5));
            spawnBuilder.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 25, 8, 8));
            spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.FROG, 6, 1, 3));

            BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));
            //we need to follow the same order as vanilla biomes for the BiomeDefaultFeatures
            globalOverworldGeneration(biomeBuilder);
            BiomeDefaultFeatures.addFerns(biomeBuilder);
            BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
            BiomeDefaultFeatures.addExtraGold(biomeBuilder);
            softDisks(biomeBuilder);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, LESS_MANGROVE_PLACED);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_NORMAL);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_WATERLILY);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, POOLS_WITH_DRIP_PLACED);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.WARM_OCEAN_VEGETATION)
                    .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_WARM)
                    .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEA_PICKLE);
            addDefaultExtraVegetation(biomeBuilder);
            biomeBuilder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, WorldgenRegistry.PLACED_LIGHTS);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CLUSTER_CASCADING_CONFIGURED);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenRegistry.PLACED_MOJANK_GRASS);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenRegistry.PLACED_MOJANK_FLOWERS);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PLACED_MOJANK_FLOREST_FLOWERS);

            return new Biome.BiomeBuilder().hasPrecipitation(true)
                    .downfall(0.8f)
                    .temperature(0.7f)
                    .generationSettings(biomeBuilder.build())
                    .mobSpawnSettings(spawnBuilder.build())
                    .specialEffects((new BiomeSpecialEffects.Builder())
                            .waterColor(7978751)
                            .waterFogColor(329011)
                            .skyColor(7978751)
                            .grassColorOverride(1149867)
                            .foliageColorOverride(2210437)
                            .fogColor(12638463)
                            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                            .backgroundMusic(Musics.createGameMusic(SoundRegistry.ARIA_BIBLIO)).build())
                    .build();
        }

        private static Biome flourishArchwoodForest(BootstrapContext<Biome> context) {
            MobSpawnSettings.Builder spawnBuilder = archwoodSpawns(EARTH_MAGE.get(), ModEntities.ENTITY_FLOURISHING_WEALD.get(), EntityType.BOGGED);
            spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PANDA, 6, 1, 3));

            BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));
            //we need to follow the same order as vanilla biomes for the BiomeDefaultFeatures
            globalOverworldGeneration(biomeBuilder);
            BiomeDefaultFeatures.addMossyStoneBlock(biomeBuilder);
            BiomeDefaultFeatures.addLightBambooVegetation(biomeBuilder);
            BiomeDefaultFeatures.addFerns(biomeBuilder);
            BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
            BiomeDefaultFeatures.addExtraGold(biomeBuilder);
            BiomeDefaultFeatures.addDefaultSoftDisks(biomeBuilder);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CLUSTER_FLOURISHING_CONFIGURED);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SPARSE_JUNGLE);
            BiomeDefaultFeatures.addWarmFlowers(biomeBuilder);
            BiomeDefaultFeatures.addJungleGrass(biomeBuilder);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.LUSH_CAVES_CEILING_VEGETATION);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CAVE_VINES);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.ROOTED_AZALEA_TREE);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.SPORE_BLOSSOM);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.CLASSIC_VINES);
            BiomeDefaultFeatures.addDefaultMushrooms(biomeBuilder);
            addDefaultExtraVegetation(biomeBuilder);
            biomeBuilder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, WorldgenRegistry.PLACED_LIGHTS);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenRegistry.PLACED_MOJANK_GRASS);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenRegistry.PLACED_MOJANK_FLOWERS);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PLACED_MOJANK_FLOREST_FLOWERS);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SCATTERED_BLOSSOMS);
            return new Biome.BiomeBuilder()
                    .hasPrecipitation(true)
                    .downfall(0.6f)
                    .temperature(0.7f)
                    .generationSettings(biomeBuilder.build())
                    .mobSpawnSettings(spawnBuilder.build())
                    .specialEffects((new BiomeSpecialEffects.Builder())
                            .waterColor(7978751)
                            .waterFogColor(329011)
                            .skyColor(7978751)
                            .grassColorOverride(1346066)
                            .foliageColorOverride(30464)
                            .fogColor(12638463)
                            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                            .backgroundMusic(Musics.createGameMusic(SoundRegistry.ARIA_BIBLIO)).build())
                    .build();
        }

        private static MobSpawnSettings.Builder archwoodSpawns(EntityType<EntityMageBase> mage, EntityType<WealdWalker> ww, EntityType<?> biomeMob) {
            MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
            spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.STARBUNCLE_TYPE.get(), 2, 3, 5));
            spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.ENTITY_DRYGMY.get(), 2, 1, 3));
            spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.WHIRLISPRIG_TYPE.get(), 2, 1, 3));
            BiomeDefaultFeatures.farmAnimals(spawnBuilder);
            spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 6, 2, 3));

            BiomeDefaultFeatures.caveSpawns(spawnBuilder);
            // unwrap the monster method so we can override the biome-specific dominant mob
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 60, 4, 4));
            if (biomeMob != null)
                spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(biomeMob, 60, 2, 4));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 20, 4, 4));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_VILLAGER, 5, 1, 1));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 60, 4, 4));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 60, 4, 4));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 40, 4, 4));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 10, 1, 4));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 5, 1, 1));

            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(mage, 4, 1, 3));
            spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ww, 10, 1, 3));
            return spawnBuilder;
        }

        private static BiomeGenerationSettings.Builder getArchwoodBiomeBuilder(ResourceKey<PlacedFeature> archwoodCluster, BootstrapContext<Biome> context, ResourceKey<PlacedFeature> rock, ResourceKey<PlacedFeature> vanillatree) {
            BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));
            //we need to follow the same order as vanilla biomes for the BiomeDefaultFeatures
            globalOverworldGeneration(biomeBuilder);

            if (rock != null)
                biomeBuilder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, rock);
            BiomeDefaultFeatures.addFerns(biomeBuilder);
            BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
            BiomeDefaultFeatures.addExtraGold(biomeBuilder);
            softDisks(biomeBuilder);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, vanillatree);

            BiomeDefaultFeatures.addDefaultMushrooms(biomeBuilder);
            addDefaultExtraVegetation(biomeBuilder);
            biomeBuilder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, WorldgenRegistry.PLACED_LIGHTS);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, archwoodCluster);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenRegistry.PLACED_MOJANK_GRASS);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenRegistry.PLACED_MOJANK_FLOWERS);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PLACED_MOJANK_FLOREST_FLOWERS);
            return biomeBuilder;
        }

        static void addDefaultExtraVegetation(BiomeGenerationSettings.Builder builder) {
            builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PLACED_MOJANK_PUMPKINS);
            builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PLACED_MOJANK_SUGAR_CANE);
        }

        public static Biome vexingCaves(BootstrapContext<Biome> context) {
            MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 50, 2, 4));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 55, 2, 4));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_VILLAGER, 5, 1, 1));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 50, 2, 4));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 50, 2, 4));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 100, 4, 4));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 30, 1, 4));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 15, 1, 1));

            spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.STARBUNCLE_TYPE.get(), 2, 3, 5));
            spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.ENTITY_DRYGMY.get(), 2, 1, 3));
            spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.WHIRLISPRIG_TYPE.get(), 2, 1, 3));
            spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.ENTITY_VEXING_WEALD.get(), 10, 1, 3));

            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntities.WILDEN_HUNTER.get(), 100, 3, 5));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntities.WILDEN_STALKER.get(), 100, 1, 3));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntities.WILDEN_GUARDIAN.get(), 80, 2, 5));


            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AIR_MAGE.get(), 4, 1, 3));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(FIRE_MAGE.get(), 4, 1, 3));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(WATER_MAGE.get(), 4, 1, 3));
            spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EARTH_MAGE.get(), 4, 1, 3));

            BiomeGenerationSettings.Builder biomeBuilder = getArchwoodBiomeBuilder(CLUSTER_VEXING_CONFIGURED, context, SOURCESTONE_FORMATION_PLACED, CavePlacements.ROOTED_AZALEA_TREE);
            biomeBuilder.addCarver(GenerationStep.Carving.AIR, CAVE_CARVER);
            biomeBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_SOURCESTONE_PLACED);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CEILING_BERRY_CAVE);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PLACED_BERRY_BUSH_CAVE);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PLACED_LIGHTS_UNDERGROUND);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VEXING_CONFIGURED_CAVE);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VEXING_CONFIGURED_SHORT);
            biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SOURCESTONE_FORMATION_PLACED);

            return new Biome.BiomeBuilder()
                    .hasPrecipitation(true)
                    .downfall(0.5f)
                    .temperature(0.5f)
                    .generationSettings(biomeBuilder.build())
                    .mobSpawnSettings(spawnBuilder.build())
                    .specialEffects((new BiomeSpecialEffects.Builder())
                            .waterColor(12080623)
                            .waterFogColor(11832560)
                            .skyColor(10979583)
                            .grassColorOverride(6566546)
                            .foliageColorOverride(8535186)
                            .fogColor(14386175)
                            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                            .backgroundMusic(Musics.createGameMusic(SoundRegistry.WILD_HUNT)).build())
                    .build();
        }

    }

}
