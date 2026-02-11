package alexthw.ars_elemental.common.blocks.relays;

import alexthw.ars_elemental.registry.ModTiles;
import com.hollingsworth.arsnouveau.common.block.tile.RelayDepositTile;
import com.hollingsworth.arsnouveau.common.capability.SourceStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EarthDepositorRelayTile extends RelayDepositTile {

    public EarthDepositorRelayTile(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return ModTiles.ADVANCED_DEPOSITOR_RELAY.get();
    }

    protected @NotNull SourceStorage createDefaultStorage() {
        return new SourceStorage(5000, 5000, 2500, 0) {
            public void onContentsChanged() {
                EarthDepositorRelayTile.this.updateBlock();
            }
        };
    }

}
