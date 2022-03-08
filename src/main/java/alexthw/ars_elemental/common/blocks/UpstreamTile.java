package alexthw.ars_elemental.common.blocks;

import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.common.block.ITickable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class UpstreamTile extends BlockEntity implements ITickable {

    public UpstreamTile(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModRegistry.UPSTREAM_TILE, pWorldPosition, pBlockState);
    }

    @Override
    public void tick() {
        if (level instanceof ServerLevel && level.getGameTime() % 5 == 0){
            List<Entity> entityList = level.getEntitiesOfClass(Entity.class, new AABB(getBlockPos(), getBlockPos().above(16)).inflate(1.0), Entity::isInWater);
            for (Entity e : entityList){
                Vec3 vec3 = e.getDeltaMovement();
                double d0 = Math.max(0.5D, vec3.y + 0.05D);
                e.setDeltaMovement(vec3.x, d0, vec3.z);
                e.resetFallDistance();
                e.hurtMarked = true;
            }
        }
    }

}
