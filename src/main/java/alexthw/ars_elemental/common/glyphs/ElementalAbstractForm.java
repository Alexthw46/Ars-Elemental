package alexthw.ars_elemental.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;

public abstract class ElementalAbstractForm extends AbstractCastMethod {
    public ElementalAbstractForm(String tag, String description) {
        super("glyph_" + tag, description);
    }
}
