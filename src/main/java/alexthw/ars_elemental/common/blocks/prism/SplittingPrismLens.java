package alexthw.ars_elemental.common.blocks.prism;

import com.hollingsworth.arsnouveau.api.util.SourceUtil;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SplittingPrismLens extends AbstractPrismLens {
    public SplittingPrismLens(Properties properties) {
        super(properties, "split");
    }

    @Override
    public void shoot(ServerLevel world, BlockPos pos, EntityProjectileSpell spell, Vec3 angle) {
        super.shoot(world, pos, spell, rotateVec(angle, 20));

        EntityProjectileSpell split = new EntityProjectileSpell(world, spell.spellResolver);
        split.setColor(spell.getParticleColor());
        split.pierceLeft = spell.pierceLeft;
        split.age = spell.age;
        split.prismRedirect = spell.prismRedirect;
        split.setPos(spell.getX(), spell.getY(), spell.getZ());
        world.addFreshEntity(split);
        super.shoot(world, pos, split, rotateVec(angle, -20));

        SourceUtil.takeSourceWithParticles(pos, world, 6, spell.spellResolver.getResolveCost());
    }

    @Override
    public boolean canConvert(EntityProjectileSpell spell, Level level, BlockPos pos) {
        return SourceUtil.hasSourceNearby(pos, level, 6, spell.spellResolver.getResolveCost()) && spell.prismRedirect < 4;
    }

    public Vec3 rotateVec(Vec3 vec, float angle) {
        // Rotate the vector around the Y axis
        double x = vec.x * Math.cos(angle) - vec.z * Math.sin(angle);
        double z = vec.x * Math.sin(angle) + vec.z * Math.cos(angle);
        return new Vec3(x, vec.y, z);
    }
}
