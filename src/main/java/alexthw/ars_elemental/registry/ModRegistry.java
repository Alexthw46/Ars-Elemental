package alexthw.ars_elemental.registry;

import alexthw.ars_elemental.common.CurioHolderContainer;
import alexthw.ars_elemental.common.enchantments.MirrorShieldEnchantment;
import alexthw.ars_elemental.common.mob_effects.EnthrallEffect;
import alexthw.ars_elemental.common.mob_effects.HellFireEffect;
import alexthw.ars_elemental.common.mob_effects.LifeLinkEffect;
import alexthw.ars_elemental.common.mob_effects.WaterGraveEffect;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static alexthw.ars_elemental.ArsElemental.MODID;
import static alexthw.ars_elemental.ArsElemental.prefix;
import static alexthw.ars_elemental.registry.ModEntities.ENTITIES;
import static alexthw.ars_elemental.registry.ModEntities.TILES;
import static alexthw.ars_elemental.registry.ModItems.BLOCKS;
import static alexthw.ars_elemental.registry.ModItems.ITEMS;
import static alexthw.ars_elemental.world.ModFeatures.FEATURES;

public class ModRegistry {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MODID);

    public static final TagKey<Item> CURIO_BAGGABLE = ItemTags.create(prefix("curio_bag_item"));

    public static void registerRegistries(IEventBus bus){
        BLOCKS.register(bus);
        ITEMS.register(bus);
        ENTITIES.register(bus);
        TILES.register(bus);
        CONTAINERS.register(bus);
        EFFECTS.register(bus);
        ENCHANTMENTS.register(bus);
        FEATURES.register(bus);
    }

    public static final RegistryObject<MobEffect> HELLFIRE;
    public static final RegistryObject<MobEffect> WATER_GRAVE;
    public static final RegistryObject<MobEffect> ENTHRALLED;
    public static final RegistryObject<LifeLinkEffect> LIFE_LINK;

    public static final RegistryObject<MenuType<CurioHolderContainer>> CURIO_HOLDER;

    public static final RegistryObject<Enchantment> MIRROR;

    static {
        HELLFIRE = EFFECTS.register("hellfire", HellFireEffect::new);
        WATER_GRAVE = EFFECTS.register("watery_grave", WaterGraveEffect::new);
        ENTHRALLED = EFFECTS.register("enthralled", EnthrallEffect::new);
        LIFE_LINK = EFFECTS.register("life_link", LifeLinkEffect::new);

        CURIO_HOLDER = CONTAINERS.register("curio_holder", () -> IForgeMenuType.create((int id, Inventory inv, FriendlyByteBuf extraData) -> new CurioHolderContainer(id, inv, extraData.readItem())));

        MIRROR = ENCHANTMENTS.register("mirror_shield", MirrorShieldEnchantment::new);

    }

}
