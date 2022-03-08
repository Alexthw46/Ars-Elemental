package alexthw.ars_elemental;

import alexthw.ars_elemental.common.glyphs.*;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import alexthw.ars_elemental.common.entity.familiars.MermaidHolder;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentFortune;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.effect.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArsNouveauRegistry {
    public static final List<AbstractSpellPart> registeredSpells = new ArrayList<>();

    public static final SpellSchool NECROMANCY = new SpellSchool("necromancy");

    public static void registerGlyphs(){
        //effects
        register(EffectWaterGrave.INSTANCE);
        register(EffectConjureDirt.INSTANCE);
        register(EffectCharm.INSTANCE);
        register(EffectLifeLink.INSTANCE);
        //methods
        register(MethodHomingProjectile.INSTANCE);
        register(MethodCurvedProjectile.INSTANCE);

    }

    public static void addCustomThings(){
        EffectWaterGrave.INSTANCE.spellSchools.add(SpellSchools.ELEMENTAL_WATER);
        EffectConjureDirt.INSTANCE.spellSchools.addAll(Set.of(SpellSchools.CONJURATION,SpellSchools.ELEMENTAL_EARTH));

        EffectHeal.INSTANCE.spellSchools.add(NECROMANCY);
        EffectSummonVex.INSTANCE.spellSchools.add(NECROMANCY);
        EffectWither.INSTANCE.spellSchools.add(NECROMANCY);
        EffectHex.INSTANCE.spellSchools.add(NECROMANCY);
        EffectLifeLink.INSTANCE.spellSchools.add(NECROMANCY);
        EffectCharm.INSTANCE.spellSchools.add(NECROMANCY);

        EffectLaunch.INSTANCE.compatibleAugments.add(AugmentExtendTime.INSTANCE);
        EffectLaunch.INSTANCE.compatibleAugments.add(AugmentDurationDown.INSTANCE);
        EffectWindshear.INSTANCE.compatibleAugments.add(AugmentFortune.INSTANCE);
        EffectCrush.INSTANCE.compatibleAugments.add(AugmentSensitive.INSTANCE);
    }

    public static void register(AbstractSpellPart spellPart){
        ArsNouveauAPI.getInstance().registerSpell(spellPart.getId(), spellPart);
        registeredSpells.add(spellPart);
    }

    public static void registerFamiliars(){
        ArsNouveauAPI.getInstance().registerFamiliar(new MermaidHolder());
    }

}
