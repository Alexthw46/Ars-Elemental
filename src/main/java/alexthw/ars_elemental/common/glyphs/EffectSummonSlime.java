package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.common.entity.summon.SummonSlime;
import alexthw.ars_elemental.common.entity.summon.SummonSlime.Variant;
import alexthw.ars_elemental.util.CompatUtils;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSplit;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public class EffectSummonSlime extends ElementalAbstractEffect {

    public static final EffectSummonSlime INSTANCE = new EffectSummonSlime("summon_slime", "Summon Slime");

    public EffectSummonSlime(String tag, String description) {
        super(tag, description);
    }

    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (!canSummon(shooter))
            return;
        Vec3 hit = rayTraceResult.getLocation();
        int ticks = (int) (20 * (60 + 60 * spellStats.getDurationMultiplier()));
        Variant variant = getVariant(resolver);
        for (int i = 0; i < 3 + spellStats.getBuffCount(AugmentSplit.INSTANCE); i++) {
            SummonSlime slime = new SummonSlime(world);
            slime.ticksLeft = ticks;
            slime.owner = shooter;
            slime.setPos(hit.x(), hit.y(), hit.z());
            slime.setSize((int) (2 + spellStats.getAmpMultiplier()), true);
            summonLivingEntity(rayTraceResult, world, shooter, spellStats, spellContext, resolver, slime);
            slime.tryResetGoals();
            slime.setTarget(shooter.getLastHurtMob());
            slime.setAggressive(true);
            slime.setVariant(variant);
        }
        if (!CompatUtils.isSummonRework())
            applySummoningSickness(shooter, (int) (ticks * 0.80));
    }

    private Variant getVariant(SpellResolver resolver) {
        // if caster == MK return Variant.MK;
        if (CompatUtils.fireCheck(resolver)) return Variant.FIRE;
        if (CompatUtils.waterCheck(resolver)) return Variant.WATER;
        if (CompatUtils.earthCheck(resolver)) return Variant.EARTH;
        if (CompatUtils.airCheck(resolver)) return Variant.AIR;
        if (resolver.hasFocus(ItemsRegistry.SHAPERS_FOCUS.get())) return Variant.MANIPULATION;

        return Variant.SUMMON;
    }

    @Override
    public int getDefaultManaCost() {
        return 100;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE, AugmentSplit.INSTANCE);
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        addSummonAugmentDescriptions(map);
        map.put(AugmentSplit.INSTANCE, "Increase the number of summoned slimes.");
    }

    @Override
    public String getBookDescription() {
        return "Summons three slimes that will fight with you. Extend Time will increase the amount of time on the summons. Applies Summoning Sickness to the caster, preventing other summoning magic.";
    }

    @Nonnull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.CONJURATION, SpellSchools.ELEMENTAL_WATER);
    }

}
