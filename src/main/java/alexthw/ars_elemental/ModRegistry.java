package alexthw.ars_elemental;

import alexthw.ars_elemental.entity.AllyVhexEntity;
import alexthw.ars_elemental.entity.SummonDirewolf;
import alexthw.ars_elemental.item.ElementalFocus;
import alexthw.ars_elemental.item.NecroticFocus;
import alexthw.ars_elemental.entity.SummonSkeleHorse;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArsElemental.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ArsElemental.MODID);

    public static final RegistryObject<EntityType<SummonSkeleHorse>> SKELEHORSE_SUMMON;
    public static final RegistryObject<EntityType<SummonDirewolf>> DIREWOLF_SUMMON;
    public static final RegistryObject<EntityType<AllyVhexEntity>> VHEX_SUMMON;

    public static final RegistryObject<Item> FIRE_FOCUS;
    public static final RegistryObject<Item> AIR_FOCUS;
    public static final RegistryObject<Item> WATER_FOCUS;
    public static final RegistryObject<Item> EARTH_FOCUS;
    public static final RegistryObject<Item> NECRO_FOCUS;

    public static Item.Properties addTabProps(){
        return new Item.Properties().tab(ArsElemental.TAB);
    }

    static {
        FIRE_FOCUS = ITEMS.register("fire_focus", ()-> new ElementalFocus(addTabProps(), SpellSchools.ELEMENTAL_FIRE));
        WATER_FOCUS = ITEMS.register("water_focus", ()-> new ElementalFocus(addTabProps(), SpellSchools.ELEMENTAL_WATER));
        AIR_FOCUS = ITEMS.register("air_focus", ()-> new ElementalFocus(addTabProps(), SpellSchools.ELEMENTAL_AIR));
        EARTH_FOCUS = ITEMS.register("earth_focus", ()-> new ElementalFocus(addTabProps(), SpellSchools.ELEMENTAL_EARTH));
        NECRO_FOCUS = ITEMS.register("necrotic_focus", () -> new NecroticFocus(addTabProps()));

        SKELEHORSE_SUMMON = ENTITIES.register("summon_skelehorse", () -> EntityType.Builder.of(SummonSkeleHorse::new,EntityClassification.CREATURE).sized(1.3964844F, 1.6F).setTrackingRange(10).build("ars_elemental:" + "summon_skelehorse"));

        DIREWOLF_SUMMON = registerEntity("summon_direwolf", 0.6F, 0.85F, SummonDirewolf::new, EntityClassification.CREATURE);
        VHEX_SUMMON = registerEntity("summon_vhex", 0.4F, 0.8F, AllyVhexEntity::new, EntityClassification.MONSTER);
    }

    static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, float width, float height, EntityType.IFactory<T> factory, EntityClassification kind) {
        EntityType<T> type = EntityType.Builder.of(factory, kind).clientTrackingRange(10).setTrackingRange(64).setUpdateInterval(1).sized(width, height).build("ars_elemental:" + name);
        return ENTITIES.register(name, () -> type);
    }

    public static void registerRegistries(IEventBus bus) {
        ITEMS.register(bus);
        ENTITIES.register(bus);
    }

}
