package alexthw.ars_elemental;

import alexthw.ars_elemental.common.entity.familiars.FirenandoHolder;
import alexthw.ars_elemental.common.entity.familiars.MermaidHolder;
import alexthw.ars_elemental.common.entity.spells.EntityCurvedProjectile;
import alexthw.ars_elemental.common.entity.spells.EntityHomingProjectile;
import alexthw.ars_elemental.common.glyphs.*;
import alexthw.ars_elemental.common.rituals.DetectionRitual;
import alexthw.ars_elemental.common.rituals.SquirrelRitual;
import alexthw.ars_elemental.common.rituals.TeslaRitual;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.light.LightManager;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentFortune;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import net.minecraft.world.level.LightLayer;

import java.util.ArrayList;
import java.util.List;

import static com.hollingsworth.arsnouveau.common.block.BasicSpellTurret.TURRET_BEHAVIOR_MAP;

public class ArsNouveauRegistry {
    public static final List<AbstractSpellPart> registeredSpells = new ArrayList<>();

    public static final SpellSchool NECROMANCY = new SpellSchool("necromancy");

    public static void init() {
        registerGlyphs();
        registerRituals();
        registerFamiliars(ArsNouveauAPI.getInstance());
        GlyphConfigs.registerGlyphConfigs();
    }

    public static void registerGlyphs() {
        //effects
        register(EffectWaterGrave.INSTANCE);
        register(EffectConjureDirt.INSTANCE);
        register(EffectCharm.INSTANCE);
        register(EffectLifeLink.INSTANCE);
        //methods
        register(MethodHomingProjectile.INSTANCE);
        register(MethodCurvedProjectile.INSTANCE);

    }

    public static void registerRituals() {

        registerRitual(new SquirrelRitual());
        registerRitual(new TeslaRitual());
        registerRitual(new DetectionRitual());

    }

    public static void registerRitual(AbstractRitual ritual) {
        ArsNouveauAPI.getInstance().registerRitual(ritual.getID(), ritual);
    }

    public static void postInit() {
        //Schools
        addSchool(EffectWaterGrave.INSTANCE, SpellSchools.ELEMENTAL_WATER);

        addSchool(EffectHeal.INSTANCE, NECROMANCY);
        addSchool(EffectSummonVex.INSTANCE, NECROMANCY);
        addSchool(EffectWither.INSTANCE, NECROMANCY);
        addSchool(EffectHex.INSTANCE, NECROMANCY);
        addSchool(EffectLifeLink.INSTANCE, NECROMANCY);
        addSchool(EffectCharm.INSTANCE, NECROMANCY);

        //Tweaks
        EffectLaunch.INSTANCE.compatibleAugments.add(AugmentExtendTime.INSTANCE);
        EffectLaunch.INSTANCE.compatibleAugments.add(AugmentDurationDown.INSTANCE);
        EffectGravity.INSTANCE.compatibleAugments.add(AugmentSensitive.INSTANCE);
        EffectWindshear.INSTANCE.compatibleAugments.add(AugmentFortune.INSTANCE);
        EffectCrush.INSTANCE.compatibleAugments.add(AugmentSensitive.INSTANCE);
    }

    public static void addSchool(AbstractSpellPart part, SpellSchool school) {
        part.spellSchools.add(school);
        school.addSpellPart(part);
    }

    public static void register(AbstractSpellPart spellPart) {
        ArsNouveauAPI.getInstance().registerSpell(spellPart.getId(), spellPart);
        registeredSpells.add(spellPart);
    }

    public static void registerFamiliars(ArsNouveauAPI api) {
        api.registerFamiliar(new MermaidHolder());
        api.registerFamiliar(new FirenandoHolder());
    }

    public static void addLights() {
        LightManager.register(ModEntities.HOMING_PROJECTILE.get(), (p -> 15));
        LightManager.register(ModEntities.CURVED_PROJECTILE.get(), (p -> 15));
        LightManager.register(ModEntities.FIRENANDO_ENTITY.get(), (p -> {
            if (p.level.getBrightness(LightLayer.BLOCK, p.blockPosition()) < 6) {
                return 10;
            }
            return 0;
        }));
        LightManager.register(ModEntities.FIRENANDO_FAMILIAR.get(), (p -> {
            if (p.level.getBrightness(LightLayer.BLOCK, p.blockPosition()) < 6) {
                return 10;
            }
            return 0;
        }));
    }

    static {
        TURRET_BEHAVIOR_MAP.put(MethodHomingProjectile.INSTANCE, (resolver, tile, world, pos, fakePlayer, position, direction) -> {
            EntityHomingProjectile spell = new EntityHomingProjectile(world, resolver);
            spell.setOwner(fakePlayer);
            spell.setPos(position.x(), position.y(), position.z());
            spell.setIgnored(MethodHomingProjectile.basicIgnores(fakePlayer, resolver.spell.getAugments(0,null).contains(AugmentSensitive.INSTANCE), resolver));
            spell.shoot(direction.getStepX(),  direction.getStepY(), direction.getStepZ(), 0.25f, 0);
            world.addFreshEntity(spell);
        });

        TURRET_BEHAVIOR_MAP.put(MethodCurvedProjectile.INSTANCE, (resolver, tile, world, pos, fakePlayer, position, direction) -> {
            EntityProjectileSpell spell = new EntityCurvedProjectile(world, resolver);
            spell.setOwner(fakePlayer);
            spell.setPos(position.x(), position.y(), position.z());
            spell.shoot(direction.getStepX(), direction.getStepY() + 0.25F, direction.getStepZ(), 0.6f, 0);
            world.addFreshEntity(spell);
        });

    }
}
