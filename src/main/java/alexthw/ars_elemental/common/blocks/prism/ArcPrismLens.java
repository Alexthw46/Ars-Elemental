package alexthw.ars_elemental.common.blocks.prism;

import alexthw.ars_elemental.common.entity.spells.EntityCurvedProjectile;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class ArcPrismLens extends AbstractPrismLens {

    public ArcPrismLens(Properties properties) {
        super(properties);
    }

    public void shoot(ServerLevel world, BlockPos pos, EntityProjectileSpell spell, Vec3 angle) {
        EntityCurvedProjectile newProjectile = new EntityCurvedProjectile(world, spell.spellResolver);
        newProjectile.setColor(spell.getParticleColor());
        newProjectile.pierceLeft = spell.pierceLeft;
        newProjectile.age = spell.age;
        newProjectile.setPos(spell.getX(), spell.getY(), spell.getZ());
        super.shoot(world, pos, newProjectile, angle);
        world.addFreshEntity(newProjectile);
        spell.discard();
    }

    @Override
    public boolean canConvert(EntityProjectileSpell spell) {
        return !(spell instanceof EntityCurvedProjectile);
    }

    @Override
    protected String getDescriptionKey() {
        return super.getDescriptionKey() + "arc";
    }
}
