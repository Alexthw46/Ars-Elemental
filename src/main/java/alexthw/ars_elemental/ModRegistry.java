package alexthw.ars_elemental;

import alexthw.ars_elemental.common.CurioHolderContainer;
import alexthw.ars_elemental.common.entity.*;
import alexthw.ars_elemental.common.mob_effects.EnthrallEffect;
import alexthw.ars_elemental.common.mob_effects.HellFireEffect;
import alexthw.ars_elemental.common.mob_effects.LifeLinkEffect;
import alexthw.ars_elemental.common.mob_effects.WaterGraveEffect;
import alexthw.ars_elemental.common.items.CurioHolder;
import alexthw.ars_elemental.common.items.ElementalFocus;
import alexthw.ars_elemental.common.items.NecroticFocus;
import alexthw.ars_elemental.common.items.SirenCharm;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import alexthw.ars_elemental.common.entity.familiars.MermaidFamiliar;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static alexthw.ars_elemental.ArsElemental.MODID;

public class ModRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);

    static Item.Properties addTabProp() {
        return new Item.Properties().tab(ArsElemental.TAB);
    }
    static Item.Properties FocusProp() {
        return addTabProp().stacksTo(1).fireResistant().rarity(Rarity.RARE);
    }

    public static void registerRegistries(IEventBus bus ){
        BLOCKS.register(bus);
        ITEMS.register(bus);
        ENTITIES.register(bus);
        CONTAINERS.register(bus);
        EFFECTS.register(bus);
    }

    public static final RegistryObject<EntityType<MermaidEntity>> SIREN_ENTITY;
    public static final RegistryObject<EntityType<MermaidFamiliar>> SIREN_FAMILIAR;

    public static final RegistryObject<EntityType<SummonSkeleHorse>> SKELEHORSE_SUMMON;
    public static final RegistryObject<EntityType<SummonDirewolf>> DIREWOLF_SUMMON;
    public static final RegistryObject<EntityType<AllyVhexEntity>> VHEX_SUMMON;
    public static final RegistryObject<EntityType<EntityHomingProjectile>> HOMING_PROJECTILE;

    public static final RegistryObject<MobEffect> HELLFIRE;
    public static final RegistryObject<MobEffect> WATER_GRAVE;
    public static final RegistryObject<MobEffect> ENTHRALLED;
    public static final RegistryObject<LifeLinkEffect> LIFE_LINK;

    public static final RegistryObject<MenuType<CurioHolderContainer>> CURIO_HOLDER;

    public static final RegistryObject<Item> SIREN_CHARM;

    public static final RegistryObject<Item> CURIO_BAG;
    public static final RegistryObject<Item> FIRE_FOCUS;
    public static final RegistryObject<Item> AIR_FOCUS;
    public static final RegistryObject<Item> WATER_FOCUS;
    public static final RegistryObject<Item> EARTH_FOCUS;
    public static final RegistryObject<Item> NECRO_FOCUS;

    static {

        HELLFIRE = EFFECTS.register("hellfire", HellFireEffect::new);
        WATER_GRAVE = EFFECTS.register("watery_grave", WaterGraveEffect::new);
        ENTHRALLED = EFFECTS.register("enthralled", EnthrallEffect::new);
        LIFE_LINK = EFFECTS.register("life_link", LifeLinkEffect::new);

        SIREN_CHARM = ITEMS.register("siren_charm", () -> new SirenCharm(addTabProp()));

        SIREN_ENTITY = registerEntity("siren_entity", 0.4F,0.8F, MermaidEntity::new, MobCategory.WATER_CREATURE );
        SIREN_FAMILIAR = registerEntity("siren_familiar", 0.4F,0.8F, MermaidFamiliar::new, MobCategory.WATER_CREATURE );

        SKELEHORSE_SUMMON = registerEntity("summon_skelehorse",1.3964844F, 1.6F, SummonSkeleHorse::new, MobCategory.CREATURE);
        DIREWOLF_SUMMON = registerEntity("summon_direwolf", 0.6F, 0.85F, SummonDirewolf::new, MobCategory.CREATURE);
        VHEX_SUMMON = registerEntity("summon_vhex", 0.4F, 0.8F, AllyVhexEntity::new, MobCategory.MONSTER);
        HOMING_PROJECTILE = registerEntity("homing_projectile", 0.5F, 0.5F, EntityHomingProjectile::new, MobCategory.MISC);

        CURIO_BAG = ITEMS.register("curio_bag", ()-> new CurioHolder(addTabProp().stacksTo(1)));
        FIRE_FOCUS = ITEMS.register("fire_focus", ()-> new ElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_FIRE));
        WATER_FOCUS = ITEMS.register("water_focus", ()-> new ElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_WATER));
        AIR_FOCUS = ITEMS.register("air_focus", ()-> new ElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_AIR));
        EARTH_FOCUS = ITEMS.register("earth_focus", ()-> new ElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_EARTH));
        NECRO_FOCUS = ITEMS.register("necrotic_focus", () -> new NecroticFocus(FocusProp()));

        CURIO_HOLDER = CONTAINERS.register("curio_holder", () -> IForgeMenuType.create((int id, Inventory inv, FriendlyByteBuf extraData) -> new CurioHolderContainer(id, inv, extraData.readItem())));
    }

    static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, float width, float height, EntityType.EntityFactory<T> factory, MobCategory kind) {
        EntityType<T> type = EntityType.Builder.of(factory, kind).setTrackingRange(64).setUpdateInterval(1).sized(width, height).build(MODID + ":" + name);
        return ENTITIES.register(name, () -> type);
    }

    static <T extends Mob> RegistryObject<EntityType<T>> addEntity(String name, float width, float height, boolean fire, boolean noSave, EntityType.EntityFactory<T> factory, MobCategory kind, int color1, int color2) {
        EntityType.Builder<T> builder = EntityType.Builder.of(factory, kind)
                .setTrackingRange(64)
                .setUpdateInterval(1)
                .sized(width, height);
        if (noSave){
            builder.noSave();
        }
        if (fire) {
            builder.fireImmune();
        }
        EntityType<T> type = builder.build(MODID + ":" + name);

        //ITEMS.register("spawn_" + name, () -> new ForgeSpawnEggItem(() -> type, color1, color2, addTabProp()));
        return ENTITIES.register(name, () -> type);
    }

    static RegistryObject<Block> addBlock(String name, Block block) {
        ITEMS.register(name, () -> new BlockItem(block, addTabProp()));
        return BLOCKS.register(name, () -> block);
    }

    static RegistryObject<Block> addSlab(String name, SlabBlock slab){
        return addBlock(name + "_slab", slab);
    }

    static RegistryObject<Block> addStair(String name, StairBlock stair){
        return addBlock(name + "_stairs", stair);
    }
    static Properties blockProps(Material mat, MaterialColor color) {
        return Properties.of(mat, color);
    }
}
