package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.common.entity.spells.EntityHomingProjectile;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSplit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Set;

public class PropagatorHoming extends AbstractEffect implements IPropagator {

    public static PropagatorHoming INSTANCE = new PropagatorHoming();

    public PropagatorHoming() {
        super("propagator_homing", "Propagate Homing");
    }

    public void propagate(Level world, Vec3 pos, LivingEntity shooter, SpellStats stats, SpellResolver resolver) {
        ArrayList<EntityHomingProjectile> projectiles = new ArrayList<>();
        EntityHomingProjectile first = new EntityHomingProjectile(world, resolver);
        first.setPos(pos.add(0, 1, 0));
        projectiles.add(first);

        MethodHomingProjectile.splits(world, shooter, new BlockPos(pos), resolver, projectiles, stats.getBuffCount(AugmentSplit.INSTANCE));

        float velocity = MethodHomingProjectile.getProjectileSpeed(stats);

        Vec3 direction = pos.subtract(shooter.position());
        for (EntityHomingProjectile proj : projectiles) {
            proj.setIgnored(MethodHomingProjectile.basicIgnores(shooter, stats.hasBuff(AugmentSensitive.INSTANCE), resolver.spell));
            if (direction.distanceTo(Vec3.ZERO) < 0.25) {
                proj.shoot(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, velocity, 0.8f);
            } else {
                proj.shoot(direction.x, direction.y, direction.z, velocity, 0.8F);
            }
            world.addFreshEntity(proj);
        }

    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext) {
        copyResolver(rayTraceResult, world, shooter, spellStats, spellContext);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext) {
        copyResolver(rayTraceResult, world, shooter, spellStats, spellContext);
    }

    @Override
    public int getDefaultManaCost() {
        return 400;
    }

    @NotNull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return MethodHomingProjectile.INSTANCE.getCompatibleAugments();
    }

    public SpellTier getTier() {
        return SpellTier.THREE;
    }

    @Nonnull
    public Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.MANIPULATION);
    }


}
