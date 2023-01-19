package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.common.entity.summon.SummonMk;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class EffectSummonMk extends AbstractEffect {
    //Easter Egg Effect, not available as Glyph
    public static EffectSummonMk INSTANCE = new EffectSummonMk();

    public EffectSummonMk() {
        super("summon_mk", "Summon Emmekiaps");
    }

    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (!canSummon(shooter) || shooter == null)
            return;
        Vec3 hit = rayTraceResult.getLocation();
        int ticks = (int) (20 * (60 + 60 * spellStats.getDurationMultiplier()));
        List<String> name = new ArrayList<>(names);
        Collections.shuffle(name);
        for (int i = 0; i < 3; i++) {
            SummonMk slime = new SummonMk(world);
            slime.ticksLeft = ticks;
            slime.owner = shooter;
            slime.setCustomName(new TextComponent(name.get(i)));
            slime.setPos(hit.x(), hit.y(), hit.z());
            slime.setTarget(shooter.getLastHurtMob());
            slime.setAggressive(true);
            slime.setSize((int) (2 + spellStats.getAmpMultiplier() + spellStats.getDamageModifier()), true);
            summonLivingEntity(rayTraceResult, world, shooter, spellStats, spellContext, slime);
            slime.tryResetGoals();
        }
        applySummoningSickness(shooter, (int) (ticks * 0.80));
    }

    //Sarebbero stati di più se Michela rispondeva in tempo <.<
    static final List<String> names = List.of("Gnappolo", "Odoacre", "Patafrollo", "IvoAvido", "Ubaldo", "Ambrogio", "Mascarpone", "Luppolo");

    @Override
    public int getDefaultManaCost() {
        return 100;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        // SummonEvent captures augments, but no uses of that field were found
        return getSummonAugments();
    }

    @Override
    public String getBookDescription() {
        return "Summons two wolves that will fight with you. Extend Time will increase the amount of time on the summons. Applies Summoning Sickness to the caster, preventing other summoning magic.";
    }

    @Override
    public SpellTier getTier() {
        return SpellTier.ONE;
    }

    @Nonnull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.CONJURATION, SpellSchools.ELEMENTAL_EARTH);
    }
}
