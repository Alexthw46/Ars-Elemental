package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.registry.ModRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class AquaticFilter extends ElementalAbstractFilter {

    public static ElementalAbstractFilter INSTANCE = new AquaticFilter("aquatic", "Aquatic");
    public static ElementalAbstractFilter NOT_INSTANCE = new AquaticFilter("not_aquatic", "Not Aquatic").inverted();

    AquaticFilter(String name, String description) {
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
        if (target.getEntity() instanceof LivingEntity living) {
            return living.getMobType() == MobType.WATER || living.getType().is(ModRegistry.AQUATIC);
        }
        return false;
    }

}
