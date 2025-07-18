package alexthw.ars_elemental.common.blocks.upstream;

import com.hollingsworth.arsnouveau.api.source.ISpecialSourceProvider;
import com.hollingsworth.arsnouveau.api.util.SourceUtil;
import com.hollingsworth.arsnouveau.common.block.ITickable;
import com.hollingsworth.arsnouveau.common.block.tile.ModdedTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class UpstreamTile extends ModdedTile implements ITickable {

    public boolean disabled;

    public UpstreamTile(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public abstract void elevatorParticles(Entity e, ServerLevel level);

    @Override
    public void tick() {
        if (!(this.level instanceof ServerLevel serverLevel) || this.disabled) return;
        if (serverLevel.getGameTime() % getTickRate() != 0) return;

        BlockPos pos = getBlockPos();
        if (serverLevel.getBlockState(pos.above()) == this.getBlockState()) return;

        int power = 1;
        while (serverLevel.getBlockState(pos.below(power)).getBlock() == this.getBlockState().getBlock()) {
            power++;
        }

        AABB area = new AABB(pos.getCenter(), pos.above(46 * power).getCenter()).inflate(1.5);
        List<LivingEntity> entities = serverLevel.getEntitiesOfClass(
                LivingEntity.class,
                area,
                this::isValidTarget
        );

        int sourceCost = getSourceCost(power);
        if (!entities.isEmpty() && sourceCost > 0) {
            var source = SourceUtil.takeSourceMultiple(pos, serverLevel, 10, sourceCost);
            if (source == null || source.isEmpty() || !source.stream().allMatch(ISpecialSourceProvider::isValid))
                return;
        }

        for (LivingEntity entity : entities) {
            applyEffects(serverLevel, entity);
        }
    }

    protected int getTickRate() {
        return 2;
    }

    protected abstract boolean isValidTarget(LivingEntity entity);

    protected abstract int getSourceCost(int power);

    protected abstract void applyEffects(ServerLevel level, LivingEntity entity);

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        this.disabled = tag.getBoolean("disabled");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("disabled", this.disabled);
    }
}
