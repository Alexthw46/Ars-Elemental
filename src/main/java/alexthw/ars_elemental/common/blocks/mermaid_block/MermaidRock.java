package alexthw.ars_elemental.common.blocks.mermaid_block;

import com.hollingsworth.arsnouveau.common.block.SummonBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static com.hollingsworth.arsnouveau.common.block.tile.SummoningTile.CONVERTED;

public class MermaidRock extends SummonBlock {

    public static VoxelShape shape;

    public MermaidRock(String string) {
        super(string);
    }


    public MermaidRock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(CONVERTED, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MermaidTile(pPos, pState);
    }

    public static VoxelShape getShape() {
        return shape;
    }

}
