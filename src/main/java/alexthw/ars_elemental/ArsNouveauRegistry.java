package alexthw.ars_elemental;

import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import alexthw.ars_elemental.common.entity.familiars.SirenFamiliar;

import java.util.ArrayList;
import java.util.List;

public class ArsNouveauRegistry {
    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>();

    public static void registerGlyphs(){ }

    public static void register(AbstractSpellPart spellPart){
        ArsNouveauAPI.getInstance().registerSpell(spellPart.getId(),spellPart);
        registeredSpells.add(spellPart);
    }

    public static void registerFamiliars(){
        ArsNouveauAPI.getInstance().registerFamiliar(new SirenFamiliar());
    }

}
