package alexthw.ars_elemental.world;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.util.SupplierBlockStateProviderAE;
import com.hollingsworth.arsnouveau.common.world.tree.MagicTrunkPlacer;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

import static alexthw.ars_elemental.ArsElemental.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WorldEvents {
    public static Holder<ConfiguredFeature<TreeConfiguration, ?>> FLASHING_TREE = FeatureUtils.register("ars_elemental:flashing_feature", Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
            new SupplierBlockStateProviderAE("yellow_archwood_log"),
            new MagicTrunkPlacer(10, 2, 0),
            new SupplierBlockStateProviderAE("yellow_archwood_leaves"),
            new BlobFoliagePlacer(UniformInt.of(0, 0), UniformInt.of(0, 0), 0),
            new TwoLayersFeatureSize(2, 0, 2)).ignoreVines().build());

    public static Holder<PlacedFeature> PLACED_FLASHING;
    public static Holder<PlacedFeature> PLACED_FLASHING_CONFIGURED;
    public static Holder<ConfiguredFeature<RandomFeatureConfiguration, ?>> ARCHWOOD_TREES;

    public static ResourceLocation RARE_FLASHING_RL = ArsElemental.prefix("archwood");
    public static ResourceLocation COMMON_FLASHING_RL = ArsElemental.prefix("common_archwood");

    @SubscribeEvent
    public static void registerFeature(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PLACED_FLASHING = PlacementUtils.register("ars_elemental:placed_flashing", FLASHING_TREE, PlacementUtils.filteredByBlockSurvival(ModItems.FLASHING_SAPLING.get()));
            ARCHWOOD_TREES = FeatureUtils.register("ars_elemental:random_flashing", Feature.RANDOM_SELECTOR,
                    new RandomFeatureConfiguration(List.of(
                            new WeightedPlacedFeature(PLACED_FLASHING, 0.25f)), PLACED_FLASHING));
            PLACED_FLASHING_CONFIGURED = PlacementUtils.register(RARE_FLASHING_RL.toString(), ARCHWOOD_TREES, VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(200)));
            PLACED_FLASHING_CONFIGURED = PlacementUtils.register(COMMON_FLASHING_RL.toString(), ARCHWOOD_TREES, VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(10)));
        });
    }

}
