package alexthw.ars_elemental.common.mob_effects;

import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class LightningLureEffect extends MobEffect {
    public LightningLureEffect() {
        super(MobEffectCategory.HARMFUL, new ParticleColor(255, 255, 0).getColor());
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(pLivingEntity.level);
        if (lightningbolt == null) return;
        lightningbolt.moveTo(Vec3.atBottomCenterOf(pLivingEntity.blockPosition()));
        lightningbolt.setCause(pLivingEntity instanceof ServerPlayer sp ? sp : null);
        pLivingEntity.level.addFreshEntity(lightningbolt);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration == 1;
    }


}
