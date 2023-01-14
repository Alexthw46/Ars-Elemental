package alexthw.ars_elemental.common.blocks.prism;

import alexthw.ars_elemental.api.item.SpellPrismLent;
import alexthw.ars_elemental.common.entity.spells.EntityHomingProjectile;
import alexthw.ars_elemental.common.glyphs.MethodHomingProjectile;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDecelerate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class HomingPrismLens extends Item implements SpellPrismLent {

    public HomingPrismLens(Properties properties) {
        super(properties);
    }

    public void shoot(ServerLevel world, BlockPos pos, EntityProjectileSpell spell, Vec3 angle) {

        EntityHomingProjectile newProjectile = new EntityHomingProjectile(world, spell.spellResolver);
        List<Predicate<LivingEntity>> ignore = MethodHomingProjectile.basicIgnores(spell.spellResolver.spellContext.getUnwrappedCaster(), true, spell.spellResolver.spell);
        newProjectile.setIgnored(ignore);
        newProjectile.setPos(spell.getX(), spell.getY(), spell.getZ());

        float acceleration = spell.spellResolver.spell.getBuffsAtIndex(0, null, AugmentAccelerate.INSTANCE) - spell.spellResolver.spell.getBuffsAtIndex(0, null, AugmentDecelerate.INSTANCE) * 0.5F;
        float velocity = Math.max(0.1f, 0.5f + 0.1f * Math.min(2, acceleration));
        newProjectile.shoot(angle.x(), angle.y(), angle.z(), velocity, 0);
        world.addFreshEntity(newProjectile);
        spell.discard();
    }

    @Override
    public boolean canConvert(EntityProjectileSpell spell) {
        return !(spell instanceof EntityHomingProjectile);
    }
}
