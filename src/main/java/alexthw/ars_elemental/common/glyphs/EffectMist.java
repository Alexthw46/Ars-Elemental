package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.common.entity.spells.EntityMistCloud;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class EffectMist extends ElementalAbstractEffect {

    public static final EffectMist INSTANCE = new EffectMist("mist", "Mist Cloud");

    public EffectMist(String tag, String description) {
        super(tag, description);
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return Set.of(SpellSchools.ELEMENTAL_WATER);
    }

    public void onResolve(HitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolverContext) {
        if (world.isClientSide) return;

        // Duration: Base 5 seconds (100 ticks) + Extend Time (20 ticks per level)
        int duration = (int) (100 + (spellStats.getDurationMultiplier() * 20));

        // Radius (AOE): Base 3 blocks + 1 block per AOE augment
        float radius = (float) (3.0f + spellStats.getAoeMultiplier());

        EntityMistCloud mist = new EntityMistCloud(world, BlockPos.containing(rayTraceResult.getLocation()), duration, radius);
        world.addFreshEntity(mist);
    }

    @Override
    public String getBookDescription() {
        return "Conjures a lingering cloud of mist that obscures vision for anyone inside and cause mobs to lose their target.";
    }

    @Override
    protected int getDefaultManaCost() {
        return 40;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        map.put(AugmentAOE.INSTANCE, "Increases the size of the Mist Cloud.");
        map.put(AugmentExtendTime.INSTANCE, "Extends the lifetime of the Mist Cloud.");
        map.put(AugmentDurationDown.INSTANCE, "Reduces the lifetime of the Mist Cloud.");
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return Set.of(AugmentAOE.INSTANCE, AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE);
    }
}
