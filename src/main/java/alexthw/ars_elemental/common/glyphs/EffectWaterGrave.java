package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.mixin.ZombieInvoker;
import alexthw.ars_elemental.registry.ModPotions;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class EffectWaterGrave extends ElementalAbstractEffect implements IDamageEffect, IPotionEffect {

    public static EffectWaterGrave INSTANCE = new EffectWaterGrave();

    private EffectWaterGrave() {
        super("watery_grave", "Watery Grave");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (rayTraceResult.getEntity() instanceof LivingEntity living) {
            if (spellStats.hasBuff(AugmentExtendTime.INSTANCE)) {
                ((IPotionEffect) this).applyConfigPotion(living, ModPotions.WATER_GRAVE.get(), spellStats);
            } else {
                Vec3 delta = living.getDeltaMovement();
                double dy = Math.min(-1.0D, delta.y - 0.05D);
                living.setDeltaMovement(delta.x, dy, delta.z);
            }
            if (living instanceof Zombie zombie && !(living instanceof Drowned)) {
                ((ZombieInvoker) zombie).callStartUnderWaterConversion(20);
                return;
            }
            int airSupply = living.getAirSupply();
            if (airSupply <= 0 || living.getMobType() == MobType.WATER) {
                double damage = DAMAGE.get() + AMP_VALUE.get() * spellStats.getAmpMultiplier();
                attemptDamage(world, shooter, spellStats, spellContext, resolver, living, new EntityDamageSource(DamageSource.DROWN.getMsgId(), shooter), (float) damage);
            } else {
                double newSupply = Math.max(-19, airSupply - 50 * (3 + spellStats.getAmpMultiplier()));
                living.setAirSupply((int) newSupply);
            }
            living.hurtMarked = true;
        }
    }

    @Override
    public int getDefaultManaCost() {
        return 25;
    }

    @Override
    public SpellTier getTier() {
        return SpellTier.TWO;
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addPotionConfig(builder, 30);
        addDamageConfig(builder, 5.0);
        addAmpConfig(builder, 2.0);
        addExtendTimeConfig(builder, 5);
    }

    @Override
    protected Map<ResourceLocation, Integer> getDefaultAugmentLimits(Map<ResourceLocation, Integer> defaults) {
        super.getDefaultAugmentLimits(defaults);
        defaults.put(AugmentAmplify.INSTANCE.getRegistryName(), 2);
        return defaults;
    }

    /**
     * Returns the set of augments that this spell part can be enhanced by.
     */
    @NotNull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return getPotionAugments();
    }

    @Override
    public String getBookDescription() {
        return "Causes entities to drown. When augmented with Extend Time, they will be dragged down and unable to swim up.";
    }

    @Override
    public int getBaseDuration() {
        return POTION_TIME == null ? 30 : POTION_TIME.get();
    }

    @Override
    public int getExtendTimeDuration() {
        return EXTEND_TIME == null ? 8 : EXTEND_TIME.get();
    }

}
