package alexthw.ars_elemental.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;

import static alexthw.ars_elemental.ArsElemental.prefix;

public abstract class ElementalAbstractForm extends AbstractCastMethod {
    public ElementalAbstractForm(String tag, String description) {
        super(prefix("glyph_" + tag), description);
    }

}
