package alexthw.ars_elemental.api;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.TileCaster;
import com.hollingsworth.arsnouveau.common.block.BasicSpellTurret;
import com.hollingsworth.arsnouveau.common.block.tile.RotatingTurretTile;
import com.hollingsworth.arsnouveau.common.block.tile.RuneTile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface IPropagator {

    AbstractAugment DUMMY = new AbstractAugment("dummy", "Dummy") {
        @Override
        public int getDefaultManaCost() {
            return 0;
        }
    };

    default void copyResolver(HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats stats, SpellContext spellContext, SpellResolver resolver) {
        spellContext.setCanceled(true);
        Spell newSpell = spellContext.getRemainingSpell();
        if (newSpell.isEmpty()) return;
        newSpell.recipe.add(0, DUMMY);
        SpellContext newContext = spellContext.clone().withSpell(newSpell);
        SpellResolver newResolver = resolver.getNewResolver(newContext);
        propagate(world, rayTraceResult, shooter, stats, newResolver);
    }

    void propagate(Level world, HitResult hitResult, LivingEntity shooter, SpellStats stats, SpellResolver resolver);

    static Vec3 adjustProjectileDirection(HitResult hitResult, LivingEntity shooter, SpellResolver resolver, Vec3 pos) {
        Vec3 direction;
        if (resolver.spellContext.getCaster() instanceof TileCaster tc) {
            if (tc.getTile() instanceof RotatingTurretTile rotatingTurretTile) {
                direction = rotatingTurretTile.getShootAngle();
            } else if (tc.getTile() instanceof RuneTile && hitResult instanceof EntityHitResult ehr) {
                direction = pos.subtract(ehr.getEntity().position());
            } else {
                direction = new Vec3(tc.getTile().getBlockState().getValue(BasicSpellTurret.FACING).step());
            }
        } else direction = pos.subtract(shooter.position());
        return direction;
    }
}
