package alexthw.ars_elemental.common.blocks.relays;

import com.hollingsworth.arsnouveau.common.block.RelayDepositBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EarthRelay extends RelayDepositBlock {

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EarthDepositorRelayTile(pos, state);
    }
}
