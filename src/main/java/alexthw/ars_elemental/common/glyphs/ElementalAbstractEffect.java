package alexthw.ars_elemental.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;

import static alexthw.ars_elemental.ArsElemental.prefix;

public abstract class ElementalAbstractEffect extends AbstractEffect {

    public ElementalAbstractEffect(String tag, String description) {
        super(prefix("glyph_" + tag), description);
    }

}
