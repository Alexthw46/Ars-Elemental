package alexthw.ars_elemental.common.blocks.relays;

import com.hollingsworth.arsnouveau.common.block.RelayWarpBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AirRelay extends RelayWarpBlock {

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AirWarperRelayTile(pos, state);
    }
}
