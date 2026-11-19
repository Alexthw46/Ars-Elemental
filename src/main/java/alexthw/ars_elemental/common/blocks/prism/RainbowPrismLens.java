package alexthw.ars_elemental.common.blocks.prism;

import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@Deprecated(forRemoval = true)
public class RainbowPrismLens extends AbstractPrismLens {

    public RainbowPrismLens(Properties pProperties) {
        super(pProperties, "rgb");
    }

    @Override
    public void shoot(ServerLevel world, BlockPos pos, EntityProjectileSpell spell, Vec3 angle) {
        super.shoot(world, pos, spell, angle);
    }

    @Override
    public boolean canConvert(EntityProjectileSpell spell, Level level, BlockPos pos) {
        return false;
    }

}
