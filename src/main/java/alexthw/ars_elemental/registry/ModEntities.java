package alexthw.ars_elemental.registry;

import alexthw.ars_elemental.common.entity.FirenandoEntity;
import alexthw.ars_elemental.common.entity.MermaidEntity;
import alexthw.ars_elemental.common.entity.familiars.FirenandoFamiliar;
import alexthw.ars_elemental.common.entity.familiars.MermaidFamiliar;
import alexthw.ars_elemental.common.entity.mages.*;
import alexthw.ars_elemental.common.entity.spells.EntityCurvedProjectile;
import alexthw.ars_elemental.common.entity.spells.EntityHomingProjectile;
import alexthw.ars_elemental.common.entity.spells.EntityLerpedProjectile;
import alexthw.ars_elemental.common.entity.spells.EntityMagnetSpell;
import alexthw.ars_elemental.common.entity.summon.*;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.WealdWalker;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectDelay;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLaunch;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectWindshear;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static alexthw.ars_elemental.ArsElemental.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<EntityType<MermaidEntity>> SIREN_ENTITY;
    public static final RegistryObject<EntityType<MermaidFamiliar>> SIREN_FAMILIAR;
    public static final RegistryObject<EntityType<FirenandoEntity>> FIRENANDO_ENTITY;
    public static final RegistryObject<EntityType<FirenandoFamiliar>> FIRENANDO_FAMILIAR;

    public static final RegistryObject<EntityType<WealdWalker>> FLASHING_WEALD_WALKER;

    public static final RegistryObject<EntityType<EntityMageBase>> FIRE_MAGE;
    public static final RegistryObject<EntityType<EntityMageBase>> WATER_MAGE;
    public static final RegistryObject<EntityType<EntityMageBase>> AIR_MAGE;
    public static final RegistryObject<EntityType<EntityMageBase>> EARTH_MAGE;

    public static final RegistryObject<EntityType<SummonSkeleHorse>> SKELEHORSE_SUMMON;
    public static final RegistryObject<EntityType<SummonDirewolf>> DIREWOLF_SUMMON;
    public static final RegistryObject<EntityType<SummonUndead>> WSKELETON_SUMMON;
    public static final RegistryObject<EntityType<SummonDolphin>> DOLPHIN_SUMMON;
    public static final RegistryObject<EntityType<SummonStrider>> STRIDER_SUMMON;

    public static final RegistryObject<EntityType<AllyVhexEntity>> VHEX_SUMMON;
    public static final RegistryObject<EntityType<EntityHomingProjectile>> HOMING_PROJECTILE;
    public static final RegistryObject<EntityType<EntityCurvedProjectile>> CURVED_PROJECTILE;
    public static final RegistryObject<EntityType<EntityMagnetSpell>> LINGER_MAGNET;
    public static final RegistryObject<EntityType<EntityLerpedProjectile>> LERP_PROJECTILE;

    static {
        SIREN_ENTITY = registerEntity("siren_entity", 0.4F, 1.0F, MermaidEntity::new, MobCategory.WATER_CREATURE);
        SIREN_FAMILIAR = registerEntity("siren_familiar", 0.4F, 1.0F, MermaidFamiliar::new, MobCategory.WATER_CREATURE);

        FIRENANDO_ENTITY = addEntity("firenando_entity", 1.0F, 2.2F, true, false, FirenandoEntity::new, MobCategory.CREATURE);
        FIRENANDO_FAMILIAR = addEntity("firenando_familiar", 1.0F, 1.8F, true, false, FirenandoFamiliar::new, MobCategory.CREATURE);

        FIRE_MAGE = registerEntity("fire_mage", 0.5F, 1.8F, FireMage::new, MobCategory.MONSTER);
        WATER_MAGE = registerEntity("water_mage", 0.5F, 1.8F, WaterMage::new, MobCategory.MONSTER);
        AIR_MAGE = registerEntity("air_mage", 0.5F, 1.8F, AirMage::new, MobCategory.MONSTER);
        EARTH_MAGE = registerEntity("earth_mage", 0.5F, 1.8F, EarthMage::new, MobCategory.MONSTER);

        FLASHING_WEALD_WALKER = registerEntity("flashing_weald_walker", 1.4F, 3F,
                (EntityType<WealdWalker> type, Level world) -> {
                    WealdWalker walker = new WealdWalker(type, world);
                    walker.spell = new Spell(MethodProjectile.INSTANCE, EffectLaunch.INSTANCE, EffectLaunch.INSTANCE, EffectDelay.INSTANCE, EffectWindshear.INSTANCE);
                    walker.color = new ParticleColor(200, 150, 15);
                    return walker;
                }, MobCategory.CREATURE);

        SKELEHORSE_SUMMON = addEntity("summon_skelehorse", 1.4F, 1.6F, true, true, SummonSkeleHorse::new, MobCategory.CREATURE);
        DIREWOLF_SUMMON = registerEntity("summon_direwolf", 0.9F, 1.0F, SummonDirewolf::new, MobCategory.CREATURE);
        WSKELETON_SUMMON = registerEntity("summon_wskeleton", 1.0F, 1.8F, SummonUndead::new, MobCategory.CREATURE);
        DOLPHIN_SUMMON = addEntity("summon_dolphin", 0.9F, 0.6F, false, true, SummonDolphin::new, MobCategory.WATER_CREATURE);
        STRIDER_SUMMON = addEntity("summon_strider", 0.9F, 1.7F, true, true, SummonStrider::new, MobCategory.CREATURE);

        VHEX_SUMMON = registerEntity("summon_vhex", 0.4F, 0.8F, AllyVhexEntity::new, MobCategory.MONSTER);
        HOMING_PROJECTILE = addEntity("homing_projectile", 0.5F, 0.5F, true, true, EntityHomingProjectile::new, MobCategory.MISC);
        CURVED_PROJECTILE = addEntity("curved_projectile", 0.5F, 0.5F, true, true, EntityCurvedProjectile::new, MobCategory.MISC);
        LINGER_MAGNET = addEntity("linger_magnet", 0.5F, 0.5F, true, true, EntityMagnetSpell::new, MobCategory.MISC);
        LERP_PROJECTILE = addEntity("lerp", 0.5F, 0.5F, true, true, EntityLerpedProjectile::new, MobCategory.MISC);

    }

    static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, float width, float height, EntityType.EntityFactory<T> factory, MobCategory kind) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(factory, kind).setTrackingRange(16).sized(width, height).build(MODID + ":" + name));
    }

    static <T extends Entity> RegistryObject<EntityType<T>> addEntity(String name, float width, float height, boolean fire, boolean noSave, EntityType.EntityFactory<T> factory, MobCategory kind) {
        return ENTITIES.register(name, () -> {
            EntityType.Builder<T> builder = EntityType.Builder.of(factory, kind)
                    .setTrackingRange(32)
                    .sized(width, height);
            if (noSave) {
                builder.noSave();
            }
            if (fire) {
                builder.fireImmune();
            }
            return builder.build(MODID + ":" + name);
        });
    }


    @SubscribeEvent
    public static void registerSP(SpawnPlacementRegisterEvent event) {

        event.register(SIREN_ENTITY.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (p_186238_, p_186239_, p_186240_, p_186241_, p_186242_) -> MermaidEntity.checkSurfaceWaterAnimalSpawnRules(p_186239_, p_186241_), SpawnPlacementRegisterEvent.Operation.OR);
        event.register(FIRE_MAGE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(AIR_MAGE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(WATER_MAGE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EARTH_MAGE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);

    }

}
