package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.common.entity.spikes.DripstoneSpikeEntity;
import alexthw.ars_elemental.common.entity.spikes.EnchantedDripstoneEntity;
import alexthw.ars_elemental.common.entity.spikes.IceSpikeEntity;
import alexthw.ars_elemental.util.CompatUtils;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.items.curios.ShapersFocus;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class EffectSpike extends ElementalAbstractEffect implements IDamageEffect {

    public static final EffectSpike INSTANCE = new EffectSpike();

    public EffectSpike() {
        super("spike", "Spike");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {

        BlockPos pos = rayTraceResult.getEntity().getOnPos();

        for (int i = 0; i < 3; i++) {
            if (world.getBlockState(pos.below(i)).isAir()) continue;
            summonSpike(world, shooter, spellStats, spellContext, resolver, pos, rayTraceResult.getEntity().position());
            return;
        }

        // no valid position found below, summon a falling spike over it instead
        float damagePerDistance = (float) (DAMAGE.get() + spellStats.getAmpMultiplier() * AMP_VALUE.get());
        EnchantedDripstoneEntity spike = new EnchantedDripstoneEntity(world, pos.above(2), resolver, spellStats);
        spike.setHurtsEntities(damagePerDistance, GENERIC_INT.get());
        world.addFreshEntity(spike);
        ShapersFocus.tryPropagateEntitySpell(spike, world, shooter, spellContext, resolver);

    }

    private void summonSpike(Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver, BlockPos pos, @Nullable Vec3 originalPosition) {
        float baseDamage = (float) (DAMAGE.get() + spellStats.getAmpMultiplier() * AMP_VALUE.get());
        DripstoneSpikeEntity spike = CompatUtils.waterCheck(resolver) ?
                new IceSpikeEntity(world, pos, baseDamage, shooter, spellStats, spellContext, resolver) :
                new DripstoneSpikeEntity(world, pos, baseDamage, shooter, spellStats, spellContext, resolver);
        world.addFreshEntity(spike);
        if (originalPosition != null)
            spike.setPos(new Vec3(originalPosition.x, spike.getY(), originalPosition.z));
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        BlockPos pos = rayTraceResult.getBlockPos();
        //check if the blockstate hit is air, if not spawn a dripstone spike entity
        for (int i = -1; i < 3; i++) {
            if (world.getBlockState(pos.below(i)).isAir()) continue;
            summonSpike(world, shooter, spellStats, spellContext, resolver, pos.below(i), null);
            return;
        }

        // no valid position found, shoot a spike instead

        //create falling block entity from a dripstone spike
        float damagePerDistance = (float) (DAMAGE.get() + spellStats.getAmpMultiplier() * AMP_VALUE.get());
        EnchantedDripstoneEntity spike = new EnchantedDripstoneEntity(world, pos, resolver, spellStats);
        spike.setHurtsEntities(damagePerDistance, GENERIC_INT.get());
        world.addFreshEntity(spike);
        ShapersFocus.tryPropagateEntitySpell(spike, world, shooter, spellContext, resolver);
    }

    @Override
    public int getDefaultManaCost() {
        return 30;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Override
    protected void addDefaultAugmentLimits(Map<ResourceLocation, Integer> defaults) {
        defaults.put(AugmentAmplify.INSTANCE.getRegistryName(), 2);
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addDamageConfig(builder, 8.0);
        addAmpConfig(builder, 2.5);
        addExtendTimeConfig(builder, 10);
        addGenericInt(builder, 40, "The maximum damage a thrown spike can deal to a single entity. The damage from thrown spikes scales with the height difference.", "maxFallDamage");
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
                AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE,
                AugmentAOE.INSTANCE, AugmentPierce.INSTANCE,
                AugmentExtendTime.INSTANCE, AugmentRandomize.INSTANCE,
                AugmentFortune.INSTANCE
        );
    }

    @Override
    public String getBookDescription() {
        return "Creates a spike of dripstone that will damage entities that touch it. Can be augmented with AoE and Pierce to make it wider or taller, with ExtendTime to make it last longer or with Amplify to make it deal more damage. If a spike can't be placed, a falling spike will be summoned instead, which only can be augmented with Amplify to increase the damage it deals based on the height it falls from.";
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        map.put(AugmentAOE.INSTANCE, "Increases the size of the spike");
        map.put(AugmentPierce.INSTANCE, "Increases the height of the spike");
        map.put(AugmentExtendTime.INSTANCE, "Extends the time before the spike retracts");
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.ELEMENTAL_EARTH, SpellSchools.ELEMENTAL_WATER);
    }

}
