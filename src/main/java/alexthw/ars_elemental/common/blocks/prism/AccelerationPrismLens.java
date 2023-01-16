package alexthw.ars_elemental.common.blocks.prism;

import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class AccelerationPrismLens extends AbstractPrismLens {
    public AccelerationPrismLens(Properties properties) {
        super(properties);
    }

    @Override
    public void shoot(ServerLevel world, BlockPos pos, EntityProjectileSpell spell, Vec3 angle) {
        spell.spellResolver.spell.add(AugmentAccelerate.INSTANCE, 1, 1);
        super.shoot(world, pos, spell, angle);
    }

    @Override
    public boolean canConvert(EntityProjectileSpell spell) {
        return true;
    }

    @Override
    protected String getDescriptionKey() {
        return super.getDescriptionKey() + "acceleration";
    }

}
