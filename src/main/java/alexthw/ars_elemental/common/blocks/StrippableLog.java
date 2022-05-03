package alexthw.ars_elemental.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class StrippableLog extends RotatedPillarBlock {
    Supplier<Block> strippedState;
    public StrippableLog(Properties properties, Supplier<Block> stateSupplier) {
        super(properties);
        this.strippedState = stateSupplier;
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, ToolAction toolAction) {
        return toolAction == ToolActions.AXE_STRIP ? strippedState.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)) : null;
    }
}