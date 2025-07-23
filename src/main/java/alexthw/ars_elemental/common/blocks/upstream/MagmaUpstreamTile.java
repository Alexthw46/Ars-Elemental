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

import static alexthw.ars_elemental.ConfigHandler.Common.LAVA_ELEVATOR_COST;

public class MagmaUpstreamTile extends UpstreamTile {
    public MagmaUpstreamTile(BlockPos pPos, BlockState pBlockState) {
        super(ModTiles.LAVA_UPSTREAM_TILE.get(), pPos, pBlockState);
    }

    @Override
    protected boolean isValidTarget(LivingEntity entity) {
        return !entity.isSpectator() && entity.isInLava() && !entity.isCrouching();
    }

    @Override
    protected int getSourceCost(int power) {
        return LAVA_ELEVATOR_COST.get() * power;
    }

    @Override
    protected void applyEffects(ServerLevel level, LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100));
        Vec3 vec3 = entity.getDeltaMovement();
        entity.setDeltaMovement(vec3.x, 0.4, vec3.z);
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

        level.sendParticles(ParticleTypes.LANDING_LAVA, d0 + ParticleUtil.inRange(-0.5D, 0.5), d1 + 1, d2 + ParticleUtil.inRange(-0.5D, 0.5), 2, 0.0D, 0.0D, 0.0D, 0.5f);

    }
}
