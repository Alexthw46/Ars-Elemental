package alexthw.ars_elemental;

import alexthw.ars_elemental.common.glyphs.EffectConjureDirt;
import alexthw.ars_elemental.common.glyphs.EffectWaterGrave;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import alexthw.ars_elemental.common.entity.familiars.MermaidHolder;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentFortune;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLaunch;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectWindshear;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArsNouveauRegistry {
    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>();

    public static void registerGlyphs(){
        register(EffectWaterGrave.INSTANCE);
        EffectWaterGrave.INSTANCE.spellSchools.add(SpellSchools.ELEMENTAL_WATER);
        register(EffectConjureDirt.INSTANCE);
        EffectConjureDirt.INSTANCE.spellSchools.addAll(Set.of(SpellSchools.CONJURATION,SpellSchools.ELEMENTAL_EARTH));
        EffectLaunch.INSTANCE.compatibleAugments.add(AugmentExtendTime.INSTANCE);
        EffectLaunch.INSTANCE.compatibleAugments.add(AugmentDurationDown.INSTANCE);
        EffectWindshear.INSTANCE.compatibleAugments.add(AugmentFortune.INSTANCE);
    }


    public static void register(AbstractSpellPart spellPart){
        ArsNouveauAPI.getInstance().registerSpell(spellPart.getId(), spellPart);
        registeredSpells.add(spellPart);
    }

    public static void registerFamiliars(){
        ArsNouveauAPI.getInstance().registerFamiliar(new MermaidHolder());
    }

}
