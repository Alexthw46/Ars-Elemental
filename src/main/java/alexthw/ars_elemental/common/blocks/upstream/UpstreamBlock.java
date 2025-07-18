package alexthw.ars_elemental.common.blocks.upstream;

import com.hollingsworth.arsnouveau.common.block.TickableModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class UpstreamBlock extends TickableModBlock {

    public UpstreamBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, isMoving);
        if (!world.isClientSide() && world.getBlockEntity(pos) instanceof UpstreamTile upstreamTile) {
            upstreamTile.disabled = world.hasNeighborSignal(pos);
            upstreamTile.updateBlock();
        }
    }


}
