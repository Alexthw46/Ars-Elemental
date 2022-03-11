package alexthw.ars_elemental.common.items;

import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;

public interface ISchoolItem extends ISpellModifierItem {

    SpellSchool getSchool();
    
}
