package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.common.entity.spells.EntityWaterJet;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentRandomize;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSplit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EffectWaterJet extends ElementalAbstractEffect implements IDamageEffect {

    public static final EffectWaterJet INSTANCE = new EffectWaterJet("water_jet", "Water Jet");

    public EffectWaterJet(String tag, String description) {
        super(tag, description);
    }

    @Override
    public String getBookDescription() {
        return "Creates a high pressure water jet that pierce trough the closest target's armor after few seconds. Split can be used to spawn multiple at the same time, using randomize on top will make each jet target a different entity if possible.";
    }

    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolverContext) {
        float baseDamage = (float) (DAMAGE.get() + spellStats.getAmpMultiplier() * AMP_VALUE.get());

        Vec3 impactPos = rayTraceResult instanceof EntityHitResult entityHitResult ? entityHitResult.getEntity().getEyePosition() : rayTraceResult.getLocation();

        // 1. Determine Number of Jets
        int numJets = 1 + spellStats.getBuffCount(AugmentSplit.INSTANCE);

        // 2. Define Search Area
        double range = 10.0;
        AABB searchArea = new AABB(BlockPos.containing(impactPos)).inflate(range);

        // 3. Get Potential Targets
        List<LivingEntity> potentialTargets = world.getEntitiesOfClass(LivingEntity.class, searchArea, (entity) -> entity != shooter && entity.isAlive());

        // Sort by distance to impact point
        potentialTargets.sort(Comparator.comparingDouble(e -> e.distanceToSqr(impactPos)));

        if (potentialTargets.isEmpty()) {
            // Fizzle if no one is around
            if (world instanceof ServerLevel sl)
                sl.sendParticles(ParticleTypes.SPLASH, impactPos.x, impactPos.y, impactPos.z, 10, 0.5, 0.5, 0.5, 0.1);
            return;
        }

        // 4. Spawn Jets Loop
        double radius = 1.5; // The radius of the spawning circle
        // Pre-calculate Jet Origins and assign Targets
        List<Vec3> jetOrigins = new ArrayList<>();
        List<Integer> targetIds = new ArrayList<>();
        for (int i = 0; i < numJets; i++) {

            // A. Calculate Spawn Position (Circle around impact)
            double angle = (2 * Math.PI / numJets) * i;
            double offsetX = Math.cos(angle) * radius;
            double offsetZ = Math.sin(angle) * radius;

            // Lift the jet slightly
            Vec3 jetOrigin = new Vec3(impactPos.x + offsetX, impactPos.y + 1.0, impactPos.z + offsetZ);
            jetOrigins.add(jetOrigin);
            // Randomize switch behavior
            // Spread Shot: Modulo operator cycles through available targets if we have more jets than enemies
            // Focus Fire: Always pick the closest one
            LivingEntity target = spellStats.isRandomized() ? potentialTargets.get(i % potentialTargets.size()) : potentialTargets.getFirst();
            targetIds.add(target.getId());
        }
        // Spawn marker entity to delay the effect
        EntityWaterJet marker = new EntityWaterJet(world, jetOrigins, targetIds, baseDamage, spellStats, spellContext, resolverContext);
        world.addFreshEntity(marker);
    }

    @Override
    protected int getDefaultManaCost() {
        return 80;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return Set.of(AugmentSplit.INSTANCE, AugmentRandomize.INSTANCE, AugmentAmplify.INSTANCE);
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return Set.of(SpellSchools.ELEMENTAL_WATER);
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        map.put(AugmentSplit.INSTANCE, "Fire one additional water jet.");
        map.put(AugmentRandomize.INSTANCE, "If used in combination with Split, the different jets will try to hit different targets.");
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addDamageConfig(builder, 4);
        addAmpConfig(builder, 1);
    }
}
