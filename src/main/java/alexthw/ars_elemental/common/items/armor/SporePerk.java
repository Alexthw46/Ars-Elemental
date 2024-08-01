package alexthw.ars_elemental.common.items.armor;

import com.hollingsworth.arsnouveau.api.event.EffectResolveEvent;
import com.hollingsworth.arsnouveau.api.perk.IEffectResolvePerk;
import com.hollingsworth.arsnouveau.api.perk.Perk;
import com.hollingsworth.arsnouveau.api.perk.PerkInstance;
import com.hollingsworth.arsnouveau.api.spell.IDamageEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class SporePerk extends Perk implements IEffectResolvePerk {

    public static SporePerk INSTANCE = new SporePerk();

    public SporePerk() {
        super(prefix("thread_spore"));
    }

    @Override
    public String getLangDescription() {
        return "Damaging effects cause the target to be poisoned or hungry for a short duration before the effect resolves.";
    }

    @Override
    public String getLangName() {
        return "Spores";
    }

    @Override
    public void onEffectPreResolve(EffectResolveEvent.Pre event, PerkInstance perkInstance) {
        //if the effect is a damage effect, the raytrace result is a living entity, and the living entity is not the shooter, then apply poison or hunger
        if (event.resolveEffect instanceof IDamageEffect damageEffect && event.rayTraceResult instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof LivingEntity livingEntity && livingEntity != event.shooter) {
            if (damageEffect.canDamage(event.shooter, event.spellStats, event.context, event.resolver, entityHitResult.getEntity())) {
                livingEntity.addEffect(new MobEffectInstance(livingEntity.isInvertedHealAndHarm() ? MobEffects.HUNGER : MobEffects.POISON, perkInstance.getSlot().value() * 5 * 20, perkInstance.getSlot().value() - 1));
            }
        }
    }


}
