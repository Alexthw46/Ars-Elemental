package alexthw.ars_elemental.api.item;

import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public interface SpellPrismLent {

    void shoot(ServerLevel world, BlockPos pos, EntityProjectileSpell spell, Vec3 angle);

    boolean canConvert(EntityProjectileSpell spell);
}
