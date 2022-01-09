package alexthw.ars_elemental;

import alexthw.ars_elemental.items.ElementalFocus;
import alexthw.ars_elemental.items.NecroticFocus;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import entity.AllyVhexEntity;
import entity.MermaidEntity;
import entity.SummonDirewolf;
import entity.SummonSkeleHorse;
import entity.familiars.MermaidFamiliar;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArsElemental.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ArsElemental.MODID);

    static Item.Properties addTabProp() {
        return new Item.Properties().tab(ArsElemental.TAB);
    }

    public static void registerRegistries(IEventBus bus ){
        ITEMS.register(bus);
        ENTITIES.register(bus);
    }

    public static final RegistryObject<EntityType<MermaidEntity>> SIREN_ENTITY;
    public static final RegistryObject<EntityType<MermaidFamiliar>> SIREN_FAMILIAR;

    public static final RegistryObject<EntityType<SummonSkeleHorse>> SKELEHORSE_SUMMON;
    public static final RegistryObject<EntityType<SummonDirewolf>> DIREWOLF_SUMMON;
    public static final RegistryObject<EntityType<AllyVhexEntity>> VHEX_SUMMON;

    public static RegistryObject<Item> SIREN_CHARM;

    public static final RegistryObject<Item> FIRE_FOCUS;
    public static final RegistryObject<Item> AIR_FOCUS;
    public static final RegistryObject<Item> WATER_FOCUS;
    public static final RegistryObject<Item> EARTH_FOCUS;
    public static final RegistryObject<Item> NECRO_FOCUS;

    static {

        //SIREN_CHARM = ITEMS.register("siren_charm", () -> new Item(addTabProp()));

        SIREN_ENTITY = addEntity("siren_entity", 500, 800,0.4F,0.8F, false, MermaidEntity::new, MobCategory.WATER_CREATURE );
        SIREN_FAMILIAR = registerEntity("siren_familiar", 500, 800, MermaidFamiliar::new, MobCategory.WATER_CREATURE );

        SKELEHORSE_SUMMON = registerEntity("summon_skelehorse",1.3964844F, 1.6F, SummonSkeleHorse::new, MobCategory.CREATURE);

        DIREWOLF_SUMMON = registerEntity("summon_direwolf", 0.6F, 0.85F, SummonDirewolf::new, MobCategory.CREATURE);
        VHEX_SUMMON = registerEntity("summon_vhex", 0.4F, 0.8F, AllyVhexEntity::new, MobCategory.MONSTER);


        FIRE_FOCUS = ITEMS.register("fire_focus", ()-> new ElementalFocus(addTabProp(), SpellSchools.ELEMENTAL_FIRE));
        WATER_FOCUS = ITEMS.register("water_focus", ()-> new ElementalFocus(addTabProp(), SpellSchools.ELEMENTAL_WATER));
        AIR_FOCUS = ITEMS.register("air_focus", ()-> new ElementalFocus(addTabProp(), SpellSchools.ELEMENTAL_AIR));
        EARTH_FOCUS = ITEMS.register("earth_focus", ()-> new ElementalFocus(addTabProp(), SpellSchools.ELEMENTAL_EARTH));
        NECRO_FOCUS = ITEMS.register("necrotic_focus", () -> new NecroticFocus(addTabProp()));


    }

    static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, float width, float height, EntityType.EntityFactory<T> factory, MobCategory kind) {
        EntityType<T> type = EntityType.Builder.of(factory, kind).setTrackingRange(64).setUpdateInterval(1).sized(width, height).build("hexblades:" + name);
        return ENTITIES.register(name, () -> type);
    }

    static <T extends Mob> RegistryObject<EntityType<T>> addEntity(String name, int color1, int color2, float width, float height, boolean fire, EntityType.EntityFactory<T> factory, MobCategory kind) {
        EntityType<T> type;
        if (fire) {
            type = EntityType.Builder.of(factory, kind)
                    .setTrackingRange(64)
                    .setUpdateInterval(1)
                    .sized(width, height)
                    .fireImmune()
                    .build(ArsElemental.MODID + ":" + name);
        } else {
            type = EntityType.Builder.of(factory, kind)
                    .setTrackingRange(64)
                    .setUpdateInterval(1)
                    .sized(width, height)
                    .build(ArsElemental.MODID + ":" + name);
        }
        //ITEMS.register("spawn_" + name, () -> new ForgeSpawnEggItem(() -> type, color1, color2, addTabProp()));
        return ENTITIES.register(name, () -> type);
    }



}
