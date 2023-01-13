package alexthw.ars_elemental;

import alexthw.ars_elemental.api.item.IElementalArmor;
import alexthw.ars_elemental.common.entity.familiars.FirenandoHolder;
import alexthw.ars_elemental.common.entity.familiars.MermaidHolder;
import alexthw.ars_elemental.common.entity.spells.EntityCurvedProjectile;
import alexthw.ars_elemental.common.entity.spells.EntityHomingProjectile;
import alexthw.ars_elemental.common.glyphs.*;
import alexthw.ars_elemental.common.items.armor.ArmorSet;
import alexthw.ars_elemental.common.items.armor.ShockPerk;
import alexthw.ars_elemental.common.items.armor.SporePerk;
import alexthw.ars_elemental.common.rituals.*;
import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.perk.ArmorPerkHolder;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.block.tile.RotatingTurretTile;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.light.LightManager;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import com.hollingsworth.arsnouveau.setup.APIRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hollingsworth.arsnouveau.common.block.BasicSpellTurret.TURRET_BEHAVIOR_MAP;
import static com.hollingsworth.arsnouveau.common.block.RotatingSpellTurret.ROT_TURRET_BEHAVIOR_MAP;
import static com.hollingsworth.arsnouveau.setup.Config.ITEM_LIGHTMAP;

public class ArsNouveauRegistry {
    public static final List<AbstractSpellPart> registeredSpells = new ArrayList<>();

    public static final SpellSchool NECROMANCY = new SpellSchool("necromancy");

    public static void init() {
        registerGlyphs();
        registerRituals();
        registerFamiliars(ArsNouveauAPI.getInstance());
        registerPerks();
        addDamageReductions();
    }

    private static void addDamageReductions() {
        IElementalArmor.damageResistances.put(SpellSchools.ELEMENTAL_FIRE, Arrays.asList(
                DamageSource.ON_FIRE,
                DamageSource.LAVA,
                DamageSource.IN_FIRE,
                DamageSource.HOT_FLOOR,
                DamageSource.DRAGON_BREATH)
        );
        IElementalArmor.damageResistances.put(SpellSchools.ELEMENTAL_WATER, Arrays.asList(
                DamageSource.DROWN,
                DamageSource.FREEZE,
                DamageSource.MAGIC,
                DamageSource.LIGHTNING_BOLT)
        );
        IElementalArmor.damageResistances.put(SpellSchools.ELEMENTAL_AIR, Arrays.asList(
                DamageSource.FALL,
                DamageSource.FLY_INTO_WALL,
                DamageSource.IN_WALL,
                DamageSource.LIGHTNING_BOLT)
        );
        IElementalArmor.damageResistances.put(SpellSchools.ELEMENTAL_EARTH, Arrays.asList(DamageSource.CACTUS,
                DamageSource.STARVE,
                DamageSource.SWEET_BERRY_BUSH,
                DamageSource.CACTUS,
                DamageSource.IN_WALL,
                new DamageSource("poison"))
        );
    }

    public static void registerGlyphs() {

        //effects
        register(EffectWaterGrave.INSTANCE);
        register(EffectConjureTerrain.INSTANCE);
        register(EffectCharm.INSTANCE);
        register(EffectLifeLink.INSTANCE);
        register(EffectSpores.INSTANCE);
        register(EffectDischarge.INSTANCE);

        //methods
        register(MethodHomingProjectile.INSTANCE);
        register(MethodCurvedProjectile.INSTANCE);

        //propagators
        register(PropagatorHoming.INSTANCE);
        register(PropagatorArc.INSTANCE);

        //filters
        register(AquaticFilter.INSTANCE);
        register(AquaticFilter.NOT_INSTANCE);
        register(FieryFilter.INSTANCE);
        register(FieryFilter.NOT_INSTANCE);
        register(AerialFilter.INSTANCE);
        register(AerialFilter.NOT_INSTANCE);
        register(InsectFilter.INSTANCE);
        register(InsectFilter.NOT_INSTANCE);
        register(UndeadFilter.INSTANCE);
        register(UndeadFilter.NOT_INSTANCE);
    }

    public static void registerRituals() {

        registerRitual(new SquirrelRitual());
        registerRitual(new TeslaRitual());
        registerRitual(new DetectionRitual());
        registerRitual(new RepulsionRitual());
        registerRitual(new AttractionRitual());

    }

    public static void registerRitual(AbstractRitual ritual) {
        ArsNouveauAPI.getInstance().registerRitual(ritual);
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
        addSchool(EffectSummonUndead.INSTANCE, NECROMANCY);

        //Tweaks
        EffectFirework.INSTANCE.compatibleAugments.add(AugmentDampen.INSTANCE);
        EffectLaunch.INSTANCE.compatibleAugments.add(AugmentExtendTime.INSTANCE);
        EffectLaunch.INSTANCE.compatibleAugments.add(AugmentDurationDown.INSTANCE);
        EffectGravity.INSTANCE.compatibleAugments.add(AugmentSensitive.INSTANCE);
        EffectWindshear.INSTANCE.compatibleAugments.add(AugmentFortune.INSTANCE);

        ArsNouveauRegistry.addLights();
        ArsNouveauRegistry.addPerkSlots();
        ArsNouveauAPI.getInstance().getEnchantingRecipeTypes().add(ModRegistry.NETHERITE_UP.get());

    }

    public static void addSchool(AbstractSpellPart part, SpellSchool school) {
        part.spellSchools.add(school);
        school.addSpellPart(part);
    }

    public static void register(AbstractSpellPart spellPart) {
        ArsNouveauAPI.getInstance().registerSpell(spellPart);
        registeredSpells.add(spellPart);
    }

    public static void registerFamiliars(ArsNouveauAPI api) {
        api.registerFamiliar(new MermaidHolder());
        api.registerFamiliar(new FirenandoHolder());
    }

    public static void registerPerks() {
        APIRegistry.registerPerk(SporePerk.INSTANCE);
        APIRegistry.registerPerk(ShockPerk.INSTANCE);
    }

    private static void addPerkSlots() {

        ArsNouveauAPI api = ArsNouveauAPI.getInstance();
        ArmorSet[] medium_armors = {ModItems.AIR_ARMOR, ModItems.FIRE_ARMOR, ModItems.EARTH_ARMOR, ModItems.WATER_ARMOR};
        List<PerkSlot> perkSlots = Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE);
        for (ArmorSet set : medium_armors) {
            api.registerPerkProvider(set.getHat(), stack -> new ArmorPerkHolder(stack, List.of(perkSlots, perkSlots, perkSlots, perkSlots)));
            api.registerPerkProvider(set.getChest(), stack -> new ArmorPerkHolder(stack, List.of(perkSlots, perkSlots, perkSlots, perkSlots)));
            api.registerPerkProvider(set.getLegs(), stack -> new ArmorPerkHolder(stack, List.of(perkSlots, perkSlots, perkSlots, perkSlots)));
            api.registerPerkProvider(set.getBoots(), stack -> new ArmorPerkHolder(stack, List.of(perkSlots, perkSlots, perkSlots, perkSlots)));
        }

    }

    public static void addLights() {
        ITEM_LIGHTMAP.put(ModItems.FLASHING_POD.getId(), 14);
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

        ROT_TURRET_BEHAVIOR_MAP.put(MethodHomingProjectile.INSTANCE, (resolver, tile, world, pos, fakePlayer, position, direction) -> {
            EntityHomingProjectile spell = new EntityHomingProjectile(world, resolver);
            spell.setOwner(fakePlayer);
            spell.setPos(position.x(), position.y(), position.z());
            spell.setIgnored(MethodHomingProjectile.basicIgnores(fakePlayer, resolver.spell.getAugments(0, null).contains(AugmentSensitive.INSTANCE), resolver.spell));
            if (tile instanceof RotatingTurretTile rotatingTurretTile) {
                Vec3 vec3d = rotatingTurretTile.getShootAngle().normalize();
                spell.shoot(vec3d.x(), vec3d.y(), vec3d.z(), 0.25f, 0);
            }
            world.addFreshEntity(spell);
        });

        ROT_TURRET_BEHAVIOR_MAP.put(MethodCurvedProjectile.INSTANCE, (resolver, tile, world, pos, fakePlayer, position, direction) -> {
            EntityProjectileSpell spell = new EntityCurvedProjectile(world, resolver);
            spell.setOwner(fakePlayer);
            spell.setPos(position.x(), position.y(), position.z());
            if (tile instanceof RotatingTurretTile rotatingTurretTile) {
                Vec3 vec3d = rotatingTurretTile.getShootAngle().normalize();
                spell.shoot(vec3d.x(), vec3d.y(), vec3d.z(), 0.6f, 0);
            }
            world.addFreshEntity(spell);
        });

        TURRET_BEHAVIOR_MAP.put(MethodHomingProjectile.INSTANCE, (resolver, tile, world, pos, fakePlayer, position, direction) -> {
            EntityHomingProjectile spell = new EntityHomingProjectile(world, resolver);
            spell.setOwner(fakePlayer);
            spell.setPos(position.x(), position.y(), position.z());
            spell.setIgnored(MethodHomingProjectile.basicIgnores(fakePlayer, resolver.spell.getAugments(0, null).contains(AugmentSensitive.INSTANCE), resolver.spell));
            spell.shoot(direction.getStepX(), direction.getStepY(), direction.getStepZ(), 0.25f, 0);
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
