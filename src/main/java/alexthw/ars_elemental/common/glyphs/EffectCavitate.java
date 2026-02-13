package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.registry.ModParticles;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.ANFakePlayer;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.DamageUtil;
import com.hollingsworth.arsnouveau.common.entity.BubbleEntity;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentRandomize;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class EffectCavitate extends ElementalAbstractEffect implements IDamageEffect, IPotionEffect {

    public static final EffectCavitate INSTANCE = new EffectCavitate("cavitate", "Cavitate");

    public EffectCavitate(String tag, String description) {
        super(tag, description);
    }

    @Override
    public String getBookDescription() {
        return "Makes the target's bubble implode from rapid changes in pressure, causing a violent shockwave that causes damage in an area and soaks the targets.";
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        Entity target = rayTraceResult.getEntity();
        if (!(world instanceof ServerLevel serverLevel))
            return;
        float damage = (float) (DAMAGE.get() + AMP_VALUE.get() * spellStats.getAmpMultiplier());
        double range = 2 + spellStats.getAoeMultiplier();
        DamageSource source = buildDamageSource(world, shooter);
        if ((target instanceof BubbleEntity || target.isPassenger() && target.getVehicle() instanceof BubbleEntity) && attemptDamage(world, shooter, spellStats, spellContext, resolver, target, source, damage)) {
            for (LivingEntity entity : world.getEntitiesOfClass(LivingEntity.class, new AABB(target.blockPosition()).inflate(range), e -> e != shooter && e != target)) {
                attemptDamage(world, shooter, spellStats, spellContext, resolver, entity, source, damage);
                this.applyConfigPotion(entity, ModPotions.SOAKED_EFFECT, spellStats);
            }
            Vec3 vec = target.position();
            double cx = vec.x;
            double cy = vec.y + 0.5; // Center mass of the mob
            double cz = vec.z;
            // Emit the shockwave from the bubble
            // Spawn 3 flat rings at distinct heights, middle one is larger
            serverLevel.sendParticles(ModParticles.SHOCKWAVE_SMALL.get(), cx, cy - 0.5, cz, 1, 0, 0, 0, 0);
            serverLevel.sendParticles(ModParticles.SHOCKWAVE.get(), cx, cy, cz, 1, 0, 0, 0, 0);
            serverLevel.sendParticles(ModParticles.SHOCKWAVE_SMALL.get(), cx, cy + 0.5, cz, 1, 0, 0, 0, 0);
        }
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addDamageConfig(builder, 6);
        addAmpConfig(builder, 1);
        addDefaultPotionConfig(builder);
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        addDamageAugmentDescriptions(map);
        map.put(AugmentAOE.INSTANCE, "Increases the range of the shockwave");
        addPotionAugmentDescriptions(map);
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return Set.of(SpellSchools.ELEMENTAL_WATER);
    }

    @Override
    protected int getDefaultManaCost() {
        return 80;
    }

    @Override
    public DamageSource buildDamageSource(Level world, LivingEntity shooter) {
        shooter = !(shooter instanceof Player) ? ANFakePlayer.getPlayer((ServerLevel) world) : shooter;
        return DamageUtil.source(world, ModRegistry.CAVITATION, shooter);
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return Set.of(AugmentAOE.INSTANCE, AugmentAmplify.INSTANCE, AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE, AugmentRandomize.INSTANCE, AugmentDampen.INSTANCE);
    }

    @Override
    public int getBaseDuration() {
        return POTION_TIME == null ? 15 : POTION_TIME.get();
    }

    @Override
    public int getExtendTimeDuration() {
        return EXTEND_TIME == null ? 5 : EXTEND_TIME.get();
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }
}
