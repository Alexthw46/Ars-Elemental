package alexthw.ars_elemental.common.blocks.relays;

import alexthw.ars_elemental.registry.ModTiles;
import com.hollingsworth.arsnouveau.common.block.tile.RelayCollectorTile;
import com.hollingsworth.arsnouveau.common.capability.SourceStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FireCollectorRelayTile extends RelayCollectorTile {
    public FireCollectorRelayTile(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return ModTiles.ADVANCED_COLLECTOR_RELAY.get();
    }

    protected @NotNull SourceStorage createDefaultStorage() {
        return new SourceStorage(4000, 4000, 3500, 0) {
            public void onContentsChanged() {
                FireCollectorRelayTile.this.updateBlock();
            }
        };
    }

}
