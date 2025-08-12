package alexthw.ars_elemental.api.item;

import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDecelerate;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public interface SpellPrismLens {

    default void shoot(ServerLevel world, BlockPos pos, EntityProjectileSpell spell, Vec3 angle) {
        // recalculate velocity based on augments on the spell, clamp to 0.1-0.5+
        float acceleration = spell.resolver().spell.getBuffsAtIndex(0, null, AugmentAccelerate.INSTANCE) - spell.resolver().spell.getBuffsAtIndex(0, null, AugmentDecelerate.INSTANCE) * 0.5F;
        float velocity = Math.max(0.1f, 0.55f + 0.1f * Math.min(8, acceleration));

        spell.shoot(angle.x(), angle.y(), angle.z(), velocity, 0);
    }

    boolean canConvert(EntityProjectileSpell spell, Level level, BlockPos pos);

    default void addTooltip(List<Component> tooltip, ItemStack lensStack) {

    }
}
