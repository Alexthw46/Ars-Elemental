package alexthw.ars_elemental.registry;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.ArsNouveauRegistry;
import alexthw.ars_elemental.common.blocks.UpstreamTile;
import alexthw.ars_elemental.common.blocks.mermaid_block.MermaidTile;
import alexthw.ars_elemental.common.entity.EntityCurvedProjectile;
import alexthw.ars_elemental.common.entity.EntityHomingProjectile;
import alexthw.ars_elemental.common.entity.FirenandoEntity;
import alexthw.ars_elemental.common.entity.MermaidEntity;
import alexthw.ars_elemental.common.entity.familiars.FirenandoFamiliar;
import alexthw.ars_elemental.common.entity.familiars.MermaidFamiliar;
import alexthw.ars_elemental.common.entity.mages.*;
import alexthw.ars_elemental.common.entity.summon.AllyVhexEntity;
import alexthw.ars_elemental.common.entity.summon.SummonDirewolf;
import alexthw.ars_elemental.common.entity.summon.SummonSkeleHorse;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import static alexthw.ars_elemental.ArsElemental.MODID;
import static alexthw.ars_elemental.registry.ModItems.MERMAID_ROCK;
import static alexthw.ars_elemental.registry.ModItems.UPSTREAM_BLOCK;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);

    public static final RegistryObject<EntityType<MermaidEntity>> SIREN_ENTITY;
    public static final RegistryObject<EntityType<MermaidFamiliar>> SIREN_FAMILIAR;
    public static final RegistryObject<EntityType<FirenandoEntity>> FIRENANDO_ENTITY;
    public static final RegistryObject<EntityType<FirenandoFamiliar>> FIRENANDO_FAMILIAR;
    public static final RegistryObject<EntityType<EntityMageBase>> FIRE_MAGE;
    public static final RegistryObject<EntityType<EntityMageBase>> WATER_MAGE;
    public static final RegistryObject<EntityType<EntityMageBase>> AIR_MAGE;
    public static final RegistryObject<EntityType<EntityMageBase>> EARTH_MAGE;

    public static final RegistryObject<EntityType<SummonSkeleHorse>> SKELEHORSE_SUMMON;
    public static final RegistryObject<EntityType<SummonDirewolf>> DIREWOLF_SUMMON;
    public static final RegistryObject<EntityType<AllyVhexEntity>> VHEX_SUMMON;
    public static final RegistryObject<EntityType<EntityHomingProjectile>> HOMING_PROJECTILE;
    public static final RegistryObject<EntityType<EntityCurvedProjectile>> CURVED_PROJECTILE;

    public static BlockEntityType<MermaidTile> MERMAID_TILE;
    public static BlockEntityType<UpstreamTile> UPSTREAM_TILE;

    static {
        SIREN_ENTITY = registerEntity("siren_entity", 0.4F, 1.4F, MermaidEntity::new, MobCategory.WATER_CREATURE);
        SIREN_FAMILIAR = registerEntity("siren_familiar", 0.4F, 1.4F, MermaidFamiliar::new, MobCategory.WATER_CREATURE);

        FIRENANDO_ENTITY = addEntity("firenando_entity", 1.0F, 2.2F, true, false, FirenandoEntity::new, MobCategory.CREATURE);
        FIRENANDO_FAMILIAR = addEntity("firenando_familiar", 1.0F, 2.2F, true, false, FirenandoFamiliar::new, MobCategory.CREATURE);

        FIRE_MAGE = registerEntity("fire_mage", 0.5F, 1.8F, FireMage::new, MobCategory.MONSTER);
        WATER_MAGE = registerEntity("water_mage", 0.5F, 1.8F, WaterMage::new, MobCategory.MONSTER);
        AIR_MAGE = registerEntity("air_mage", 0.5F, 1.8F, AirMage::new, MobCategory.MONSTER);
        EARTH_MAGE = registerEntity("earth_mage", 0.5F, 1.8F, EarthMage::new, MobCategory.MONSTER);

        SKELEHORSE_SUMMON = addEntity("summon_skelehorse", 1.4F, 1.6F, true, true, SummonSkeleHorse::new, MobCategory.CREATURE);
        DIREWOLF_SUMMON = registerEntity("summon_direwolf", 0.9F, 1.0F, SummonDirewolf::new, MobCategory.CREATURE);
        VHEX_SUMMON = registerEntity("summon_vhex", 0.4F, 0.8F, AllyVhexEntity::new, MobCategory.MONSTER);
        HOMING_PROJECTILE = addEntity("homing_projectile", 0.5F, 0.5F, true, true, EntityHomingProjectile::new, MobCategory.MISC);
        CURVED_PROJECTILE = addEntity("curved_projectile", 0.5F, 0.5F, true, true, EntityCurvedProjectile::new, MobCategory.MISC);
    }

    static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, float width, float height, EntityType.EntityFactory<T> factory, MobCategory kind) {
        EntityType<T> type = EntityType.Builder.of(factory, kind).setTrackingRange(64).setUpdateInterval(1).sized(width, height).build(MODID + ":" + name);
        return ENTITIES.register(name, () -> type);
    }

    static <T extends Entity> RegistryObject<EntityType<T>> addEntity(String name, float width, float height, boolean fire, boolean noSave, EntityType.EntityFactory<T> factory, MobCategory kind) {
        EntityType.Builder<T> builder = EntityType.Builder.of(factory, kind)
                .setTrackingRange(64)
                .setUpdateInterval(1)
                .sized(width, height);
        if (noSave) {
            builder.noSave();
        }
        if (fire) {
            builder.fireImmune();
        }
        EntityType<T> type = builder.build(MODID + ":" + name);
        return ENTITIES.register(name, () -> type);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
        SpawnPlacements.register(FIRE_MAGE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);

        ArsNouveauRegistry.addLights();
    }


    @SubscribeEvent
    public static void registerTiles(RegistryEvent.Register<BlockEntityType<?>> evt) {
        MERMAID_TILE = addTileEntity(evt.getRegistry(), "mermaid_tile", MermaidTile::new, MERMAID_ROCK.get());
        UPSTREAM_TILE = addTileEntity(evt.getRegistry(), "upstream_tile", UpstreamTile::new, UPSTREAM_BLOCK.get());
    }

    @SuppressWarnings("ConstantConditions")
    static <T extends BlockEntity> BlockEntityType<T> addTileEntity(IForgeRegistry<BlockEntityType<?>> registry, String name, BlockEntityType.BlockEntitySupplier<T> factory, Block... blocks) {
        BlockEntityType<T> type = BlockEntityType.Builder.of(factory, blocks).build(null);
        type.setRegistryName(ArsElemental.MODID, name);
        registry.register(type);
        return type;
    }

}
