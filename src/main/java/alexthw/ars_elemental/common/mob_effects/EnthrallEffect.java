package alexthw.ars_elemental.common.mob_effects;

import com.alexthw.sauce.common.entity.EnthrallUtil;
import com.alexthw.sauce.util.EntityCarryMEI;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static alexthw.ars_elemental.registry.ModPotions.ENTHRALLED;
import static com.alexthw.sauce.common.entity.EnthrallUtil.THRALL_KEY;

public class EnthrallEffect extends MobEffect {

    public EnthrallEffect() {
        super(MobEffectCategory.NEUTRAL, 0);
        NeoForge.EVENT_BUS.addListener(this::onRemove);
    }

    private void onRemove(MobEffectEvent.Remove event) {
        event.getEntity().getPersistentData().remove(THRALL_KEY);
    }

    @Override
    public void onEffectAdded(@NotNull LivingEntity livingEntity, int amplifier) {
        super.onEffectAdded(livingEntity, amplifier);
        if (livingEntity.getEffect(ENTHRALLED) instanceof EntityCarryMEI mei) {
            EnthrallUtil.permanentEnthrall(mei.getOwner(), livingEntity);
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        // Trigger the effect only once, when the time left reaches 1.
        return pDuration == 1;
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        pLivingEntity.getPersistentData().remove(THRALL_KEY);
        return true;
    }


    @Override
    public void fillEffectCures(@NotNull Set<EffectCure> cures, @NotNull MobEffectInstance effectInstance) {

    }

}
