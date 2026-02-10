package alexthw.ars_elemental.common.mob_effects;

import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class MistEffect extends MobEffect {
    public MistEffect() {
        super(MobEffectCategory.NEUTRAL, new ParticleColor(255, 255, 0).getColor());
    }

}
