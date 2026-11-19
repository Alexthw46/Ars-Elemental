package alexthw.ars_elemental.common.rituals.forest;

import alexthw.ars_elemental.common.rituals.forest.ArchwoodVariantsRegistry.ArchwoodVariant;
import com.hollingsworth.arsnouveau.api.ritual.FeaturePlacementRitual;
import com.hollingsworth.arsnouveau.api.ritual.features.BonemealFeature;
import com.hollingsworth.arsnouveau.api.ritual.features.IPlaceableFeature;
import com.hollingsworth.arsnouveau.api.ritual.features.RandomTreeFeature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class ArchwoodForestationRitual extends FeaturePlacementRitual {

    public static final String ID = "ritual_archwood_forestation";

    private Item selectedSapling;

    @Override
    public void addFeatures(List<IPlaceableFeature> features) {

        features.add(new BonemealFeature(6, 0.4));

        ArchwoodVariant selected =
                ArchwoodVariantsRegistry.get(selectedSapling);

        if (selected != null) {
            features.add(selected.treeFeature().get());
            features.add(selected.plantFeature().get());
            features.add(selected.podFeature().get());
        } else {
            // Generate all registered Archwood variants.
            List<BlockState> saplings = new ArrayList<>();

            for (ArchwoodVariant variant :
                    ArchwoodVariantsRegistry.getAll()) {

                saplings.add(((BlockItem) variant.sapling()).getBlock()
                        .defaultBlockState());
            }

            if (!saplings.isEmpty()) {
                features.add(new RandomTreeFeature(saplings, 8, 0.8));
            }

            for (ArchwoodVariant variant :
                    ArchwoodVariantsRegistry.getAll()) {

                features.add(variant.plantFeature().get());
                features.add(variant.podFeature().get());
            }
        }

        features.add(new PlaceableLightFeature(4, 0.1));
    }

    @Override
    public String getLangName() {
        return "Forestation - Archwood";
    }

    @Override
    public String getLangDescription() {
        return "Places grown Archwood Trees, and applies bonemeal in a 7x7 (circular) area. Augmenting with a source gem will increase the radius by 1 for each gem. Augmenting with a specific archwood sapling will spawn resources tied to its biome.";
    }

    @Override
    public ResourceLocation getRegistryName() {
        return prefix(ID);
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {

        ArchwoodVariant variant =
                ArchwoodVariantsRegistry.get(stack.getItem());

        if (variant != null) {
            selectedSapling = stack.getItem();
            return true;
        }

        selectedSapling = null;

        return super.canConsumeItem(stack);
    }

}
