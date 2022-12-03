package alexthw.ars_elemental.registry;

import alexthw.ars_elemental.common.CasterHolderContainer;
import alexthw.ars_elemental.common.CurioHolderContainer;
import alexthw.ars_elemental.common.enchantments.MirrorShieldEnchantment;
import alexthw.ars_elemental.common.enchantments.SoulboundEnchantment;
import alexthw.ars_elemental.recipe.NetheriteUpgradeRecipe;
import alexthw.ars_elemental.util.SupplierBlockStateProviderAE;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static alexthw.ars_elemental.ArsElemental.MODID;
import static alexthw.ars_elemental.ArsElemental.prefix;
import static alexthw.ars_elemental.registry.ModEntities.ENTITIES;
import static alexthw.ars_elemental.registry.ModItems.BLOCKS;
import static alexthw.ars_elemental.registry.ModItems.ITEMS;
import static alexthw.ars_elemental.registry.ModPotions.EFFECTS;
import static alexthw.ars_elemental.registry.ModPotions.POTIONS;
import static alexthw.ars_elemental.registry.ModTiles.TILES;
import static alexthw.ars_elemental.world.ModFeatures.FEATURES;

public class ModRegistry {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MODID);
    public static final DeferredRegister<BlockStateProviderType<?>> BS_PROVIDERS = DeferredRegister.create(ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES, MODID);

    public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

    public static final TagKey<Item> CURIO_BAGGABLE = ItemTags.create(prefix("curio_bag_item"));
    public static final TagKey<Item> SOULBOUND_ABLE = ItemTags.create(prefix("soulbound_extra"));

    public static final TagKey<EntityType<?>> AQUATIC = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, prefix("aquatic"));
    public static final TagKey<EntityType<?>> FIERY = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, prefix("fiery"));
    public static final TagKey<EntityType<?>> AERIAL = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, prefix("aerial"));
    public static final TagKey<EntityType<?>> INSECT = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, prefix("insect"));
    public static final TagKey<EntityType<?>> UNDEAD = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, prefix("undead"));


    public static void registerRegistries(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        ENTITIES.register(bus);
        TILES.register(bus);
        CONTAINERS.register(bus);
        EFFECTS.register(bus);
        POTIONS.register(bus);
        ENCHANTMENTS.register(bus);
        RECIPES.register(bus);
        SERIALIZERS.register(bus);
        FEATURES.register(bus);
        BS_PROVIDERS.register(bus);
    }

    public static final RegistryObject<MenuType<CurioHolderContainer>> CURIO_HOLDER;
    public static final RegistryObject<MenuType<CasterHolderContainer>> CASTER_HOLDER;


    public static final RegistryObject<BlockStateProviderType<?>> AE_BLOCKSTATE_PROVIDER;

    public static final RegistryObject<Enchantment> MIRROR;
    public static final RegistryObject<Enchantment> SOULBOUND;

    public static final RegistryObject<RecipeType<NetheriteUpgradeRecipe>> NETHERITE_UP;
    public static final RegistryObject<RecipeSerializer<NetheriteUpgradeRecipe>> NETHERITE_UP_SERIALIZER;


    static {

        CURIO_HOLDER = CONTAINERS.register("curio_holder", () -> IForgeMenuType.create((int id, Inventory inv, FriendlyByteBuf extraData) -> new CurioHolderContainer(id, inv, extraData.readItem())));
        CASTER_HOLDER = CONTAINERS.register("caster_holder", () -> IForgeMenuType.create((int id, Inventory inv, FriendlyByteBuf extraData) -> new CasterHolderContainer(id, inv, extraData.readItem())));

        AE_BLOCKSTATE_PROVIDER = BS_PROVIDERS.register("ae_stateprovider", () -> new BlockStateProviderType<>(SupplierBlockStateProviderAE.CODEC));

        MIRROR = ENCHANTMENTS.register("mirror_shield", MirrorShieldEnchantment::new);
        SOULBOUND = ENCHANTMENTS.register("soulbound", SoulboundEnchantment::new);

        NETHERITE_UP = RECIPES.register("netherite_upgrade", () -> RecipeType.simple(prefix("netherite_upgrade")));
        NETHERITE_UP_SERIALIZER = SERIALIZERS.register("netherite_upgrade", NetheriteUpgradeRecipe.Serializer::new);

    }

}
