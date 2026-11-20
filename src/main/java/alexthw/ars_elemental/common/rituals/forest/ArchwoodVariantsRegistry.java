package alexthw.ars_elemental.common.rituals.forest;

import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.world.ModWorldgen;
import com.hollingsworth.arsnouveau.api.ritual.features.IPlaceableFeature;
import com.hollingsworth.arsnouveau.api.ritual.features.PlaceBlockFeature;
import com.hollingsworth.arsnouveau.api.ritual.features.RandomTreeFeature;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class ArchwoodVariantsRegistry {

    private static final Map<Item, ArchwoodVariant> VARIANTS_BY_SAPLING =
            new LinkedHashMap<>();
    private static final Map<Item, ArchwoodVariant> VARIANTS_BY_POD =
            new LinkedHashMap<>();

    public static void register(ArchwoodVariant variant) {
        Objects.requireNonNull(variant);

        if (VARIANTS_BY_SAPLING.putIfAbsent(variant.sapling(), variant) != null) {
            throw new IllegalArgumentException(
                    "An Archwood Forestation variant is already registered for: "
                            + variant.sapling()
            );
        }
        if (variant.pod != null && variant.biome.get() != null) {
            VARIANTS_BY_POD.putIfAbsent(variant.pod(), variant);
        }
    }

    public static ArchwoodVariant get(Item sapling) {
        return VARIANTS_BY_SAPLING.get(sapling);
    }

    public static ArchwoodVariant getByPod(Item pod) {
        return VARIANTS_BY_POD.get(pod);
    }

    public static Collection<ArchwoodVariant> getAll() {
        return Collections.unmodifiableCollection(VARIANTS_BY_SAPLING.values());
    }

    public static void init() {

        register(new ArchwoodVariant(
                BlockRegistry.BLAZING_SAPLING.asItem(),
                BlockRegistry.BOMBEGRANTE_POD.asItem(),
                () -> new RandomTreeFeature(
                        List.of(BlockRegistry.BLAZING_SAPLING.defaultBlockState()),
                        8, 0.8
                ),
                () -> new PlaceBlockFeature(
                        2, 0.1, Blocks.TORCHFLOWER::defaultBlockState
                ),
                () -> new ArchpodFeature(
                        1, 0.15, BlockRegistry.BOMBEGRANTE_POD.defaultBlockState()
                ),
                () -> ModWorldgen.Biomes.BLAZING_FOREST_KEY
        ));

        register(new ArchwoodVariant(
                BlockRegistry.CASCADING_SAPLING.asItem(),
                BlockRegistry.FROSTAYA_POD.asItem(),

                () -> new RandomTreeFeature(
                        List.of(BlockRegistry.CASCADING_SAPLING.defaultBlockState()),
                        8, 0.8
                ),
                () -> new PlaceBlockFeature(
                        2, 0.1, Blocks.PITCHER_PLANT::defaultBlockState
                ),
                () -> new ArchpodFeature(
                        1, 0.15, BlockRegistry.FROSTAYA_POD.defaultBlockState()
                ),
                () -> ModWorldgen.Biomes.CASCADING_FOREST_KEY
        ));

        register(new ArchwoodVariant(
                BlockRegistry.VEXING_SAPLING.asItem(),
                BlockRegistry.BASTION_POD.asItem(),
                () -> new RandomTreeFeature(
                        List.of(BlockRegistry.VEXING_SAPLING.defaultBlockState()),
                        8, 0.8
                ),
                () -> new PlaceBlockFeature(
                        2, 0.1, BlockRegistry.SOURCEBERRY_BUSH.get()::defaultBlockState
                ),
                () -> new ArchpodFeature(
                        1, 0.15, BlockRegistry.BASTION_POD.defaultBlockState()
                ),
                () -> ModWorldgen.Biomes.VEXING_CAVES_KEY
        ));

        register(new ArchwoodVariant(
                BlockRegistry.FLOURISHING_SAPLING.asItem(),
                BlockRegistry.MENDOSTEEN_POD.asItem(),
                () -> new RandomTreeFeature(
                        List.of(BlockRegistry.FLOURISHING_SAPLING.defaultBlockState()),
                        8, 0.8
                ),
                () -> new PlaceBlockFeature(
                        2, 0.1, ModItems.GROUND_BLOSSOM.get()::defaultBlockState
                ),
                () -> new ArchpodFeature(
                        1, 0.15, BlockRegistry.MENDOSTEEN_POD.defaultBlockState()
                ),
                () -> ModWorldgen.Biomes.FLOURISHING_FOREST_KEY
        ));

        register(new ArchwoodVariant(
                ModItems.FLASHING_SAPLING.get().asItem(),
                ModItems.FLASHING_POD.get().asItem(),
                () -> new RandomTreeFeature(
                        List.of(ModItems.FLASHING_SAPLING.get().defaultBlockState()),
                        8, 0.8
                ),
                () -> new PlaceBlockFeature(
                        2, 0.1, ModItems.SPARKFLOWER.get()::defaultBlockState
                ),
                () -> new ArchpodFeature(
                        1, 0.15, ModItems.FLASHING_POD.get().defaultBlockState()
                ),
                () -> ModWorldgen.Biomes.FLASHING_FOREST_KEY
        ));
    }

    public record ArchwoodVariant(
            Item sapling,
            @Nullable Item pod,
            Supplier<IPlaceableFeature> treeFeature,
            Supplier<IPlaceableFeature> plantFeature,
            Supplier<IPlaceableFeature> podFeature,
            Supplier<ResourceKey<Biome>> biome
    ) {
        public ArchwoodVariant(Item sapling,
                               Supplier<IPlaceableFeature> treeFeature,
                               Supplier<IPlaceableFeature> plantFeature,
                               Supplier<IPlaceableFeature> podFeature) {
            this(sapling, null, treeFeature, plantFeature, podFeature, () -> null);
        }
    }
}