package alexthw.ars_elemental.common.blocks.upstream;

import alexthw.ars_elemental.registry.ModTiles;
import com.hollingsworth.arsnouveau.common.block.ITickable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class AirUpstreamTile extends BlockEntity implements ITickable {

    public AirUpstreamTile(BlockPos pPos, BlockState pBlockState) {
        super(ModTiles.AIR_UPSTREAM_TILE.get(), pPos, pBlockState);
    }

    @Override
    public void tick() {
        if (this.level instanceof ServerLevel serverLevel && serverLevel.getGameTime() % 20 == 0) {
            List<LivingEntity> entityList = serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(getBlockPos(), getBlockPos().above(46)).inflate(1.1), e -> !e.isInWater() && !e.isInLava());
            for (LivingEntity e : entityList) {
                Vec3 vec3 = e.getDeltaMovement();
                e.resetFallDistance();
                e.addEffect(new MobEffectInstance((e.isCrouching() ? MobEffects.SLOW_FALLING : MobEffects.LEVITATION), 25, 1, false, false, false));
                e.hurtMarked = true;
            }
        }
    }

}
