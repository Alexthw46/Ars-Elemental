package alexthw.ars_elemental.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;

public abstract class ElementalAbstractEffect extends AbstractEffect {

    public ElementalAbstractEffect(String tag, String description) {
        super("glyph_" + tag, description);
        //super(prefix(tag), description);
    }

}
