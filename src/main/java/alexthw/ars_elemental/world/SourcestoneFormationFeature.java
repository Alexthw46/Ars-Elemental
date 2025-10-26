package alexthw.ars_elemental.world;

import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.common.block.SourceLamp;
import com.hollingsworth.arsnouveau.common.lib.LibBlockNames;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.common.Tags;

public class SourcestoneFormationFeature extends Feature<NoneFeatureConfiguration> {
    public SourcestoneFormationFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    private boolean isValidGround(BlockState state) {
        return state.is(BlockTags.DIRT) || state.is(Blocks.DEEPSLATE) || state.is(Tags.Blocks.STONES);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        LevelAccessor level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        BlockState baseSourcestone = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, LibBlockNames.SOURCESTONE)).defaultBlockState();
        BlockState var1Sourcestone = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, LibBlockNames.SOURCESTONE_ALTERNATING)).defaultBlockState();
        BlockState var2Sourcestone = BlockRegistry.SOURCE_LAMP.defaultBlockState().setValue(SourceLamp.LIT, true).setValue(SourceLamp.LIGHT_LEVEL, 8);

        // Search for valid ground
        BlockPos blockpos = origin;
        while (blockpos.getY() > level.getMinBuildHeight() + 3) {
            BlockState stateBelow = level.getBlockState(blockpos.below());
            if (!level.isEmptyBlock(blockpos.below()) && isValidGround(stateBelow)) {
                break;
            }
            blockpos = blockpos.below();
        }

        // Abort if no valid ground found
        if (blockpos.getY() <= level.getMinBuildHeight() + 3) {
            return false;
        }

        // drop down so we don't make it floating on a border
        blockpos = blockpos.below();

        // Decide spike properties
        int height = random.nextInt(12) + 6; // Height between 6 and 18
        int baseRadius = random.nextInt(3) + 2; // Base radius of 2 or 5 blocks
        int top = height * 9 / 10; // Top 1 of the spike is lamp

        // Generate the spike
        for (int y = 0; y < height; y++) {
            int radius = baseRadius - (y / (height / baseRadius));

            if (radius < 0 || y >= height - 2) radius = 0; // Ensure radius 0 is minimum (only the center block)

            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (dx * dx + dz * dz <= radius * radius) { // Inside circle
                        BlockPos pos = blockpos.offset(dx, y, dz);

                        // Choose material based on height
                        if (y >= top) {
                            level.setBlock(pos, var2Sourcestone, 2);
                        } else {
                            BlockState whiteRock = random.nextBoolean() ? var1Sourcestone : baseSourcestone;
                            level.setBlock(pos, whiteRock, 2);
                        }
                    }
                }
            }
        }
        return true;
    }
}