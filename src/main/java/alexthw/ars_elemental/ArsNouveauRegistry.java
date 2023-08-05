package alexthw.ars_elemental;

import alexthw.ars_elemental.common.entity.familiars.FirenandoFamiliar;
import alexthw.ars_elemental.common.entity.familiars.FirenandoHolder;
import alexthw.ars_elemental.common.entity.familiars.MermaidHolder;
import alexthw.ars_elemental.common.entity.spells.EntityCurvedProjectile;
import alexthw.ars_elemental.common.entity.spells.EntityHomingProjectile;
import alexthw.ars_elemental.common.glyphs.*;
import alexthw.ars_elemental.common.glyphs.filters.*;
import alexthw.ars_elemental.common.items.armor.ArmorSet;
import alexthw.ars_elemental.common.items.armor.ShockPerk;
import alexthw.ars_elemental.common.items.armor.SporePerk;
import alexthw.ars_elemental.common.items.armor.SummonPerk;
import alexthw.ars_elemental.common.rituals.*;
import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.perk.ArmorPerkHolder;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import com.hollingsworth.arsnouveau.api.registry.FamiliarRegistry;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.api.registry.RitualRegistry;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.block.tile.RotatingTurretTile;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.light.LightManager;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hollingsworth.arsnouveau.common.block.BasicSpellTurret.TURRET_BEHAVIOR_MAP;
import static com.hollingsworth.arsnouveau.common.block.RotatingSpellTurret.ROT_TURRET_BEHAVIOR_MAP;
import static com.hollingsworth.arsnouveau.setup.config.Config.ITEM_LIGHTMAP;

public class ArsNouveauRegistry {
    public static final List<AbstractSpellPart> registeredSpells = new ArrayList<>();

    public static final SpellSchool NECROMANCY = new SpellSchool("necromancy");

    public static void init() {
        registerGlyphs();
        registerRituals();
        registerFamiliars();
        registerPerks();
    }

    public static void registerGlyphs() {

        //effects
        register(EffectWaterGrave.INSTANCE);
        register(EffectBubbleShield.INSTANCE);
        register(EffectConjureTerrain.INSTANCE);
        register(EffectCharm.INSTANCE);
        register(EffectPhantom.INSTANCE);
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
        register(SummonFilter.INSTANCE);
        register(SummonFilter.NOT_INSTANCE);
    }

    public static void registerRituals() {

        registerRitual(new SquirrelRitual());
        registerRitual(new TeslaRitual());
        registerRitual(new DetectionRitual());
        registerRitual(new RepulsionRitual());
        registerRitual(new AttractionRitual());

    }

    public static void registerRitual(AbstractRitual ritual) {
        RitualRegistry.registerRitual(ritual);
    }

    public static void postInit() {
        //Schools
        addSchool(EffectHeal.INSTANCE, NECROMANCY);
        addSchool(EffectSummonVex.INSTANCE, NECROMANCY);
        addSchool(EffectWither.INSTANCE, NECROMANCY);
        addSchool(EffectHex.INSTANCE, NECROMANCY);
        addSchool(EffectLifeLink.INSTANCE, NECROMANCY);
        addSchool(EffectCharm.INSTANCE, NECROMANCY);
        addSchool(EffectSummonUndead.INSTANCE, NECROMANCY);

        addSchool(EffectCut.INSTANCE, SpellSchools.ELEMENTAL_AIR);

        //Tweaks
        EffectFirework.INSTANCE.compatibleAugments.add(AugmentDampen.INSTANCE);
        EffectLaunch.INSTANCE.compatibleAugments.add(AugmentExtendTime.INSTANCE);
        EffectLaunch.INSTANCE.compatibleAugments.add(AugmentDurationDown.INSTANCE);
        EffectGravity.INSTANCE.compatibleAugments.add(AugmentSensitive.INSTANCE);
        EffectWindshear.INSTANCE.compatibleAugments.add(AugmentFortune.INSTANCE);

        ArsNouveauRegistry.addLights();
        ArsNouveauRegistry.addPerkSlots();

        ArsNouveauAPI.getInstance().getEnchantingRecipeTypes().add(ModRegistry.NETHERITE_UP.get());
        ArsNouveauAPI.getInstance().getEnchantingRecipeTypes().add(ModRegistry.ELEMENTAL_ARMOR_UP.get());

        FirenandoFamiliar.projectileGlyphs.addAll(List.of(MethodCurvedProjectile.INSTANCE, MethodHomingProjectile.INSTANCE, MethodProjectile.INSTANCE, PropagatorHoming.INSTANCE, PropagatorArc.INSTANCE));
    }

    public static void addSchool(AbstractSpellPart part, SpellSchool school) {
        part.spellSchools.add(school);
        school.addSpellPart(part);
    }

    public static void register(AbstractSpellPart spellPart) {
        GlyphRegistry.registerSpell(spellPart);
        registeredSpells.add(spellPart);
    }

    public static void registerFamiliars() {
        FamiliarRegistry.registerFamiliar(new MermaidHolder());
        FamiliarRegistry.registerFamiliar(new FirenandoHolder());
    }

    public static void registerPerks() {
        PerkRegistry.registerPerk(SporePerk.INSTANCE);
        PerkRegistry.registerPerk(ShockPerk.INSTANCE);
        PerkRegistry.registerPerk(SummonPerk.INSTANCE);
    }

    private static void addPerkSlots() {

        ArmorSet[] medium_armors = {ModItems.AIR_ARMOR, ModItems.FIRE_ARMOR, ModItems.EARTH_ARMOR, ModItems.WATER_ARMOR};
        List<PerkSlot> perkSlots = Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE);
        for (ArmorSet set : medium_armors) {
            PerkRegistry.registerPerkProvider(set.getHat(), stack -> new ArmorPerkHolder(stack, List.of(perkSlots, perkSlots, perkSlots, perkSlots)));
            PerkRegistry.registerPerkProvider(set.getChest(), stack -> new ArmorPerkHolder(stack, List.of(perkSlots, perkSlots, perkSlots, perkSlots)));
            PerkRegistry.registerPerkProvider(set.getLegs(), stack -> new ArmorPerkHolder(stack, List.of(perkSlots, perkSlots, perkSlots, perkSlots)));
            PerkRegistry.registerPerkProvider(set.getBoots(), stack -> new ArmorPerkHolder(stack, List.of(perkSlots, perkSlots, perkSlots, perkSlots)));
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

        ROT_TURRET_BEHAVIOR_MAP.put(MethodHomingProjectile.INSTANCE, new ITurretBehavior() {
            @Override
            public void onCast(SpellResolver resolver, ServerLevel world, BlockPos pos, Player fakePlayer, Position position, Direction direction) {
                EntityHomingProjectile spell = new EntityHomingProjectile(world, resolver);
                spell.setOwner(fakePlayer);
                spell.setPos(position.x(), position.y(), position.z());
                spell.setIgnored(MethodHomingProjectile.basicIgnores(fakePlayer, resolver.spell.getAugments(0, null).contains(AugmentSensitive.INSTANCE), resolver.spell));
                if (world.getBlockEntity(pos) instanceof RotatingTurretTile rotatingTurretTile) {
                    Vec3 vec3d = rotatingTurretTile.getShootAngle().normalize();
                    spell.shoot(vec3d.x(), vec3d.y(), vec3d.z(), 0.25f, 0);
                }
                world.addFreshEntity(spell);
            }
        });

        ROT_TURRET_BEHAVIOR_MAP.put(MethodCurvedProjectile.INSTANCE, new ITurretBehavior() {
            @Override
            public void onCast(SpellResolver resolver, ServerLevel world, BlockPos pos, Player fakePlayer, Position position, Direction direction) {
                EntityProjectileSpell spell = new EntityCurvedProjectile(world, resolver);
                spell.setOwner(fakePlayer);
                spell.setPos(position.x(), position.y(), position.z());
                if (world.getBlockEntity(pos) instanceof RotatingTurretTile rotatingTurretTile) {
                    Vec3 vec3d = rotatingTurretTile.getShootAngle().normalize();
                    spell.shoot(vec3d.x(), vec3d.y(), vec3d.z(), 0.6f, 0);
                }
                world.addFreshEntity(spell);
            }
        });

        TURRET_BEHAVIOR_MAP.put(MethodHomingProjectile.INSTANCE, new ITurretBehavior() {
            @Override
            public void onCast(SpellResolver resolver, ServerLevel world, BlockPos pos, Player fakePlayer, Position position, Direction direction) {
                EntityHomingProjectile spell = new EntityHomingProjectile(world, resolver);
                spell.setOwner(fakePlayer);
                spell.setPos(position.x(), position.y(), position.z());
                spell.setIgnored(MethodHomingProjectile.basicIgnores(fakePlayer, resolver.spell.getAugments(0, null).contains(AugmentSensitive.INSTANCE), resolver.spell));
                spell.shoot(direction.getStepX(), direction.getStepY(), direction.getStepZ(), 0.25f, 0);
                world.addFreshEntity(spell);
            }
        });

        TURRET_BEHAVIOR_MAP.put(MethodCurvedProjectile.INSTANCE, new ITurretBehavior() {
            @Override
            public void onCast(SpellResolver resolver, ServerLevel world, BlockPos pos, Player fakePlayer, Position position, Direction direction) {
                EntityProjectileSpell spell = new EntityCurvedProjectile(world, resolver);
                spell.setOwner(fakePlayer);
                spell.setPos(position.x(), position.y(), position.z());
                spell.shoot(direction.getStepX(), direction.getStepY() + 0.25F, direction.getStepZ(), 0.6f, 0);
                world.addFreshEntity(spell);
            }
        });

    }

}
