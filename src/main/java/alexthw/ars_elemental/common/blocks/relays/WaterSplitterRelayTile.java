package alexthw.ars_elemental.common.blocks.relays;

import alexthw.ars_elemental.registry.ModTiles;
import com.hollingsworth.arsnouveau.common.block.tile.RelaySplitterTile;
import com.hollingsworth.arsnouveau.common.capability.SourceStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WaterSplitterRelayTile extends RelaySplitterTile {

    public WaterSplitterRelayTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public WaterSplitterRelayTile(BlockPos pos, BlockState state) {
        super(ModTiles.ADVANCED_SPLITTER_RELAY.get(), pos, state);
    }

    protected @NotNull SourceStorage createDefaultStorage() {
        return new SourceStorage(3500, 3500, 3500, 0) {
            public void onContentsChanged() {
                WaterSplitterRelayTile.this.updateBlock();
            }
        };
    }
}
