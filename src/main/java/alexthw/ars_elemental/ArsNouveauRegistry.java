package alexthw.ars_elemental;

import alexthw.ars_elemental.glyphs.ConjureTerrain;
import alexthw.ars_elemental.glyphs.MethodArcProj;
import alexthw.ars_elemental.glyphs.MethodHomingProj;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;

import java.util.ArrayList;
import java.util.List;

public class ArsNouveauRegistry {
    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>();

    public static void registerGlyphs(){
        register(ConjureTerrain.INSTANCE);
        register(MethodHomingProj.INSTANCE);
        register(MethodArcProj.INSTANCE);
    }

    public static void register(AbstractSpellPart spellPart){
        ArsNouveauAPI.getInstance().registerSpell(spellPart.tag,spellPart);
        registeredSpells.add(spellPart);
    }

}
