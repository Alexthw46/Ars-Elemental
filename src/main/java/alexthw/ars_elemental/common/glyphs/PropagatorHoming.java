package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.api.IPropagator;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.entity.EntityHomingProjectileSpell;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSplit;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectReset;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static alexthw.ars_elemental.common.glyphs.MethodHomingProjectile.getProjectileSpeed;

public class PropagatorHoming extends ElementalAbstractEffect implements IPropagator {

    public static PropagatorHoming INSTANCE = new PropagatorHoming();

    public PropagatorHoming() {
        super("propagator_homing", "Propagate Homing");
        EffectReset.RESET_LIMITS.add(this);
    }

    @Override
    public void propagate(Level world, HitResult hitResult, LivingEntity shooter, SpellStats stats, SpellResolver resolver) {
        Vec3 pos = hitResult.getLocation();
        int numSplits = 1 + stats.getBuffCount(AugmentSplit.INSTANCE);

        List<EntityHomingProjectileSpell> projectiles = new ArrayList<>();
        // Create the projectiles
        for (int i = 0; i < numSplits; i++) {
            projectiles.add(new EntityHomingProjectileSpell(world, resolver));
        }
        float velocity = getProjectileSpeed(stats);
        int opposite = -1;
        int counter = 0;

        // Adjust the direction of the projectiles
        Vec3 direction = IPropagator.getDirection(shooter, resolver, pos);

        // Set the position and shoot the projectiles in the correct direction
        for (EntityHomingProjectileSpell proj : projectiles) {
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
    public void onResolve(HitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
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
