package alexthw.ars_elemental.common.blocks.upstream;

import com.hollingsworth.arsnouveau.common.block.TickableModBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class UpstreamBlock extends TickableModBlock {

    public UpstreamBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new WaterUpstreamTile(pPos, pState);
    }
}
