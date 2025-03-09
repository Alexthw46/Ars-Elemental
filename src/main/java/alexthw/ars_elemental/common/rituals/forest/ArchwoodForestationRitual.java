package alexthw.ars_elemental.common.rituals.forest;

import alexthw.ars_elemental.registry.ModItems;
import com.hollingsworth.arsnouveau.api.ritual.FeaturePlacementRitual;
import com.hollingsworth.arsnouveau.api.ritual.features.BonemealFeature;
import com.hollingsworth.arsnouveau.api.ritual.features.IPlaceableFeature;
import com.hollingsworth.arsnouveau.api.ritual.features.PlaceBlockFeature;
import com.hollingsworth.arsnouveau.api.ritual.features.RandomTreeFeature;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;

import java.util.List;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class ArchwoodForestationRitual extends FeaturePlacementRitual {

    public static final String ID = "ritual_archwood_forestation";
    int biomeType = -1;

    @Override
    public void addFeatures(List<IPlaceableFeature> features) {
        features.add(new BonemealFeature(6, 0.8));
        switch (biomeType) {
            case 0 -> {
                features.add(new RandomTreeFeature(List.of(BlockRegistry.BLAZING_SAPLING.defaultBlockState()), 8, 0.8));
                features.add(new PlaceBlockFeature(2, 0.1, Blocks.TORCHFLOWER::defaultBlockState));
                features.add(new ArchpodFeature(0.5, 0.15, BlockRegistry.BOMBEGRANTE_POD.defaultBlockState()));
            }
            case 1 -> {
                features.add(new RandomTreeFeature(List.of(BlockRegistry.CASCADING_SAPLING.defaultBlockState()), 8, 0.8));
                features.add(new PlaceBlockFeature(2, 0.1, Blocks.PITCHER_PLANT::defaultBlockState));
                features.add(new ArchpodFeature(0.5, 0.15, BlockRegistry.FROSTAYA_POD.defaultBlockState()));
            }
            case 2 -> {
                features.add(new RandomTreeFeature(List.of(BlockRegistry.VEXING_SAPLING.defaultBlockState()), 8, 0.8));
                features.add(new PlaceBlockFeature(2, 0.1, BlockRegistry.SOURCEBERRY_BUSH.get()::defaultBlockState));
                features.add(new ArchpodFeature(0.5, 0.15, BlockRegistry.BASTION_POD.defaultBlockState()));
            }
            case 3 -> {
                features.add(new RandomTreeFeature(List.of(BlockRegistry.FLOURISHING_SAPLING.defaultBlockState()), 8, 0.8));
                features.add(new PlaceBlockFeature(2, 0.1, ModItems.GROUND_BLOSSOM.get()::defaultBlockState));
                features.add(new ArchpodFeature(0.5, 0.15, BlockRegistry.MENDOSTEEN_POD.defaultBlockState()));
            }
            case 4 -> {
                features.add(new RandomTreeFeature(List.of(ModItems.FLASHING_SAPLING.get().defaultBlockState()), 8, 0.8));
                features.add(new PlaceBlockFeature(2, 0.1, ModItems.SPARKFLOWER.get()::defaultBlockState));
                features.add(new ArchpodFeature(0.5, 0.15, ModItems.FLASHING_POD.get().defaultBlockState()));
            }
            default -> {
                // all the above
                features.add(new RandomTreeFeature(List.of(BlockRegistry.BLAZING_SAPLING.defaultBlockState(),
                        BlockRegistry.CASCADING_SAPLING.defaultBlockState(),
                        BlockRegistry.VEXING_SAPLING.defaultBlockState(),
                        BlockRegistry.FLOURISHING_SAPLING.defaultBlockState(),
                        ModItems.FLASHING_SAPLING.get().defaultBlockState()
                ), 8, 0.8));
                features.add(new PlaceBlockFeature(2, 0.1, BlockRegistry.SOURCEBERRY_BUSH.get()::defaultBlockState));
                features.add(new ArchpodFeature(0.5, 0.15, BlockRegistry.BOMBEGRANTE_POD.defaultBlockState()));
                features.add(new ArchpodFeature(0.5, 0.15, BlockRegistry.FROSTAYA_POD.defaultBlockState()));
                features.add(new ArchpodFeature(0.5, 0.15, BlockRegistry.BASTION_POD.defaultBlockState()));
                features.add(new ArchpodFeature(0.5, 0.15, BlockRegistry.MENDOSTEEN_POD.defaultBlockState()));
                features.add(new ArchpodFeature(0.5, 0.15, ModItems.FLASHING_POD.get().defaultBlockState()));
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
        if (biomeType != -1) {
            return super.canConsumeItem(stack);
        }
        boolean blazing = stack.getItem() == BlockRegistry.BLAZING_SAPLING.asItem();
        boolean cascading = stack.getItem() == BlockRegistry.CASCADING_SAPLING.asItem();
        boolean flourishing = stack.getItem() == BlockRegistry.FLOURISHING_SAPLING.asItem();
        boolean vexing = stack.getItem() == BlockRegistry.VEXING_SAPLING.asItem();
        boolean flashing = stack.getItem() == ModItems.FLASHING_SAPLING.get().asItem();

        boolean isVariant = blazing || cascading || flourishing || vexing || flashing;

        if (isVariant) {
            if (blazing) biomeType = 0;
            if (cascading) biomeType = 1;
            if (vexing) biomeType = 2;
            if (flourishing) biomeType = 3;
            if (flashing) biomeType = 4;
        } else {
            biomeType = -1;
        }

        return super.canConsumeItem(stack) || (isVariant && stack.getItem() instanceof BlockItem bi && bi.getBlock() instanceof SaplingBlock);
    }

}
