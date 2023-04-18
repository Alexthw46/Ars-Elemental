package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.ArsNouveauRegistry;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentFortune;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class EffectPhantom extends ElementalAbstractEffect implements IDamageEffect {

    public static EffectPhantom INSTANCE = new EffectPhantom();

    public EffectPhantom() {
        super("phantom_grasp", "Phantom Grasp");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (rayTraceResult.getEntity() instanceof LivingEntity entity) {
            if (entity.isRemoved() || entity.getHealth() <= 0)
                return;

            float healVal = (float) (GENERIC_DOUBLE.get() + AMP_VALUE.get() * spellStats.getAmpMultiplier());
            // If the entity is undead, heal it
            if (entity.isInvertedHealAndHarm()) {
                entity.heal(healVal);
            } else {
                // Otherwise, deal damage
                attemptDamage(world, shooter, spellStats, spellContext, resolver, entity, buildDamageSource(world, shooter).setMagic(), healVal);
                // And consume saturation
                if (entity instanceof Player player) {
                    player.causeFoodExhaustion((float) (2.5 * (1 + spellStats.getAmpMultiplier())));
                }
            }

        }
    }

    @Override
    protected void addDefaultAugmentLimits(Map<ResourceLocation, Integer> defaults) {
        defaults.put(AugmentAmplify.INSTANCE.getRegistryName(), 3);
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addGenericDouble(builder, 3.0, "Base heal amount", "base_heal");
        addAmpConfig(builder, 3.0);
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return setOf(ArsNouveauRegistry.NECROMANCY);
    }

    @Override
    public boolean canDamage(LivingEntity shooter, SpellStats stats, SpellContext spellContext, SpellResolver resolver, Entity entity) {
        return IDamageEffect.super.canDamage(shooter, stats, spellContext, resolver, entity) && !(entity instanceof LivingEntity living && living.isInvertedHealAndHarm());
    }

    @Override
    public int getDefaultManaCost() {
        return 50;
    }

    @Override
    public String getBookDescription() {
        return "Heals a small amount of health to undead. When used on living beings, the spell will deal an equal amount of magic damage and consume hunger.";
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE, AugmentFortune.INSTANCE);
    }
}
