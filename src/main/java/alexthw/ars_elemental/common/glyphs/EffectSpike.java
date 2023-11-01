package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.common.entity.DripstoneSpikeEntity;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
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
            if (!world.getBlockState(pos.below(i)).isAir()) {
                Vec3 location = rayTraceResult.getLocation();
                float baseDamage = (float) (DAMAGE.get() + spellStats.getAccMultiplier() * AMP_VALUE.get());

                DripstoneSpikeEntity spike = new DripstoneSpikeEntity(world, location.x, location.y, location.z, baseDamage, shooter, spellStats, spellContext, resolver);
                world.addFreshEntity(spike);
                break;
            }
        }
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {

        //check if the blockstate hit is air, if not spawn a dripstone spike entity
        if (!world.getBlockState(rayTraceResult.getBlockPos()).isAir()) {
            Vec3 location = rayTraceResult.getLocation();
            float baseDamage = (float) (DAMAGE.get() + spellStats.getAccMultiplier() * AMP_VALUE.get());

            DripstoneSpikeEntity spike = new DripstoneSpikeEntity(world, location.x, location.y, location.z, baseDamage, shooter, spellStats, spellContext, resolver);
            world.addFreshEntity(spike);
        }

    }

    @Override
    public int getDefaultManaCost() {
        return 30;
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
                AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE,
                AugmentFortune.INSTANCE
        );
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.ELEMENTAL_EARTH);
    }

}
