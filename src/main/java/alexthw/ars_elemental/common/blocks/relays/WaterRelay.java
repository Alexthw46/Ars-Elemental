package alexthw.ars_elemental.common.blocks.relays;

import com.hollingsworth.arsnouveau.common.block.RelaySplitter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WaterRelay extends RelaySplitter {

    public WaterRelay() {
        super();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WaterSplitterRelayTile(pos, state);
    }

}
