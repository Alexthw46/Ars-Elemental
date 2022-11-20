package alexthw.ars_elemental.common.blocks;

import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SporeBlossomGround extends Block {
    public SporeBlossomGround(Properties properties) {
        super(properties);
    }

    private static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D);

    private static final int ADD_PARTICLE_ATTEMPTS = 7;
    private static final int PARTICLE_XZ_RADIUS = 4;
    private static final int PARTICLE_Y_MAX = 4;

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return !level.getBlockState(pos.below()).isAir() && !level.isWaterAt(pos) && level.getBlockState(pos.below()).isFaceSturdy(level, pos, Direction.UP);
    }

    /**
     * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific direction passed in.
     */
    public BlockState updateShape(BlockState p_154713_, Direction p_154714_, BlockState p_154715_, LevelAccessor p_154716_, BlockPos p_154717_, BlockPos p_154718_) {
        return p_154714_ == Direction.DOWN && !this.canSurvive(p_154713_, p_154716_, p_154717_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_154713_, p_154714_, p_154715_, p_154716_, p_154717_, p_154718_);
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles).
     */
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        double d0 = i + random.nextDouble();
        double d1 = j + 1.0D;
        double d2 = k + random.nextDouble();
        level.addParticle(ParticleTypes.FALLING_SPORE_BLOSSOM, d0, d1, d2, ParticleUtil.inRange(-0.1, 0.1), 0.0D, ParticleUtil.inRange(-0.1, 0.1));
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for (int l = 0; l < ADD_PARTICLE_ATTEMPTS; ++l) {
            mutableBlockPos.set(i + Mth.nextInt(random, -PARTICLE_XZ_RADIUS, PARTICLE_XZ_RADIUS), j + random.nextInt(PARTICLE_Y_MAX), k + Mth.nextInt(random, -PARTICLE_XZ_RADIUS, PARTICLE_XZ_RADIUS));
            BlockState blockstate = level.getBlockState(mutableBlockPos);
            if (!blockstate.isCollisionShapeFullBlock(level, mutableBlockPos)) {
                level.addParticle(ParticleTypes.SPORE_BLOSSOM_AIR, (double) mutableBlockPos.getX() + random.nextDouble(), (double) mutableBlockPos.getY() + random.nextDouble(), (double) mutableBlockPos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    public VoxelShape getShape(BlockState p_154699_, BlockGetter p_154700_, BlockPos p_154701_, CollisionContext p_154702_) {
        return SHAPE;
    }

}
