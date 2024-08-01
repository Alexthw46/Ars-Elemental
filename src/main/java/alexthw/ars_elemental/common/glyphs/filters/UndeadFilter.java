package alexthw.ars_elemental.common.glyphs.filters;

import alexthw.ars_elemental.common.glyphs.ElementalAbstractFilter;
import alexthw.ars_elemental.registry.ModRegistry;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class UndeadFilter extends ElementalAbstractFilter {

    public static ElementalAbstractFilter INSTANCE = new UndeadFilter("undead", "Undead");
    public static ElementalAbstractFilter NOT_INSTANCE = new UndeadFilter("not_undead", "Not Undead").inverted();

    UndeadFilter(String name, String description) {
        super(name, description);
    }

    /**
     * Whether the filter should allow the block hit
     *
     * @param target BlockHitResult
     */
    @Override
    public boolean shouldResolveOnBlock(BlockHitResult target, Level level) {
        return false;
    }

    /**
     * Whether the filter should allow the entity hit
     *
     * @param target EntityHitResult
     */
    @Override
    public boolean shouldResolveOnEntity(EntityHitResult target, Level level) {
        return target.getEntity() instanceof LivingEntity living && (living.getType().is(EntityTypeTags.UNDEAD) || living.getType().is(ModRegistry.UNDEAD));
    }
}
