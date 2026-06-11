package alexthw.ars_elemental.common.blocks.prism;

import alexthw.ars_elemental.common.glyphs.MethodHomingProjectile;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.common.entity.EntityHomingProjectileSpell;
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
        SpellResolver resolver = spell.resolver();
        EntityHomingProjectileSpell newProjectile = new EntityHomingProjectileSpell(world, resolver);
        List<Predicate<LivingEntity>> ignore = MethodHomingProjectile.basicIgnores(resolver.spellContext.getUnwrappedCaster(), resolver.getCastStats(), resolver.spellContext, resolver);
        newProjectile.setIgnored(ignore);
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
        return !(spell instanceof EntityHomingProjectileSpell);
    }

}
