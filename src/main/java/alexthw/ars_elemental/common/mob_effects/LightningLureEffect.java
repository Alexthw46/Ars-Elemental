package alexthw.ars_elemental.common.mob_effects;

import alexthw.ars_elemental.registry.ModParticles;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class LightningLureEffect extends ParticleMobEffect {
    public LightningLureEffect() {
        super(MobEffectCategory.HARMFUL, new ParticleColor(255, 255, 0).getColor());
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        super.applyEffectTick(living, amplifier);
        MobEffectInstance effect = living.getEffect(this);
        if (effect != null && effect.getDuration() == 1) {
            fallLightning(living);
        }
    }

    @Override
    public ParticleOptions getParticle() {
        return ModParticles.SPARK.get();
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        // Trigger the effect only once, when the time left reaches 1.
        return true;
    }

    public static void fallLightning(LivingEntity pLivingEntity) {
        // Spawn a lightning bolt at the entity's position when the effect triggers.
        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(pLivingEntity.level());
        if (lightningbolt == null) return;
        lightningbolt.moveTo(Vec3.atBottomCenterOf(pLivingEntity.blockPosition()));
        lightningbolt.setCause(pLivingEntity instanceof ServerPlayer sp ? sp : null);
        pLivingEntity.level.addFreshEntity(lightningbolt);
    }

}
