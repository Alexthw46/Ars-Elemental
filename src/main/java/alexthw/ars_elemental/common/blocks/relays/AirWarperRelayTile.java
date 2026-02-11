package alexthw.ars_elemental.common.blocks.relays;

import alexthw.ars_elemental.registry.ModTiles;
import com.hollingsworth.arsnouveau.api.source.ISourceCap;
import com.hollingsworth.arsnouveau.common.block.tile.RelayWarpTile;
import com.hollingsworth.arsnouveau.common.capability.SourceStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AirWarperRelayTile extends RelayWarpTile {
    public AirWarperRelayTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public AirWarperRelayTile(BlockPos pos, BlockState state) {
        super(ModTiles.ADVANCED_WARP_RELAY.get(), pos, state);
    }

    @Override
    protected @NotNull SourceStorage createDefaultStorage() {
        return new SourceStorage(3000, 3000, 3000, 0) {
            public void onContentsChanged() {
                AirWarperRelayTile.this.updateBlock();
            }
        };
    }

    @Override
    public int transferSource(ISourceCap from, ISourceCap to) {
        return transferSource(from, to, from.getMaxExtract());
    }

}
