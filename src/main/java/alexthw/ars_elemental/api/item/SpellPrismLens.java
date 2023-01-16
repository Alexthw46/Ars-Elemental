package alexthw.ars_elemental.api.item;

import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDecelerate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public interface SpellPrismLens {

    default void shoot(ServerLevel world, BlockPos pos, EntityProjectileSpell spell, Vec3 angle) {
        float acceleration = spell.spellResolver.spell.getBuffsAtIndex(0, null, AugmentAccelerate.INSTANCE) - spell.spellResolver.spell.getBuffsAtIndex(0, null, AugmentDecelerate.INSTANCE) * 0.5F;
        float velocity = Math.max(0.1f, 0.5f + 0.1f * Math.min(4, acceleration));

        spell.shoot(angle.x(), angle.y(), angle.z(), velocity, 0);
    }

    boolean canConvert(EntityProjectileSpell spell);
}
