package alexthw.ars_elemental.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.DamageUtil;
import com.hollingsworth.arsnouveau.common.lib.PotionEffectTags;
import com.hollingsworth.arsnouveau.setup.registry.DamageTypesRegistry;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.common.EffectCures;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class EffectCauterize extends ElementalAbstractEffect implements IDamageEffect {

    public static final EffectCauterize INSTANCE = new EffectCauterize("cauterize", "Cauterize");

    public EffectCauterize(String id, String name) {
        super(id, name);
    }

    @Override
    public String getBookDescription() {
        return "Cauterize wounds with fire, dealing a small amount of fire damage to remove harmful effects. Can only remove effects that are curable with milk.";
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (rayTraceResult.getEntity() instanceof LivingEntity entity) {
            // Only works if the damage can land
            if (attemptDamage(world, shooter, spellStats, spellContext, resolver, entity, buildDamageSource(world, shooter), DAMAGE.get().floatValue())) {
                Collection<MobEffectInstance> effects = entity.getActiveEffects();
                MobEffectInstance[] array = effects.toArray(new MobEffectInstance[0]);
                Optional<HolderSet.Named<MobEffect>> blacklist = world.registryAccess().registryOrThrow(Registries.MOB_EFFECT).getTag(PotionEffectTags.DISPEL_DENY);
                Optional<HolderSet.Named<MobEffect>> whitelist = world.registryAccess().registryOrThrow(Registries.MOB_EFFECT).getTag(PotionEffectTags.DISPEL_ALLOW);
                for (MobEffectInstance e : array) {
                    if (e.getEffect().value().getCategory() == MobEffectCategory.HARMFUL && e.getCures().contains(EffectCures.MILK)) {
                        if (blacklist.isPresent() && blacklist.get().stream().anyMatch(effect -> effect.value() == e.getEffect()))
                            continue;
                        entity.removeEffect(e.getEffect());
                    } else if (whitelist.isPresent() && whitelist.get().stream().anyMatch(effect -> effect.value() == e.getEffect())) {
                        entity.removeEffect(e.getEffect());
                    }
                }
            }
        }
    }

    @Override
    public boolean canDamage(LivingEntity shooter, SpellStats stats, SpellContext spellContext, SpellResolver resolver, @NotNull Entity entity) {
        return !(entity instanceof LivingEntity living && living.getHealth() <= 0); // allow self-damage
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addDamageConfig(builder, 3f);
    }

    @Override
    public DamageSource buildDamageSource(Level world, LivingEntity shooter) {
        return DamageUtil.source(world, DamageTypesRegistry.FLARE, shooter);
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return Set.of(SpellSchools.ELEMENTAL_FIRE);
    }

    @Override
    protected int getDefaultManaCost() {
        return 80;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return Set.of();
    }

}
