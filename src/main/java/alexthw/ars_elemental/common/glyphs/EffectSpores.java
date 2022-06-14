package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.common.blocks.ElementalSpellTurretTile;
import alexthw.ars_elemental.common.items.ISchoolFocus;
import com.hollingsworth.arsnouveau.api.ANFakePlayer;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

import static com.hollingsworth.arsnouveau.api.spell.SpellSchools.ELEMENTAL_EARTH;

public class EffectSpores extends AbstractEffect {

    public static EffectSpores INSTANCE = new EffectSpores();

    public EffectSpores() {
        super("poison_spores", "Spores");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level level, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext) {
        if (!(rayTraceResult.getEntity() instanceof LivingEntity livingEntity && level instanceof ServerLevel world))
            return;
        Vec3 vec = safelyGetHitPos(rayTraceResult);
        float damage = (float) (DAMAGE.get() + AMP_VALUE.get() * spellStats.getAmpMultiplier());
        double range = 3 + spellStats.getAoeMultiplier();
        int effectSec = (int) (POTION_TIME.get() + EXTEND_TIME.get() * spellStats.getDurationMultiplier());

        SpellSchool focus = spellContext.castingTile instanceof ElementalSpellTurretTile turret ? turret.getSchool() : ISchoolFocus.hasFocus(world, shooter);

        if (focus == ELEMENTAL_EARTH)
            livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20 * effectSec));

        if (!canDamage(livingEntity)) return;

        damage(vec, world, shooter, spellStats, damage, effectSec, livingEntity);

        for (LivingEntity e : world.getEntitiesOfClass(LivingEntity.class, new AABB(livingEntity.position().add(range, range, range), livingEntity.position().subtract(range, range, range)))) {
            if (e.equals(livingEntity) || e.equals(shooter))
                continue;
            if (canDamage(e)) {
                vec = e.position();
                damage(vec, world, shooter, spellStats, damage, effectSec, e);
            } else {
                e.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * effectSec, (int) spellStats.getAmpMultiplier()));
            }
        }
    }

    public boolean canDamage(LivingEntity livingEntity) {
        return livingEntity.hasEffect(MobEffects.POISON) || livingEntity.hasEffect(MobEffects.HUNGER);
    }

    public void damage(Vec3 vec, ServerLevel world, @Nullable LivingEntity shooter, SpellStats stats, float damage, int snareTime, LivingEntity livingEntity) {
        EntityDamageSource damageSource = new EntityDamageSource("poison", shooter == null ? ANFakePlayer.getPlayer(world) : shooter);
        damageSource.setMagic();
        dealDamage(world, shooter, damage, stats, livingEntity, damageSource);
        world.sendParticles(ParticleTypes.SPORE_BLOSSOM_AIR, vec.x, vec.y + 0.5, vec.z, 50,
                ParticleUtil.inRange(-0.1, 0.1), ParticleUtil.inRange(-0.1, 0.1), ParticleUtil.inRange(-0.1, 0.1), 0.5);
        livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20 * snareTime));
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

}
