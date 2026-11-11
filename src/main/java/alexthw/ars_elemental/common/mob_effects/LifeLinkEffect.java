package alexthw.ars_elemental.common.mob_effects;

import alexthw.ars_elemental.common.glyphs.EffectLifeLink;
import alexthw.ars_elemental.registry.ModPotions;
import com.alexthw.sauce.util.EntityCarryMEI;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class LifeLinkEffect extends MobEffect {

    public LifeLinkEffect() {
        super(MobEffectCategory.NEUTRAL, 1);
        NeoForge.EVENT_BUS.addListener(this::healForHeal);
        NeoForge.EVENT_BUS.addListener(this::hurtForHurt);
    }

    @Override
    public void fillEffectCures(@NotNull Set<EffectCure> cures, @NotNull MobEffectInstance effectInstance) {
    }

    public void healForHeal(LivingHealEvent event) {
        if (event.getEntity().hasEffect(ModPotions.LIFE_LINK)) {
            MobEffectInstance instance = event.getEntity().getEffect(ModPotions.LIFE_LINK);
            if (instance instanceof EntityCarryMEI mei && mei.getTarget() == event.getEntity()) {
                if (mei.getOwner() != null && mei.getOwner().isAlive()) {
                    mei.getOwner().heal(event.getAmount() * EffectLifeLink.INSTANCE.SHARED_HEAL.get().floatValue());
                    event.setAmount(event.getAmount() * (1 - EffectLifeLink.INSTANCE.SHARED_HEAL.get().floatValue()));
                } else {
                    event.getEntity().removeEffect(ModPotions.LIFE_LINK);
                }
            }
        }
    }

    public void hurtForHurt(LivingDamageEvent.Pre event) {
        if (!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) && event.getEntity().hasEffect(ModPotions.LIFE_LINK)) {
            MobEffectInstance instance = event.getEntity().getEffect(ModPotions.LIFE_LINK);
            if (instance instanceof EntityCarryMEI mei && mei.getOwner() == event.getEntity()) {
                if (mei.getTarget() != null && mei.getTarget().isAlive()) {
                    mei.getTarget().hurt(event.getSource(), event.getNewDamage() * EffectLifeLink.INSTANCE.SHARED_DAMAGE.get().floatValue());
                    event.setNewDamage(event.getNewDamage() * (1 - EffectLifeLink.INSTANCE.SHARED_DAMAGE.get().floatValue()));
                } else {
                    event.getEntity().removeEffect(ModPotions.LIFE_LINK);
                }
            }
        }
    }

}
