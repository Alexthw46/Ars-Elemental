package alexthw.ars_elemental.common.rituals.forest;

import com.hollingsworth.arsnouveau.api.ritual.FeaturePlacementRitual;
import com.hollingsworth.arsnouveau.api.ritual.features.PlaceBlockFeature;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.block.tile.LightTile;
import com.hollingsworth.arsnouveau.common.block.tile.RitualBrazierTile;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PlaceableLightFeature extends PlaceBlockFeature {

    public PlaceableLightFeature(double distance, double chance) {
        super(distance, chance, BlockRegistry.LIGHT_BLOCK.get()::defaultBlockState);
    }

    @Override
    public boolean onPlace(Level level, BlockPos pos, FeaturePlacementRitual placementRitual, RitualBrazierTile brazierTile) {
        BlockState state = block.get();
        RandomSource random = level.random;
        if (random.nextFloat() < chance && level.getBlockState(pos).isAir()) {
            level.setBlockAndUpdate(pos, state);
            if (level.getBlockEntity(pos) instanceof LightTile tile) {
                tile.color = new ParticleColor(
                        Math.max(10, random.nextInt(255)),
                        Math.max(10, random.nextInt(255)),
                        Math.max(10, random.nextInt(255))
                );
            }
            return true;
        }
        return false;
    }

}
