package alexthw.ars_elemental.common.glyphs;

import com.alexthw.sauce.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EffectRage extends ElementalAbstractEffect implements IPotionEffect {

    public static final EffectRage INSTANCE = new EffectRage("rage", "Rage");

    public EffectRage(String tag, String description) {
        super(tag, description);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (rayTraceResult.getEntity() instanceof Mob target) {
            applyConfigPotion(target, ModRegistry.RAGE, spellStats);
        }
    }

    @Override
    protected int getDefaultManaCost() {
        return 100;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return getPotionAugments();
    }

    @Override
    public String getBookDescription() {
        return "Fills the target with rage, causing them to attack nearby entities, even allied, and deal more damage.";
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return Set.of(SpellSchools.ELEMENTAL_FIRE, SpellSchools.NECROMANCY);
    }

    @Override
    public int getBaseDuration() {
        return 60;
    }

    @Override
    public int getExtendTimeDuration() {
        return 120;
    }
}
