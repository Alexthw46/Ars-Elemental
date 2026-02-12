package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.common.entity.summon.SummonBee;
import alexthw.ars_elemental.util.CompatUtils;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSplit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public class EffectSummonBee extends ElementalAbstractEffect {

    public static final EffectSummonBee INSTANCE = new EffectSummonBee("summon_bee", "Summon Bee");

    public EffectSummonBee(String tag, String description) {
        super(tag, description);
    }

    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (!canSummon(shooter))
            return;
        Vec3 hit = rayTraceResult.getLocation();
        int ticks = (int) (20 * (60 + 60 * spellStats.getDurationMultiplier()));
        for (int i = 0; i < 3 + spellStats.getBuffCount(AugmentSplit.INSTANCE); i++) {
            SummonBee slime = new SummonBee(world);
            slime.ticksLeft = ticks;
            slime.owner = shooter;
            slime.setPos(hit.x(), hit.y(), hit.z());
            summonLivingEntity(rayTraceResult, world, shooter, spellStats, spellContext, resolver, slime);
            slime.setTarget(shooter.getLastHurtMob());
            slime.setAggressive(true);
        }
        if (!CompatUtils.isSummonRework())
            applySummoningSickness(shooter, (int) (ticks * 0.80));

    }

    @Override
    protected int getDefaultManaCost() {
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
        map.put(AugmentSplit.INSTANCE, "Increase the number of summoned bees");
    }

    @Override
    public String getBookDescription() {
        return "Summons three bees that will fight with you. Extend Time will increase the amount of time on the summons. Applies Summoning Sickness to the caster, preventing other summoning magic.";
    }

    @Nonnull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.CONJURATION, SpellSchools.ELEMENTAL_EARTH);
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }
}
