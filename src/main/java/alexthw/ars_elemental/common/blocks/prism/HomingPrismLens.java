package alexthw.ars_elemental.common.blocks.prism;

import alexthw.ars_elemental.common.entity.spells.EntityHomingProjectile;
import alexthw.ars_elemental.common.glyphs.MethodHomingProjectile;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class HomingPrismLens extends AbstractPrismLens {

    public HomingPrismLens(Properties properties) {
        super(properties, "homing");
    }

    public void shoot(ServerLevel world, BlockPos pos, EntityProjectileSpell spell, Vec3 angle) {
        // create a new EntityHomingProjectile and copy the properties of the spell
        EntityHomingProjectile newProjectile = new EntityHomingProjectile(world, spell.spellResolver);
        List<Predicate<LivingEntity>> ignore = MethodHomingProjectile.basicIgnores(spell.spellResolver.spellContext.getUnwrappedCaster(), true, spell.spellResolver.spell);
        newProjectile.setIgnored(ignore);
        newProjectile.setColor(spell.getParticleColor());
        newProjectile.pierceLeft = spell.pierceLeft;
        newProjectile.prismRedirect = spell.prismRedirect;
        newProjectile.age = spell.age;
        newProjectile.setPos(spell.getX(), spell.getY(), spell.getZ());
        super.shoot(world, pos, newProjectile, angle);
        world.addFreshEntity(newProjectile);
        spell.discard();
    }

    @Override
    public boolean canConvert(EntityProjectileSpell spell, Level level, BlockPos pos) {
        return !(spell instanceof EntityHomingProjectile);
    }

}
