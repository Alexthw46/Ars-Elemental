package alexthw.ars_elemental.common.blocks.upstream;

import alexthw.ars_elemental.registry.ModTiles;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketANEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

import static alexthw.ars_elemental.ConfigHandler.Common.AIR_ELEVATOR_COST;

public class AirUpstreamTile extends UpstreamTile {

    public AirUpstreamTile(BlockPos pPos, BlockState pBlockState) {
        super(ModTiles.AIR_UPSTREAM_TILE.get(), pPos, pBlockState);
    }

    @Override
    protected int getTickRate() {
        return 20;
    }

    @Override
    protected boolean isValidTarget(LivingEntity entity) {
        return !entity.isSpectator() && !entity.isInWater() && !entity.isInLava();
    }

    @Override
    protected int getSourceCost(int power) {
        return AIR_ELEVATOR_COST.get() * power;
    }

    @Override
    protected void applyEffects(ServerLevel level, LivingEntity entity) {
        entity.resetFallDistance();
        entity.addEffect(new MobEffectInstance((entity.isCrouching() ? MobEffects.SLOW_FALLING : MobEffects.LEVITATION), 50, 1, false, false, false));
        entity.hurtMarked = true;
        elevatorParticles(entity, level);
    }

    @Override
    public void elevatorParticles(Entity e, ServerLevel level) {
        Networking.sendToNearbyClient(level, e, new PacketANEffect(PacketANEffect.EffectType.TIMED_HELIX, e.getOnPos(), ParticleColor.WHITE));
    }

}
