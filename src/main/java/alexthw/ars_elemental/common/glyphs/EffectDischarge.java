package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.network.RayEffectPacket;
import com.hollingsworth.arsnouveau.api.ANFakePlayer;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.potions.ModPotions;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static alexthw.ars_elemental.registry.ModPotions.LIGHTNING_LURE;

public class EffectDischarge extends ElementalAbstractEffect implements IDamageEffect, IPotionEffect {

    public static EffectDischarge INSTANCE = new EffectDischarge();

    public EffectDischarge() {
        super("discharge", "Discharge");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (!(rayTraceResult.getEntity() instanceof LivingEntity livingEntity) || !(world instanceof ServerLevel level))
            return;
        Vec3 vec = safelyGetHitPos(rayTraceResult);
        float damage = (float) (DAMAGE.get() + AMP_VALUE.get() * spellStats.getAmpMultiplier());
        double range = 3 + spellStats.getAoeMultiplier();
        DamageSource source = buildDamageSource(world, shooter);

        // If the target is shocked, damage all nearby entities and apply the shock effect to them
        if (livingEntity.hasEffect(ModPotions.SHOCKED_EFFECT.get()) || livingEntity.hasEffect(LIGHTNING_LURE.get())) {
            // If the target is static charged, damage is increased by 30% and the effect is removed
            if (livingEntity.hasEffect(LIGHTNING_LURE.get())) {
                damage *= 1.3;
                livingEntity.removeEffect(LIGHTNING_LURE.get());
            }
            // If the target wear energy armors, damage is increased by 10% for each armor piece and energy is drained
            for (ItemStack i : livingEntity.getArmorSlots()) {
                LazyOptional<IEnergyStorage> lazyEnergyStorage = i.getCapability(ForgeCapabilities.ENERGY);
                if (lazyEnergyStorage.isPresent()) {
                    Optional<IEnergyStorage> energyStorage = lazyEnergyStorage.resolve();
                    if (energyStorage.isPresent()) {
                        energyStorage.get().extractEnergy((int) (energyStorage.get().getEnergyStored() * 0.25), false);
                        damage *= 1.1;
                    }
                }
            }
            // Damage all nearby entities and apply the shock effect to them
            for (LivingEntity entity : world.getEntitiesOfClass(LivingEntity.class, new AABB(livingEntity.blockPosition()).inflate(range), (e) -> !e.equals(shooter))) {
                attemptDamage(world, shooter, spellStats, spellContext, resolver, entity, source, damage);
                ((IPotionEffect) this).applyConfigPotion(entity, ModPotions.SHOCKED_EFFECT.get(), spellStats);
                RayEffectPacket.send(world, new ParticleColor(225, 200, 50), livingEntity.position(), entity.position());
            }
        }
    }

    @SuppressWarnings("removal")
    @Override
    public void applyPotion(LivingEntity entity, MobEffect potionEffect, SpellStats stats, int baseDurationSeconds, int durationBuffSeconds, boolean showParticles) {
        if (entity == null) return;
        int ticks = baseDurationSeconds * 20 + durationBuffSeconds * stats.getDurationInTicks();
        int amp = (int) Math.min(stats.getAmpMultiplier(), 5);
        entity.addEffect(new MobEffectInstance(potionEffect, ticks, amp, false, showParticles, true));
    }

    @Override
    public DamageSource buildDamageSource(Level world, LivingEntity shooter) {
        shooter = !(shooter instanceof Player) ? ANFakePlayer.getPlayer((ServerLevel) world) : shooter;
        return new EntityDamageSource(DamageSource.LIGHTNING_BOLT.getMsgId(), shooter);
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addDamageConfig(builder, 7.0);
        addAmpConfig(builder, 3.0);
    }

    @Override
    public int getDefaultManaCost() {
        return 40;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Override
    protected Map<ResourceLocation, Integer> getDefaultAugmentLimits(Map<ResourceLocation, Integer> defaults) {
        super.getDefaultAugmentLimits(defaults);
        defaults.put(AugmentAmplify.INSTANCE.getRegistryName(), 2);
        return defaults;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
                AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE,
                AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE,
                AugmentAOE.INSTANCE,
                AugmentFortune.INSTANCE
        );
    }

    @NotNull
    @Override
    protected Set<SpellSchool> getSchools() {
        return Set.of(SpellSchools.ELEMENTAL_AIR);
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
