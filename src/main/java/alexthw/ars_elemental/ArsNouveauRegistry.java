package alexthw.ars_elemental;

import alexthw.ars_elemental.common.entity.familiars.FirenandoFamiliar;
import alexthw.ars_elemental.common.entity.familiars.FirenandoHolder;
import alexthw.ars_elemental.common.entity.familiars.FlashjackFamiliar;
import alexthw.ars_elemental.common.entity.familiars.FlashjackHolder;
import alexthw.ars_elemental.common.entity.familiars.MermaidHolder;
import alexthw.ars_elemental.common.glyphs.*;
import alexthw.ars_elemental.common.glyphs.filters.AerialFilter;
import alexthw.ars_elemental.common.glyphs.filters.AquaticFilter;
import alexthw.ars_elemental.common.glyphs.filters.FieryFilter;
import alexthw.ars_elemental.common.glyphs.filters.InsectFilter;
import alexthw.ars_elemental.common.glyphs.filters.SummonFilter;
import alexthw.ars_elemental.common.glyphs.filters.UndeadFilter;
import alexthw.ars_elemental.common.items.armor.ArmorSet;
import alexthw.ars_elemental.common.items.armor.ShockPerk;
import alexthw.ars_elemental.common.items.armor.SporePerk;
import alexthw.ars_elemental.common.items.armor.SummonPerk;
import alexthw.ars_elemental.common.rituals.AttractionRitual;
import alexthw.ars_elemental.common.rituals.DetectionRitual;
import alexthw.ars_elemental.common.rituals.PollinationRitual;
import alexthw.ars_elemental.common.rituals.RepulsionRitual;
import alexthw.ars_elemental.common.rituals.SquirrelRitual;
import alexthw.ars_elemental.common.rituals.TeslaRitual;
import alexthw.ars_elemental.common.rituals.forest.ArchwoodForestRitual;
import alexthw.ars_elemental.common.rituals.forest.ArchwoodForestationRitual;
import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.registry.ModParticles;
import alexthw.ars_elemental.registry.ModRegistry;
import com.alexthw.sauce.registry.SauceTags;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.documentation.DocAssets;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.ParticleTypeProperty;
import com.hollingsworth.arsnouveau.api.particle.timelines.IParticleTimelineType;
import com.hollingsworth.arsnouveau.api.particle.timelines.LingerTimeline;
import com.hollingsworth.arsnouveau.api.particle.timelines.SimpleParticleTimelineType;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import com.hollingsworth.arsnouveau.api.registry.FamiliarRegistry;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.api.registry.RitualRegistry;
import com.hollingsworth.arsnouveau.api.registry.SpellCasterRegistry;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.ITurretBehavior;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.block.tile.RotatingTurretTile;
import com.hollingsworth.arsnouveau.common.entity.EntityHomingProjectileSpell;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.light.LightManager;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentFortune;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.alexthw.sauce.api.item.IElementalArmor.damageResistances;
import static com.alexthw.sauce.registry.ModRegistry.E_TOME_CASTER;
import static com.hollingsworth.arsnouveau.api.registry.ParticleTimelineRegistry.TIMELINE_DF;
import static com.hollingsworth.arsnouveau.common.block.BasicSpellTurret.TURRET_BEHAVIOR_MAP;
import static com.hollingsworth.arsnouveau.common.block.RotatingSpellTurret.ROT_TURRET_BEHAVIOR_MAP;
import static com.hollingsworth.arsnouveau.setup.config.Config.ITEM_LIGHTMAP;
import static net.neoforged.fml.loading.FMLLoader.isProduction;

public class ArsNouveauRegistry {
    public static final List<AbstractSpellPart> registeredSpells = new ArrayList<>();

    public static void init() {
        registerGlyphs();
        registerRituals();
        registerFamiliars();
        registerPerks();
        linkDamageResistances();
    }

    private static void registerCasters() {
        SpellCasterRegistry.register(ModItems.SPELL_HORN.get(), (stack) -> stack.get(DataComponentRegistry.SPELL_CASTER.get()));
        SpellCasterRegistry.register(ModItems.AIR_CTOME.get(), (stack) -> stack.get(E_TOME_CASTER.get()));
        SpellCasterRegistry.register(ModItems.FIRE_CTOME.get(), (stack) -> stack.get(E_TOME_CASTER.get()));
        SpellCasterRegistry.register(ModItems.EARTH_CTOME.get(), (stack) -> stack.get(E_TOME_CASTER.get()));
        SpellCasterRegistry.register(ModItems.WATER_CTOME.get(), (stack) -> stack.get(E_TOME_CASTER.get()));
        SpellCasterRegistry.register(ModItems.NECRO_CTOME.get(), (stack) -> stack.get(E_TOME_CASTER.get()));
        SpellCasterRegistry.register(ModItems.SHAPERS_CTOME.get(), (stack) -> stack.get(E_TOME_CASTER.get()));
        SpellCasterRegistry.register(ModItems.CHAIN_LENS.get(), (stack) -> stack.get(DataComponentRegistry.SPELL_CASTER));

    }

    private static void linkDamageResistances() {
        damageResistances.put(SpellSchools.ELEMENTAL_FIRE, SauceTags.FIRE_DAMAGE);
        damageResistances.put(SpellSchools.ELEMENTAL_AIR, SauceTags.AIR_DAMAGE);
        damageResistances.put(SpellSchools.ELEMENTAL_EARTH, SauceTags.EARTH_DAMAGE);
        damageResistances.put(SpellSchools.ELEMENTAL_WATER, SauceTags.WATER_DAMAGE);
    }


    //    public static final DeferredHolder<IParticleTimelineType<?>, IParticleTimelineType<ProjectileTimeline>> ARC_PROJECTILE_TIMELINE = TIMELINE_DF.register("projectile", () -> new SimpleParticleTimelineType<>(MethodArcProjectile.INSTANCE, ProjectileTimeline.CODEC, ProjectileTimeline.STREAM_CODEC, ProjectileTimeline::new));
    //    public static final DeferredHolder<IParticleTimelineType<?>, IParticleTimelineType<ProjectileTimeline>> HOMING_PROJECTILE_TIMELINE = TIMELINE_DF.register("projectile", () -> new SimpleParticleTimelineType<>(MethodHomingProjectile.INSTANCE, ProjectileTimeline.CODEC, ProjectileTimeline.STREAM_CODEC, ProjectileTimeline::new));
    public static final DeferredHolder<IParticleTimelineType<?>, IParticleTimelineType<LingerTimeline>> GRAVITY_TIMELINE = TIMELINE_DF.register("gravity", () -> new SimpleParticleTimelineType<>(EffectGravity.INSTANCE, LingerTimeline.CODEC, LingerTimeline.STREAM_CODEC, LingerTimeline::new));

    public static void registerRitual(AbstractRitual ritual) {
        RitualRegistry.registerRitual(ritual);
    }

    @Deprecated
    public static final DocAssets.BlitInfo ANIMA_ICON = new DocAssets.BlitInfo(ArsNouveau.prefix("textures/gui/documentation/doc_icon_anima.png"), 10, 10);

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
        register(EffectEnvenom.INSTANCE);
        register(EffectSpike.INSTANCE);
        register(EffectSpark.INSTANCE);
        register(EffectConflagrate.INSTANCE);

        // Water Update
        register(EffectWaterJet.INSTANCE);
        register(EffectGeyser.INSTANCE);
        register(EffectMist.INSTANCE);
        register(EffectSlipper.INSTANCE);
        register(EffectCavitate.INSTANCE);
        register(EffectOxidize.INSTANCE);

        //summons
        register(EffectSummonBee.INSTANCE);
        register(EffectSummonSlime.INSTANCE);

        //methods
        register(MethodHomingProjectile.INSTANCE);
        register(MethodArcProjectile.INSTANCE);

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

        // the bullshit one
        register(EffectNullify.INSTANCE);

        if (!isProduction()) {
            register(MethodCarianPhalanx.INSTANCE);
        }
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
        FamiliarRegistry.registerFamiliar(new FlashjackHolder());
    }

    public static void registerPerks() {
        PerkRegistry.registerPerk(SporePerk.INSTANCE);
        PerkRegistry.registerPerk(ShockPerk.INSTANCE);
        PerkRegistry.registerPerk(SummonPerk.INSTANCE);
    }

    public static ArmorSet[] medium_armors = {ModItems.AIR_ARMOR, ModItems.FIRE_ARMOR, ModItems.EARTH_ARMOR, ModItems.WATER_ARMOR};
    public static ArmorSet[] heavy_armors = {ModItems.AIR_ARMOR_H, ModItems.FIRE_ARMOR_H, ModItems.EARTH_ARMOR_H, ModItems.WATER_ARMOR_H};
    public static ArmorSet[] light_armors = {ModItems.AIR_ARMOR_L, ModItems.FIRE_ARMOR_L, ModItems.EARTH_ARMOR_L, ModItems.WATER_ARMOR_L};

    private static void addPerkSlots() {

        for (ArmorSet set : medium_armors) {
            PerkRegistry.registerPerkProvider(set.getHat(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
            PerkRegistry.registerPerkProvider(set.getChest(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
            PerkRegistry.registerPerkProvider(set.getLegs(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
            PerkRegistry.registerPerkProvider(set.getBoots(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
        }

        if (ConfigHandler.Startup.ENABLE_ARMOR_REWORK.get()) {
            for (ArmorSet set : heavy_armors) {
                PerkRegistry.registerPerkProvider(set.getHat(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.TWO)));
                PerkRegistry.registerPerkProvider(set.getChest(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
                PerkRegistry.registerPerkProvider(set.getLegs(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
                PerkRegistry.registerPerkProvider(set.getBoots(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.TWO)));
            }
            for (ArmorSet set : light_armors) {
                PerkRegistry.registerPerkProvider(set.getHat(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
                PerkRegistry.registerPerkProvider(set.getChest(), makePerkList(Arrays.asList(PerkSlot.TWO, PerkSlot.TWO, PerkSlot.THREE)));
                PerkRegistry.registerPerkProvider(set.getLegs(), makePerkList(Arrays.asList(PerkSlot.TWO, PerkSlot.TWO, PerkSlot.THREE)));
                PerkRegistry.registerPerkProvider(set.getBoots(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
            }
        } else {
            for (ArmorSet set : light_armors) {
                PerkRegistry.registerPerkProvider(set.getHat(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
                PerkRegistry.registerPerkProvider(set.getChest(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
                PerkRegistry.registerPerkProvider(set.getLegs(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
                PerkRegistry.registerPerkProvider(set.getBoots(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
            }
            for (ArmorSet set : heavy_armors) {
                PerkRegistry.registerPerkProvider(set.getHat(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
                PerkRegistry.registerPerkProvider(set.getChest(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
                PerkRegistry.registerPerkProvider(set.getLegs(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
                PerkRegistry.registerPerkProvider(set.getBoots(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
            }
        }

    }

    private static @NotNull List<List<PerkSlot>> makePerkList(List<PerkSlot> perkSlots) {
        return List.of(perkSlots, perkSlots, perkSlots, perkSlots);
    }

    public static void addLights() {
        ITEM_LIGHTMAP.put(ModItems.FLASHING_POD.getId(), 14);
        LightManager.register(ModEntities.FLASHJACK_ENTITY.get(), p -> 8);
        LightManager.register(ModEntities.FLASHJACK_FAMILIAR.get(), p -> 8);

        LightManager.register(ModEntities.FIRENANDO_ENTITY.get(), p -> {
            if (p.level().getBrightness(LightLayer.BLOCK, p.blockPosition()) < 6) {
                return 10;
            }
            return 0;
        });
        LightManager.register(ModEntities.FIRENANDO_FAMILIAR.get(), p -> {
            if (p.level().getBrightness(LightLayer.BLOCK, p.blockPosition()) < 6) {
                return 10;
            }
            return 0;
        });

    }

    public static void postInit() {
        registerCasters();

        //Schools
        addSchool(EffectHeal.INSTANCE, SpellSchools.NECROMANCY);
        addSchool(EffectSummonVex.INSTANCE, SpellSchools.NECROMANCY);
        addSchool(EffectWither.INSTANCE, SpellSchools.NECROMANCY);
        addSchool(EffectHex.INSTANCE, SpellSchools.NECROMANCY);
        addSchool(EffectLifeLink.INSTANCE, SpellSchools.NECROMANCY);
        addSchool(EffectCharm.INSTANCE, SpellSchools.NECROMANCY);
        addSchool(EffectSummonUndead.INSTANCE, SpellSchools.NECROMANCY);

        addSchool(EffectCut.INSTANCE, SpellSchools.ELEMENTAL_AIR);
        addSchool(EffectEvaporate.INSTANCE, SpellSchools.ELEMENTAL_FIRE);

        //Tweaks
        EffectFirework.INSTANCE.compatibleAugments.add(AugmentDampen.INSTANCE);
        EffectLaunch.INSTANCE.compatibleAugments.add(AugmentExtendTime.INSTANCE);
        EffectLaunch.INSTANCE.compatibleAugments.add(AugmentDurationDown.INSTANCE);
        EffectGravity.INSTANCE.compatibleAugments.add(AugmentSensitive.INSTANCE);
        EffectWindshear.INSTANCE.compatibleAugments.add(AugmentFortune.INSTANCE);

        ArsNouveauRegistry.addLights();
        ArsNouveauRegistry.addPerkSlots();

        ArsNouveauAPI.getInstance().getEnchantingRecipeTypes().add(ModRegistry.NETHERITE_UP.get());

        FirenandoFamiliar.projectileGlyphs.addAll(List.of(MethodArcProjectile.INSTANCE, MethodHomingProjectile.INSTANCE, MethodProjectile.INSTANCE, PropagatorHoming.INSTANCE, PropagatorArc.INSTANCE));
        FlashjackFamiliar.movementGlyphs.addAll(List.of(EffectLeap.INSTANCE, EffectLaunch.INSTANCE, EffectPull.INSTANCE, EffectKnockback.INSTANCE, EffectBlink.INSTANCE, EffectGlide.INSTANCE));

        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(ModParticles.SPARK_2.get(), true));
        ParticleTypeProperty.addType(new ParticleTypeProperty.ParticleData(ModParticles.VENOM_2.get(), true));
    }


    static {

        ROT_TURRET_BEHAVIOR_MAP.put(MethodHomingProjectile.INSTANCE, new ITurretBehavior() {
            @Override
            public void onCast(SpellResolver resolver, ServerLevel world, BlockPos pos, Player fakePlayer, Position position, Direction direction) {
                EntityHomingProjectileSpell spell = new EntityHomingProjectileSpell(world, resolver);
                SpellStats stats = resolver.getCastStats();
                spell.setOwner(fakePlayer);
                spell.setPos(position.x(), position.y(), position.z());
                spell.setIgnored(MethodHomingProjectile.basicIgnores(fakePlayer, resolver.spell.getAugments(0, null).contains(AugmentSensitive.INSTANCE), resolver.spell));
                float velocity = MethodHomingProjectile.getProjectileSpeed(stats);
                if (world.getBlockEntity(pos) instanceof RotatingTurretTile rotatingTurretTile) {
                    Vec3 vec3d = rotatingTurretTile.getShootAngle().normalize();
                    spell.shoot(vec3d.x(), vec3d.y(), vec3d.z(), velocity, 0);
                }
                world.addFreshEntity(spell);
            }
        });

        ROT_TURRET_BEHAVIOR_MAP.put(MethodArcProjectile.INSTANCE, new ITurretBehavior() {
            @Override
            public void onCast(SpellResolver resolver, ServerLevel world, BlockPos pos, Player fakePlayer, Position position, Direction direction) {
                EntityProjectileSpell spell = new EntityProjectileSpell(world, resolver);
                SpellStats stats = resolver.getCastStats();
                spell.setGravity(true);
                spell.setOwner(fakePlayer);
                spell.setPos(position.x(), position.y(), position.z());
                float velocity = MethodArcProjectile.getProjectileSpeed(stats);
                if (world.getBlockEntity(pos) instanceof RotatingTurretTile rotatingTurretTile) {
                    Vec3 vec3d = rotatingTurretTile.getShootAngle().normalize();
                    spell.shoot(vec3d.x(), vec3d.y(), vec3d.z(), velocity, 0);
                }
                world.addFreshEntity(spell);
            }
        });

        TURRET_BEHAVIOR_MAP.put(MethodHomingProjectile.INSTANCE, new ITurretBehavior() {
            @Override
            public void onCast(SpellResolver resolver, ServerLevel world, BlockPos pos, Player fakePlayer, Position position, Direction direction) {
                EntityHomingProjectileSpell spell = new EntityHomingProjectileSpell(world, resolver);
                SpellStats stats = resolver.getCastStats();
                float velocity = MethodHomingProjectile.getProjectileSpeed(stats);
                spell.setOwner(fakePlayer);
                spell.setPos(position.x(), position.y(), position.z());
                spell.setIgnored(MethodHomingProjectile.basicIgnores(fakePlayer, resolver.spell.getAugments(0, null).contains(AugmentSensitive.INSTANCE), resolver.spell));
                spell.shoot(direction.getStepX(), direction.getStepY(), direction.getStepZ(), velocity, 0);
                world.addFreshEntity(spell);
            }
        });

        TURRET_BEHAVIOR_MAP.put(MethodArcProjectile.INSTANCE, new ITurretBehavior() {
            @Override
            public void onCast(SpellResolver resolver, ServerLevel world, BlockPos pos, Player fakePlayer, Position position, Direction direction) {
                EntityProjectileSpell spell = new EntityProjectileSpell(world, resolver);
                SpellStats stats = resolver.getCastStats();
                float velocity = MethodArcProjectile.getProjectileSpeed(stats);
                spell.setGravity(true);
                spell.setOwner(fakePlayer);
                spell.setPos(position.x(), position.y(), position.z());
                spell.shoot(direction.getStepX(), direction.getStepY() + 0.25F, direction.getStepZ(), velocity, 0);
                world.addFreshEntity(spell);
            }
        });

    }

    public static void registerRituals() {

        registerRitual(new SquirrelRitual());
        registerRitual(new TeslaRitual());
        registerRitual(new DetectionRitual());
        registerRitual(new RepulsionRitual());
        registerRitual(new AttractionRitual());
        registerRitual(new PollinationRitual());
        registerRitual(new ArchwoodForestRitual());
        registerRitual(new ArchwoodForestationRitual());

    }


}
