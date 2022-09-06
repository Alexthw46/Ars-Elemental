package alexthw.ars_elemental.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.AbstractFilter;
import net.minecraft.world.phys.HitResult;

import static alexthw.ars_elemental.ArsElemental.prefix;

public abstract class ElementalAbstractFilter extends AbstractFilter {

    private boolean inverted = false;

    public ElementalAbstractFilter(String name, String description) {
        super(prefix("glyph_" + name + "_filter"), "Filter:" + description);
    }

    public ElementalAbstractFilter inverted() {
        this.inverted = !inverted;
        return this;
    }

    @Override
    public boolean shouldAffect(HitResult rayTraceResult) {
        return inverted != super.shouldAffect(rayTraceResult);
    }
}
