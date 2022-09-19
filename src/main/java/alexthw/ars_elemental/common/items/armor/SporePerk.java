package alexthw.ars_elemental.common.items.armor;

import alexthw.ars_elemental.ArsElemental;
import com.hollingsworth.arsnouveau.api.perk.IEffectResolvePerk;
import com.hollingsworth.arsnouveau.api.perk.Perk;
import com.hollingsworth.arsnouveau.api.perk.PerkInstance;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class SporePerk extends Perk implements IEffectResolvePerk {

    public static SporePerk INSTANCE = new SporePerk();

    public SporePerk() {
        super(new ResourceLocation(ArsElemental.MODID, "thread_spore"));
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
    public void onPreResolve(HitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver, AbstractEffect effect, PerkInstance perkInstance) {
        if (effect instanceof IDamageEffect damageEffect && rayTraceResult instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof LivingEntity livingEntity && livingEntity != shooter) {
            if (damageEffect.canDamage(shooter, spellStats, spellContext, resolver, entityHitResult.getEntity())) {
                livingEntity.addEffect(new MobEffectInstance(livingEntity.isInvertedHealAndHarm() ? MobEffects.HUNGER : MobEffects.POISON, perkInstance.getSlot().value * 5 * 20, perkInstance.getSlot().value >= 3 ? 2 : 1));
            }
        }
    }

    @Override
    public void onPostResolve(HitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver, AbstractEffect effect, PerkInstance perkInstance) {
    }

}
