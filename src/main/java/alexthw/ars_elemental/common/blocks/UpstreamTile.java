package alexthw.ars_elemental.common.blocks;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.block.ITickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class UpstreamTile extends BlockEntity implements ITickable {

    public UpstreamTile(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModEntities.UPSTREAM_TILE, pWorldPosition, pBlockState);
    }

    @Override
    public void tick() {
        if (this.level instanceof ServerLevel level && level.getGameTime() % 5 == 0){
            List<Entity> entityList = level.getEntitiesOfClass(Entity.class, new AABB(getBlockPos(), getBlockPos().above(16)).inflate(1.5), e -> e.isInWater() && !e.isCrouching());
            for (Entity e : entityList){
                Vec3 vec3 = e.getDeltaMovement();
                double d0 = Math.max(0.4D, vec3.y + 0.1D);
                e.setDeltaMovement(vec3.x, d0, vec3.z);
                e.resetFallDistance();
                for (int i = 0; i < 3; i++) {
                    spawnBubbles(e, level);
                }
                e.hurtMarked = true;
            }
        }
    }

    public void spawnBubbles(Entity e, ServerLevel level) {

        double d0 = e.getX();
        double d1 = e.getY();
        double d2 = e.getZ();

        level.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, d0 + ParticleUtil.inRange(-0.5D, 0.5), d1+1, d2 + ParticleUtil.inRange(-0.5D, 0.5), 2,0.0D, 0.0D, 0.0D,0.5f);

    }

}
