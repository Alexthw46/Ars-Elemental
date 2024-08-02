package alexthw.ars_elemental.common.blocks.prism;

import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ArcPrismLens extends AbstractPrismLens {

    public ArcPrismLens(Properties properties) {
        super(properties, "arc");
    }

    public void shoot(ServerLevel world, BlockPos pos, EntityProjectileSpell spell, Vec3 angle) {
        // create a new EntityCurvedProjectile and copy the properties of the spell
        super.shoot(world, pos, spell, angle);
        spell.setGravity(true);
    }

    @Override
    public boolean canConvert(EntityProjectileSpell spell, Level level, BlockPos pos) {
        return spell.isNoGravity();
    }

}
