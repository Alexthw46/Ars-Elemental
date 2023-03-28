package alexthw.ars_elemental.common.glyphs.filters;

import alexthw.ars_elemental.common.glyphs.ElementalAbstractFilter;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class SummonFilter extends ElementalAbstractFilter {

    public static ElementalAbstractFilter INSTANCE = new SummonFilter("summon", "Summon");
    public static ElementalAbstractFilter NOT_INSTANCE = new SummonFilter("not_summon", "Not Summon").inverted();

    public SummonFilter(String name, String description) {
        super(name, description);
    }

    @Override
    public boolean shouldResolveOnBlock(BlockHitResult target) {
        return false;
    }

    @Override
    public boolean shouldResolveOnEntity(EntityHitResult target) {
        return target.getEntity() instanceof ISummon summon;
    }

}
