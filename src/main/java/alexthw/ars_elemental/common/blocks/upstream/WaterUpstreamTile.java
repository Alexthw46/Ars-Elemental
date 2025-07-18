package alexthw.ars_elemental.common.blocks.upstream;

import alexthw.ars_elemental.registry.ModTiles;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static alexthw.ars_elemental.ConfigHandler.Common.WATER_ELEVATOR_COST;

public class WaterUpstreamTile extends UpstreamTile {

    public WaterUpstreamTile(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModTiles.WATER_UPSTREAM_TILE.get(), pWorldPosition, pBlockState);
    }

    @Override
    protected boolean isValidTarget(LivingEntity entity) {
        return !entity.isSpectator() && entity.isInWater() && !entity.isCrouching();
    }

    @Override
    protected int getSourceCost(int power) {
        return WATER_ELEVATOR_COST.get() * power;
    }

    @Override
    protected void applyEffects(ServerLevel level, LivingEntity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        entity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 100));
        double d0 = Math.max(0.4D, vec3.y + 0.1D);
        entity.setDeltaMovement(vec3.x, d0, vec3.z);
        entity.resetFallDistance();
        for (int i = 0; i < 3; i++) {
            elevatorParticles(entity, level);
        }
        entity.hurtMarked = true;
    }

    public void elevatorParticles(Entity e, ServerLevel level) {

        double d0 = e.getX();
        double d1 = e.getY();
        double d2 = e.getZ();

        level.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, d0 + ParticleUtil.inRange(-0.5D, 0.5), d1 + 1, d2 + ParticleUtil.inRange(-0.5D, 0.5), 2, 0.0D, 0.0D, 0.0D, 0.5f);

    }

}
