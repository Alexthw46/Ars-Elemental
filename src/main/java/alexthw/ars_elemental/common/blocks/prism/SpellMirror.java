package alexthw.ars_elemental.common.blocks.prism;

import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.common.advancement.ANCriteriaTriggers;
import com.hollingsworth.arsnouveau.common.block.SpellPrismBlock;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SpellMirror extends SpellPrismBlock {

    public SpellMirror(Properties properties) {
        super(properties.noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }


    @Override
    public void onHit(ServerLevel world, BlockPos pos, EntityProjectileSpell spell) {
        Direction direction = world.getBlockState(pos).getValue(FACING);
        float factor = -0.9F;
        spell.prismRedirect++;
        if (spell.prismRedirect >= 3) {
            ANCriteriaTriggers.rewardNearbyPlayers(ANCriteriaTriggers.PRISMATIC, world, pos, 10);
        }
        if (spell.spellResolver == null) {
            spell.remove(Entity.RemovalReason.DISCARDED);
            return;
        }
        switch (direction) {
            case UP, DOWN -> {
                Vec3 vel = spell.getDeltaMovement();
                spell.setDeltaMovement(vel.x(), factor * vel.y(), vel.z());
            }
            case EAST, WEST -> {
                Vec3 vel = spell.getDeltaMovement();
                spell.setDeltaMovement(factor * vel.x(), vel.y(), vel.z());
            }
            case NORTH, SOUTH -> {
                Vec3 vel = spell.getDeltaMovement();
                spell.setDeltaMovement(vel.x(), vel.y(), factor * vel.z());
            }
        }
        BlockUtil.updateObservers(world, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = pState.getValue(FACING);
        return switch (direction) {
            case EAST -> east;
            case WEST -> west;
            case SOUTH -> south;
            case NORTH -> base;
            case DOWN -> ceiling;
            case UP -> floor;
        };
    }

    static final VoxelShape base = Block.box(0, 0, 14, 16, 16, 16);
    static final VoxelShape south = Block.box(0, 0, 0, 16, 16, 2);
    static final VoxelShape floor = Block.box(0, 0, 0, 16, 2, 16);
    static final VoxelShape ceiling = Block.box(0, 14, 0, 16, 16, 16);
    static final VoxelShape east = Block.box(0, 0, 0, 2, 16, 16);
    static final VoxelShape west = Block.box(14, 0, 0, 16, 16, 16);

}
