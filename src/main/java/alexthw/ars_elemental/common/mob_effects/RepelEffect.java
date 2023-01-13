package alexthw.ars_elemental.common.mob_effects;

import alexthw.ars_elemental.util.PosCarryMEI;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class RepelEffect extends MobEffect {
    public RepelEffect() {
        super(MobEffectCategory.NEUTRAL, new ParticleColor(255, 255, 0).getColor());
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int pAmplifier) {
        if (entity.getEffect(this) instanceof PosCarryMEI mei) {
            Vec3 vec3d = new Vec3(entity.getX() - mei.getOrigin().getX(), entity.getY() - mei.getOrigin().getY(), entity.getZ() - mei.getOrigin().getZ());
            if (vec3d.length() > 15) return;
            entity.setDeltaMovement(entity.getDeltaMovement().add(vec3d.normalize()).scale(0.5F));
            entity.hurtMarked = true;
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
