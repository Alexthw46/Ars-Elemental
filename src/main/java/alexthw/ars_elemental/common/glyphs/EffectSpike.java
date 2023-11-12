package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.api.item.ISchoolFocus;
import alexthw.ars_elemental.common.entity.DripstoneSpikeEntity;
import alexthw.ars_elemental.common.entity.IceSpikeEntity;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EffectSpike extends ElementalAbstractEffect implements IDamageEffect {

    public static EffectSpike INSTANCE = new EffectSpike();

    public EffectSpike() {
        super("spike", "Spike");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {

        BlockPos pos = rayTraceResult.getEntity().getOnPos();
        //check if the blockstate below the entity is air up to 5 blocks, if not spawn a dripstone spike entity,

        for (int i = 0; i < 5; i++) {
            if (world.getBlockState(pos.below(i)).isAir()) continue;
            summonSpike(world, shooter, spellStats, spellContext, resolver, pos);
            break;
        }
    }

    private void summonSpike(Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver, BlockPos pos) {
        float baseDamage = (float) (DAMAGE.get() + spellStats.getAmpMultiplier() * AMP_VALUE.get());
        DripstoneSpikeEntity spike = ISchoolFocus.hasFocus(shooter) == SpellSchools.ELEMENTAL_WATER ?
                new IceSpikeEntity(world, pos, baseDamage, shooter, spellStats, spellContext, resolver) :
                new DripstoneSpikeEntity(world, pos, baseDamage, shooter, spellStats, spellContext, resolver);
        world.addFreshEntity(spike);
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        BlockPos pos = rayTraceResult.getBlockPos();
        //check if the blockstate hit is air, if not spawn a dripstone spike entity
        for (int i = 0; i < 5; i++) {
            if (world.getBlockState(pos.above(i)).isAir()) continue;
            summonSpike(world, shooter, spellStats, spellContext, resolver, pos);
            break;
        }

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
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addDamageConfig(builder, 8.0);
        addAmpConfig(builder, 2.5);
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
    protected @NotNull Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.ELEMENTAL_EARTH);
    }

}
