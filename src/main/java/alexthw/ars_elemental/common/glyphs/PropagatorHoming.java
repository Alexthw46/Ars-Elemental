package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.api.IPropagator;
import alexthw.ars_elemental.common.entity.spells.EntityHomingProjectile;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.block.BasicSpellTurret;
import com.hollingsworth.arsnouveau.common.block.tile.BasicSpellTurretTile;
import com.hollingsworth.arsnouveau.common.block.tile.RotatingTurretTile;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSplit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static alexthw.ars_elemental.common.glyphs.MethodHomingProjectile.getProjectileSpeed;

public class PropagatorHoming extends ElementalAbstractEffect implements IPropagator {

    public static PropagatorHoming INSTANCE = new PropagatorHoming();

    public PropagatorHoming() {
        super("propagator_homing", "Propagate Homing");
    }

    public void propagate(Level world, Vec3 pos, LivingEntity shooter, SpellStats stats, SpellResolver resolver, SpellContext spellContext) {
        int numSplits = 1 + stats.getBuffCount(AugmentSplit.INSTANCE);

        List<EntityHomingProjectile> projectiles = new ArrayList<>();
        // Create the projectiles
        for (int i = 0; i < numSplits; i++) {
            projectiles.add(new EntityHomingProjectile(world, resolver));
        }
        float velocity = getProjectileSpeed(stats);
        int opposite = -1;
        int counter = 0;

        // Adjust the direction of the projectiles
        Vec3 direction = pos.subtract(shooter.position());
        if (spellContext.castingTile instanceof BasicSpellTurretTile turretTile) {
            if (turretTile instanceof RotatingTurretTile rotatingTurretTile) {
                direction = rotatingTurretTile.getShootAngle();
            } else {
                direction = new Vec3(turretTile.getBlockState().getValue(BasicSpellTurret.FACING).step());
            }
        }

        // Set the position and shoot the projectiles in the correct direction
        for (EntityHomingProjectile proj : projectiles) {
            proj.setPos(pos.add(0, 1, 0));
            proj.setIgnored(MethodHomingProjectile.basicIgnores(shooter, stats.hasBuff(AugmentSensitive.INSTANCE), resolver.spell));
            if (!(shooter instanceof FakePlayer)) {
                proj.shoot(shooter, shooter.getXRot(), shooter.getYRot() + Math.round(counter / 2.0) * 5 * opposite, 0.0F, velocity, 0.8f);
            } else {
                proj.shoot(direction.x, direction.y, direction.z, velocity, 0.8F);
            }
            opposite = opposite * -1;
            counter++;
            world.addFreshEntity(proj);
        }

    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        copyResolver(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        copyResolver(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
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

    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Nonnull
    public Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.MANIPULATION);
    }

    @Override
    public Integer getTypeIndex() {
        return 8;
    }
}
