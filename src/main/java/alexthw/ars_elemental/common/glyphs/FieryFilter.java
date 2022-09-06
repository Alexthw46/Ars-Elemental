package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.registry.ModRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class FieryFilter extends ElementalAbstractFilter {

    public static ElementalAbstractFilter INSTANCE = new FieryFilter("fiery", "Fiery");
    public static ElementalAbstractFilter NOT_INSTANCE = new FieryFilter("not_fiery", "Not Fiery").inverted();

    FieryFilter(String name, String description) {
        super(name, description);
    }

    /**
     * Whether the filter should allow the block hit
     *
     * @param target BlockHitResult
     */
    @Override
    public boolean shouldResolveOnBlock(BlockHitResult target) {
        return false;
    }

    /**
     * Whether the filter should allow the entity hit
     *
     * @param target EntityHitResult
     */
    @Override
    public boolean shouldResolveOnEntity(EntityHitResult target) {
        return target.getEntity() instanceof LivingEntity living && (living.getType().is(ModRegistry.FIERY) || living.fireImmune());
    }

}
