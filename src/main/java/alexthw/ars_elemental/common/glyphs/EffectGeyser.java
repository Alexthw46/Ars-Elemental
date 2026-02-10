package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.common.entity.spells.EntityGeyser;
import alexthw.ars_elemental.common.entity.spells.EntityLavaGeyser;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

import static alexthw.ars_elemental.util.CompatUtils.fireCheck;

public class EffectGeyser extends ElementalAbstractEffect {

    public static final EffectGeyser INSTANCE = new EffectGeyser("geyser", "Create Geyser");

    public EffectGeyser(String tag, String description) {
        super(tag, description);
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return Set.of(SpellSchools.ELEMENTAL_WATER, SpellSchools.ELEMENTAL_FIRE);
    }

    @Override
    public String getBookDescription() {
        return "Creates a Geyser on the spot that soaks and propels entities upwards for a small time. Height controlled by Amplify, size controlled by AoE";
    }

    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolverContext) {
        if (world.isClientSide) return;

        // Determine spawn location
        Vec3 hitPos = rayTraceResult.getLocation();
        BlockPos blockPos = BlockPos.containing(hitPos);
        float height = (float) Math.max(0d, 4.0f + (spellStats.getAmpMultiplier() * 2.0f)); // Base 4 blocks high + 2 * Amplify

        // If we hit a block side, adjust to spawn "on top" or "in front" of it
        if (rayTraceResult instanceof BlockHitResult bhr) {
            Direction face = bhr.getDirection();
            if (face != Direction.UP)
                blockPos = blockPos.relative(face);
            while (world.getBlockState(blockPos).isAir() && blockPos.distManhattan(bhr.getBlockPos()) < height * 2) {
                blockPos = blockPos.below();
            }
        } else {
            blockPos.above();
        }
        if (world.getBlockState(blockPos).isAir()) return;

        // Calculate Stats
        int duration = 5 * 20; // Base 5 seconds
        duration += (int) (spellStats.getDurationMultiplier() * 20); // Each Extend Time adds 1 second

        float aoe = (float) spellStats.getAoeMultiplier();

        // Spawn the Geyser Entity
        EntityGeyser geyser = fireCheck(resolverContext) ? new EntityLavaGeyser(world, blockPos, duration, height, aoe) : new EntityGeyser(world, blockPos, duration, height, aoe);
        world.addFreshEntity(geyser);
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        map.put(AugmentAOE.INSTANCE, "Increases the size of the Geyser.");
        map.put(AugmentAmplify.INSTANCE, "Increases the height of the Geyser");
        map.put(AugmentDampen.INSTANCE, "Decreases the height of the Geyser");
        map.put(AugmentExtendTime.INSTANCE, "Extends the lifetime of the Geyser.");
        map.put(AugmentDurationDown.INSTANCE, "Reduces the lifetime of the Geyser.");
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Override
    protected int getDefaultManaCost() {
        return 40;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return Set.of(AugmentAOE.INSTANCE, AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE, AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE);
    }

}
