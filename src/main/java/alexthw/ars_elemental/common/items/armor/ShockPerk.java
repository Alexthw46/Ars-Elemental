package alexthw.ars_elemental.common.items.armor;

import com.hollingsworth.arsnouveau.api.perk.IEffectResolvePerk;
import com.hollingsworth.arsnouveau.api.perk.Perk;
import com.hollingsworth.arsnouveau.api.perk.PerkInstance;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import static alexthw.ars_elemental.ArsElemental.prefix;
import static alexthw.ars_elemental.registry.ModPotions.LIGHTNING_LURE;

public class ShockPerk extends Perk implements IEffectResolvePerk {

    public static ShockPerk INSTANCE = new ShockPerk();

    public ShockPerk() {
        super(prefix("thread_shock"));
    }

    @Override
    public String getLangDescription() {
        return "Damaging effects cause the target to be shocked for a short duration before the effect resolves.";
    }

    @Override
    public String getLangName() {
        return "Shocking";
    }

    @Override
    public void onPreResolve(HitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver, AbstractEffect effect, PerkInstance perkInstance) {
        // If the effect is a damage effect, the raytrace result is a living entity, and the entity hit is not the shooter, then apply shocked or lightning lure.
        if (effect instanceof IDamageEffect damageEffect && rayTraceResult instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof LivingEntity livingEntity && livingEntity != shooter) {
            if (damageEffect.canDamage(shooter, spellStats, spellContext, resolver, entityHitResult.getEntity())) {
                int value = perkInstance.getSlot().value;
                livingEntity.addEffect(new MobEffectInstance(value < 3 ? ModPotions.SHOCKED_EFFECT.get() : LIGHTNING_LURE.get(), value * 5 * 20, value < 3 ? value : 1));
            }
        }
    }

    @Override
    public void onPostResolve(HitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver, AbstractEffect effect, PerkInstance perkInstance) {
    }
}
