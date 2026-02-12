package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.registry.ModPotions;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EffectSlimeFeet extends ElementalAbstractEffect implements IPotionEffect {

    public static final EffectSlimeFeet INSTANCE = new EffectSlimeFeet("slime_feet", "Slime Walk");

    public EffectSlimeFeet(String tag, String description) {
        super(tag, description);
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.ELEMENTAL_WATER, SpellSchools.ABJURATION);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (rayTraceResult.getEntity() instanceof LivingEntity target) {
            applyConfigPotion(target, ModPotions.SLIME_SLIDE, spellStats);
        }
    }

    @Override
    public String getBookDescription() {
        return "Applies the Slippery Feet status, making the target slide as if on ice on any surface.";
    }

    @Override
    protected int getDefaultManaCost() {
        return 20;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return getPotionAugments();
    }

    @Override
    public int getBaseDuration() {
        return POTION_TIME == null ? 10 : POTION_TIME.get();
    }

    @Override
    public int getExtendTimeDuration() {
        return EXTEND_TIME == null ? 10 : EXTEND_TIME.get();
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }
}
