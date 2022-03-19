package alexthw.ars_elemental.util;

import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;

public class TooManyCompats {
    //TODO adjust for tmg filters
    public boolean resolveFilters(AbstractSpellPart... spells){
        for (AbstractSpellPart spellPart : spells){
            if (spellPart instanceof AbstractEffect){
                return false;
            }
        }
        return true;
    }

}
