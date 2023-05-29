package alexthw.ars_elemental.world;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.util.SupplierBlockStateProviderAE;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.common.entity.ModEntities;
import com.hollingsworth.arsnouveau.common.world.DefaultFeatures;
import com.hollingsworth.arsnouveau.common.world.WorldEvent;
import com.hollingsworth.arsnouveau.common.world.tree.MagicTrunkPlacer;
import com.hollingsworth.arsnouveau.setup.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.SoundRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Musics;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static alexthw.ars_elemental.ArsElemental.prefix;
import static alexthw.ars_elemental.registry.ModEntities.FLASHING_WEALD_WALKER;
import static com.hollingsworth.arsnouveau.common.world.biome.ModBiomes.biome;
import static com.hollingsworth.arsnouveau.common.world.biome.ModBiomes.globalOverworldGeneration;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class ModWorldgen {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, ArsElemental.MODID);
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFG_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, ArsElemental.MODID);
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, ArsElemental.MODID);

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, ArsElemental.MODID);

    public static final String FLASHING_FOREST_ID = "flashing_forest";
    public static final String BLAZING_FOREST_ID = "blazing_forest";
    public static final String CASCADING_FOREST_ID = "cascading_forest";
    public static final String FLOURISHING_FOREST_ID = "flourishing_forest";
    public static final ResourceKey<Biome> FLASHING_FOREST_KEY = register(FLASHING_FOREST_ID);
    public static final ResourceKey<Biome> BLAZING_FOREST_KEY = register(BLAZING_FOREST_ID);
    public static final ResourceKey<Biome> CASCADING_FOREST_KEY = register(CASCADING_FOREST_ID);
    public static final ResourceKey<Biome> FLOURISHING_FOREST_KEY = register(FLOURISHING_FOREST_ID);

    public static ResourceKey<Biome> register(String name) {
        return ResourceKey.create(Registry.BIOME_REGISTRY, prefix(name));
    }

    public static final RegistryObject<Biome> FLASHING_FOREST = BIOMES.register(FLASHING_FOREST_ID, ModWorldgen::flashingArchwoodForest);
    public static final RegistryObject<Biome> BLAZING_FOREST = BIOMES.register(BLAZING_FOREST_ID, ModWorldgen::blazingArchwoodForest);
    public static final RegistryObject<Biome> CASCADING_FOREST = BIOMES.register(CASCADING_FOREST_ID, ModWorldgen::cascadingArchwoodForest);
    public static final RegistryObject<Biome> FLOURISHING_FOREST = BIOMES.register(FLOURISHING_FOREST_ID, ModWorldgen::flourishArchwoodForest);


    public static final RegistryObject<ConfiguredFeature<TreeConfiguration, ?>> FLASHING_TREE_SAPLING;
    public static final RegistryObject<ConfiguredFeature<TreeConfiguration, ?>> NATURAL_FLASHING_TREE;

    public static final RegistryObject<PlacedFeature> SIMPLE_FLASHING_PLACED;
    public static final RegistryObject<PlacedFeature> COMMON_FLASHING_PLACED;

    public static final RegistryObject<ConfiguredFeature<SimpleRandomFeatureConfiguration, ?>> RARE_FLASHING_TREES;
    public static final RegistryObject<ConfiguredFeature<SimpleRandomFeatureConfiguration, ?>> COMMON_FLASHING_TREES;

    public static final RegistryObject<PlacedFeature> RARE_FLASHING_CONFIGURED;
    public static final RegistryObject<PlacedFeature> COMMON_FLASHING_CONFIGURED;
    public static final RegistryObject<PlacedFeature> CLUSTER_FLASHING_CONFIGURED;

    public static final RegistryObject<PlacedFeature> CLUSTER_BLAZING_CONFIGURED;
    public static final RegistryObject<PlacedFeature> CLUSTER_CASCADING_CONFIGURED;
    public static final RegistryObject<PlacedFeature> CLUSTER_FLOURISHING_CONFIGURED;
    public static final RegistryObject<PlacedFeature> CLUSTER_VEXING_CONFIGURED;

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


    static {
        FLASHING_TREE_SAPLING = CONFG_FEATURES.register("flashing_feature", () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                new SupplierBlockStateProviderAE("yellow_archwood_log"),
                new MagicTrunkPlacer(10, 1, 0, false, "ars_elemental:flashpine_pod"),
                new SupplierBlockStateProviderAE("yellow_archwood_leaves"),
                new BlobFoliagePlacer(UniformInt.of(0, 0), UniformInt.of(0, 0), 0),
                new TwoLayersFeatureSize(2, 0, 2)).ignoreVines().build()));

        NATURAL_FLASHING_TREE = CONFG_FEATURES.register("natural_flashing_feature", () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                new SupplierBlockStateProviderAE("yellow_archwood_log"),
                new MagicTrunkPlacer(10, 2, 0, true, "ars_elemental:flashpine_pod"),
                new SupplierBlockStateProviderAE("yellow_archwood_leaves"),
                new BlobFoliagePlacer(UniformInt.of(0, 0), UniformInt.of(0, 0), 0),
                new TwoLayersFeatureSize(2, 0, 2)).build()));

        SIMPLE_FLASHING_PLACED = PLACED_FEATURES.register(SIMPLE_FLASHING_ID, () -> new PlacedFeature(Holder.direct(NATURAL_FLASHING_TREE.get()), List.of(PlacementUtils.filteredByBlockSurvival(ModItems.FLASHING_SAPLING.get()))));
        COMMON_FLASHING_PLACED = PLACED_FEATURES.register(COMMON_FLASHING_ID, () -> new PlacedFeature(Holder.direct(NATURAL_FLASHING_TREE.get()), List.of(PlacementUtils.countExtra(5, 0.01F, 1), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, PlacementUtils.filteredByBlockSurvival(ModItems.FLASHING_SAPLING.get()))));

        RARE_FLASHING_TREES = CONFG_FEATURES.register(RARE_RANDOM_FLASHING_ID, () -> new ConfiguredFeature<>(Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(HolderSet.direct(SIMPLE_FLASHING_PLACED.getHolder().get()))));
        COMMON_FLASHING_TREES = CONFG_FEATURES.register(COMMON_RANDOM_FLASHING_ID, () -> new ConfiguredFeature<>(Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(HolderSet.direct(COMMON_FLASHING_PLACED.getHolder().get()))));

        RARE_FLASHING_CONFIGURED = PLACED_FEATURES.register(FINAL_RARE_FLASHING, () -> new PlacedFeature(Holder.direct(RARE_FLASHING_TREES.get()), VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(200))));
        COMMON_FLASHING_CONFIGURED = PLACED_FEATURES.register(FINAL_COMMON_FLASHING, () -> new PlacedFeature(Holder.direct(COMMON_FLASHING_TREES.get()), VegetationPlacements.treePlacement(CountPlacement.of(2), ModItems.FLASHING_SAPLING.get())));
        CLUSTER_FLASHING_CONFIGURED = PLACED_FEATURES.register(FINAL_CLUSTER_FLASHING, () -> new PlacedFeature(Holder.direct(COMMON_FLASHING_TREES.get()), VegetationPlacements.treePlacement(CountPlacement.of(6), ModItems.FLASHING_SAPLING.get())));
        CLUSTER_CASCADING_CONFIGURED = PLACED_FEATURES.register(FINAL_CLUSTER_CASCADING, () -> new PlacedFeature(Holder.direct(WorldEvent.NATURAL_CASCADE_TREE.get()), VegetationPlacements.treePlacement(CountPlacement.of(6), BlockRegistry.CASCADING_SAPLING)));
        CLUSTER_BLAZING_CONFIGURED = PLACED_FEATURES.register(FINAL_CLUSTER_BLAZING, () -> new PlacedFeature(Holder.direct(WorldEvent.NATURAL_BLAZING_TREE.get()), VegetationPlacements.treePlacement(CountPlacement.of(6), BlockRegistry.BLAZING_SAPLING)));
        CLUSTER_FLOURISHING_CONFIGURED = PLACED_FEATURES.register(FINAL_CLUSTER_FLOURISHING, () -> new PlacedFeature(Holder.direct(WorldEvent.NATURAL_FLOURISHING_TREE.get()), VegetationPlacements.treePlacement(CountPlacement.of(6), BlockRegistry.FLOURISHING_SAPLING)));
        CLUSTER_VEXING_CONFIGURED = PLACED_FEATURES.register(FINAL_CLUSTER_VEXING, () -> new PlacedFeature(Holder.direct(WorldEvent.NATURAL_VEXING_TREE.get()), VegetationPlacements.treePlacement(CountPlacement.of(6), BlockRegistry.VEXING_SAPLING)));


    }

    public static Biome flashingArchwoodForest() {
        MobSpawnSettings.Builder spawnBuilder =archwoodSpawns();
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(FLASHING_WEALD_WALKER.get(), 3, 1, 3));

        BiomeGenerationSettings.Builder biomeBuilder = getArchwoodBiomeBuilder(CLUSTER_FLASHING_CONFIGURED.getId());
        return biome(Biome.Precipitation.RAIN, 0.4F, 0.9F, 7978751, 329011, 7978751, 13414701, 13084948, spawnBuilder, biomeBuilder, () -> Musics.createGameMusic(SoundRegistry.ARIA_BIBLIO.get()));
    }

    private static Biome blazingArchwoodForest() {
        MobSpawnSettings.Builder spawnBuilder =archwoodSpawns();
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.ENTITY_BLAZING_WEALD.get(), 3, 1, 1));

        BiomeGenerationSettings.Builder biomeBuilder = getArchwoodBiomeBuilder(CLUSTER_BLAZING_CONFIGURED.getId());
        return biome(Biome.Precipitation.RAIN, 0.9F, 0.4F, 7978751, 329011, 7978751, 16077890, 2210437, spawnBuilder, biomeBuilder, () -> Musics.createGameMusic(SoundRegistry.ARIA_BIBLIO.get()));
    }

    private static Biome cascadingArchwoodForest() {
        MobSpawnSettings.Builder spawnBuilder =archwoodSpawns();
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.ENTITY_CASCADING_WEALD.get(), 3, 1, 1));

        BiomeGenerationSettings.Builder biomeBuilder = getArchwoodBiomeBuilder(CLUSTER_CASCADING_CONFIGURED.getId());
        return biome(Biome.Precipitation.RAIN, 0.7F, 0.9F, 7978751, 329011, 7978751, 1142955, 2210437, spawnBuilder, biomeBuilder, () -> Musics.createGameMusic(SoundRegistry.ARIA_BIBLIO.get()));
    }

    private static Biome flourishArchwoodForest() {
        MobSpawnSettings.Builder spawnBuilder = archwoodSpawns();
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.ENTITY_FLOURISHING_WEALD.get(), 3, 1, 1));

        BiomeGenerationSettings.Builder biomeBuilder = getArchwoodBiomeBuilder(CLUSTER_FLOURISHING_CONFIGURED.getId());
        return biome(Biome.Precipitation.RAIN, 0.7F, 0.8F, 7978751, 329011, 7978751, 1346066, 2210437, spawnBuilder, biomeBuilder, () -> Musics.createGameMusic(SoundRegistry.ARIA_BIBLIO.get()));
    }

    private static MobSpawnSettings.Builder archwoodSpawns() {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.STARBUNCLE_TYPE.get(), 2, 3, 5));
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.ENTITY_DRYGMY.get(), 2, 1, 3));
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.WHIRLISPRIG_TYPE.get(), 2, 1, 3));
        BiomeDefaultFeatures.farmAnimals(spawnBuilder);
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);
        return spawnBuilder;
    }

    @NotNull
    private static BiomeGenerationSettings.Builder getArchwoodBiomeBuilder(ResourceLocation archwoodCluster) {
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder();
        //we need to follow the same order as vanilla biomes for the BiomeDefaultFeatures
        globalOverworldGeneration(biomeBuilder);
        BiomeDefaultFeatures.addMossyStoneBlock(biomeBuilder);
        BiomeDefaultFeatures.addForestFlowers(biomeBuilder);
        BiomeDefaultFeatures.addFerns(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
        BiomeDefaultFeatures.addExtraGold(biomeBuilder);
        DefaultFeatures.softDisks(biomeBuilder);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_PLAINS);

        BiomeDefaultFeatures.addDefaultMushrooms(biomeBuilder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(biomeBuilder);

        biomeBuilder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, BuiltinRegistries.PLACED_FEATURE.getHolderOrThrow(
                ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(ArsNouveau.MODID, "placed_lights"))));
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, BuiltinRegistries.PLACED_FEATURE.getHolderOrThrow(
                ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, archwoodCluster)));
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldEvent.ArtisanalMojangGrassTM);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldEvent.ArtisanalMojangFlowersTM);
        return biomeBuilder;
    }
}
