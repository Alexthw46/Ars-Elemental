package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.api.item.ISchoolFocus;
import alexthw.ars_elemental.common.blocks.ElementalSpellTurretTile;
import alexthw.ars_elemental.registry.ModAdvTriggers;
import alexthw.ars_elemental.registry.ModItems;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

import static com.hollingsworth.arsnouveau.api.spell.SpellSchools.ELEMENTAL_EARTH;

public class EffectSpores extends ElementalAbstractEffect implements IDamageEffect, IPotionEffect {

    public static EffectSpores INSTANCE = new EffectSpores();

    public EffectSpores() {
        super("poison_spores", "Spores");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level level, @Nonnull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (!(rayTraceResult.getEntity() instanceof LivingEntity livingEntity && level instanceof ServerLevel world))
            return;
        Vec3 vec = safelyGetHitPos(rayTraceResult);
        float damage = (float) (DAMAGE.get() + AMP_VALUE.get() * spellStats.getAmpMultiplier());
        double range = 3 + spellStats.getAoeMultiplier();
        int effectSec = (int) (POTION_TIME.get() + EXTEND_TIME.get() * spellStats.getDurationMultiplier());

        if (!canDamage(shooter, spellStats, spellContext, resolver, livingEntity)) return;

        damage(vec, world, shooter, spellStats, damage, effectSec, livingEntity, spellContext, resolver);

        for (LivingEntity e : world.getEntitiesOfClass(LivingEntity.class, new AABB(livingEntity.position().add(range, range, range), livingEntity.position().subtract(range, range, range)))) {
            if (e.equals(livingEntity) || e.equals(shooter))
                continue;
            if (canDamage(e, spellStats, spellContext, resolver, e)) {
                vec = e.position();
                damage(vec, world, shooter, spellStats, damage, effectSec, e, spellContext, resolver);
            } else {
                e.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * effectSec, (int) spellStats.getAmpMultiplier()));
            }
        }
    }

    @Override
    public boolean canDamage(LivingEntity shooter, SpellStats stats, SpellContext spellContext, SpellResolver resolver, Entity entity) {
        return !(entity instanceof LivingEntity living && living.getHealth() <= 0 && (living.hasEffect(MobEffects.POISON) || living.hasEffect(MobEffects.HUNGER)));
    }

    public void damage(Vec3 vec, ServerLevel world, @Nonnull LivingEntity shooter, SpellStats stats, float damage, int snareTime, LivingEntity livingEntity, SpellContext spellContext, SpellResolver resolver) {
        EntityDamageSource damageSource = new EntityDamageSource("poison", shooter);
        damageSource.setMagic();
        attemptDamage(world, shooter, stats, spellContext, resolver, livingEntity, damageSource, damage);
        world.sendParticles(ParticleTypes.SPORE_BLOSSOM_AIR, vec.x, vec.y + 0.5, vec.z, 50,
                ParticleUtil.inRange(-0.1, 0.1), ParticleUtil.inRange(-0.1, 0.1), ParticleUtil.inRange(-0.1, 0.1), 0.5);
        if (livingEntity.isDeadOrDying() && world.getRandom().nextInt(100) < 5 && (spellContext.castingTile instanceof ElementalSpellTurretTile turret ? turret.getSchool() : ISchoolFocus.hasFocus(world, shooter)) == ELEMENTAL_EARTH) {
            BlockPos feet = livingEntity.getOnPos();
            Material underfoot = world.getBlockState(feet).getMaterial();
            Block blossom = ModItems.GROUND_BLOSSOM.get();
            if ((underfoot == Material.DIRT || underfoot == Material.GRASS || underfoot == Material.MOSS || underfoot == Material.LEAVES) && world.getBlockState(feet.above()).isAir()) {
                world.setBlockAndUpdate(feet.above(), ModItems.GROUND_BLOSSOM.get().defaultBlockState());
                if (shooter instanceof ServerPlayer serverPlayer && !(serverPlayer instanceof FakePlayer)) ModAdvTriggers.BLOSSOM.trigger(serverPlayer);
            }
        } else livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20 * snareTime));

    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addDamageConfig(builder, 6.0);
        addAmpConfig(builder, 2.5);
        addPotionConfig(builder, 10);
        addExtendTimeConfig(builder, 3);

    }

    @Override
    protected Map<ResourceLocation, Integer> getDefaultAugmentLimits(Map<ResourceLocation, Integer> defaults) {
        super.getDefaultAugmentLimits(defaults);
        defaults.put(AugmentAmplify.INSTANCE.getRegistryName(), 2);
        return defaults;
    }

    @Override
    public int getDefaultManaCost() {
        return 30;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
                AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE,
                AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE,
                AugmentAOE.INSTANCE, AugmentFortune.INSTANCE
        );
    }

    @Override
    public SpellTier getTier() {
        return SpellTier.TWO;
    }

    @Nonnull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(ELEMENTAL_EARTH);
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
