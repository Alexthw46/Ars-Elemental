package alexthw.ars_elemental.world;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.util.SupplierBlockStateProviderAE;
import com.hollingsworth.arsnouveau.common.world.tree.MagicTrunkPlacer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

import static alexthw.ars_elemental.ArsElemental.MODID;
import static alexthw.ars_elemental.ArsElemental.prefix;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WorldRegistry {
    public static Holder<ConfiguredFeature<TreeConfiguration, ?>> FLASHING_TREE = FeatureUtils.register("ars_elemental:flashing_feature", Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
            new SupplierBlockStateProviderAE("yellow_archwood_log"),
            new MagicTrunkPlacer(10, 1, 0, false, "ars_elemental:flashpine_pod"),
            new SupplierBlockStateProviderAE("yellow_archwood_leaves"),
            new BlobFoliagePlacer(UniformInt.of(0, 0), UniformInt.of(0, 0), 0),
            new TwoLayersFeatureSize(2, 0, 2)).ignoreVines().build());

    public static Holder<ConfiguredFeature<TreeConfiguration, ?>> NATURAL_FLASHING_TREE = FeatureUtils.register("ars_elemental:natural_flashing_feature", Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
            new SupplierBlockStateProviderAE("yellow_archwood_log"),
            new MagicTrunkPlacer(10, 2, 0, true, "ars_elemental:flashpine_pod"),
            new SupplierBlockStateProviderAE("yellow_archwood_leaves"),
            new BlobFoliagePlacer(UniformInt.of(0, 0), UniformInt.of(0, 0), 0),
            new TwoLayersFeatureSize(2, 0, 2)).build());

    public static Holder<PlacedFeature> PLACED_FLASHING;
    public static Holder<PlacedFeature> COMMON_FLASHING;
    public static Holder<PlacedFeature> RARE_FLASHING_CONFIGURED;
    public static Holder<PlacedFeature> COMMON_FLASHING_CONFIGURED;

    public static Holder<ConfiguredFeature<SimpleRandomFeatureConfiguration, ?>> ARCHWOOD_TREES;
    public static Holder<ConfiguredFeature<SimpleRandomFeatureConfiguration, ?>> COMMON_ARCHWOOD_TREES;

    public static ResourceLocation SIMPLE_FLASHING_RL = prefix("placed_flashing");
    public static ResourceLocation RARE_FLASHING_RL = ArsElemental.prefix("archwood");
    public static ResourceLocation COMMON_FLASHING_RL = ArsElemental.prefix("common_flashing");
    public static ResourceLocation COMMON_FLASHING_PLACED_RL = ArsElemental.prefix("common_archwood");

    @SubscribeEvent
    public static void registerFeature(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PLACED_FLASHING = PlacementUtils.register(SIMPLE_FLASHING_RL.toString(), NATURAL_FLASHING_TREE, PlacementUtils.filteredByBlockSurvival(ModItems.FLASHING_SAPLING.get()));
            COMMON_FLASHING = PlacementUtils.register(COMMON_FLASHING_RL.toString(), NATURAL_FLASHING_TREE, List.of(PlacementUtils.countExtra(5, 0.01F, 1), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, PlacementUtils.filteredByBlockSurvival(ModItems.FLASHING_SAPLING.get())));

            ARCHWOOD_TREES = FeatureUtils.register("ars_elemental:random_flashing", Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(HolderSet.direct(PLACED_FLASHING)));
            COMMON_ARCHWOOD_TREES = FeatureUtils.register("ars_elemental:random_common_archwood", Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(HolderSet.direct(COMMON_FLASHING)));

            RARE_FLASHING_CONFIGURED = PlacementUtils.register(RARE_FLASHING_RL.toString(), ARCHWOOD_TREES, VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(200)));
            COMMON_FLASHING_CONFIGURED = PlacementUtils.register(COMMON_FLASHING_PLACED_RL.toString(), COMMON_ARCHWOOD_TREES, VegetationPlacements.treePlacement(CountPlacement.of(2), ModItems.FLASHING_SAPLING.get()));
        });
    }

}
