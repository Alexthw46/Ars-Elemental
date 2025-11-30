package alexthw.ars_elemental.common.items.armor;

import alexthw.ars_elemental.ArsElemental;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

import static alexthw.ars_elemental.ArsElemental.MODID;

public class AAMaterials {

    public static final DeferredRegister<ArmorMaterial> A_MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, MODID);

    public static final EnumMap<ArmorItem.Type, Integer> ARMOR_SLOT_PROTECTION_L = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 2);
        map.put(ArmorItem.Type.LEGGINGS, 5);
        map.put(ArmorItem.Type.CHESTPLATE, 6);
        map.put(ArmorItem.Type.HELMET, 2);
        map.put(ArmorItem.Type.BODY, 4);
    });

    public static final EnumMap<ArmorItem.Type, Integer> ARMOR_SLOT_PROTECTION_M = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 3);
        map.put(ArmorItem.Type.LEGGINGS, 6);
        map.put(ArmorItem.Type.CHESTPLATE, 8);
        map.put(ArmorItem.Type.HELMET, 3);
        map.put(ArmorItem.Type.BODY, 4);
    });

    public static final EnumMap<ArmorItem.Type, Integer> ARMOR_SLOT_PROTECTION_H = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 4);
        map.put(ArmorItem.Type.LEGGINGS, 7);
        map.put(ArmorItem.Type.CHESTPLATE, 10);
        map.put(ArmorItem.Type.HELMET, 4);
        map.put(ArmorItem.Type.BODY, 4);
    });

    public final static Holder<ArmorMaterial> l_fire = A_MATERIALS.register("light_fire", () -> new ArmorMaterial(ARMOR_SLOT_PROTECTION_L
            , 50, new Holder.Direct<>(SoundEvents.GENERIC_BURN), () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(ArsElemental.prefix("light_fire"))), 1.0f, 0));
    public final static Holder<ArmorMaterial> l_water = A_MATERIALS.register("light_water", () -> new ArmorMaterial(ARMOR_SLOT_PROTECTION_L
            , 50, new Holder.Direct<>(SoundEvents.BREWING_STAND_BREW), () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(ArsElemental.prefix("light_water"))), 1.0f, 0));
    public final static Holder<ArmorMaterial> l_earth = A_MATERIALS.register("light_earth", () -> new ArmorMaterial(ARMOR_SLOT_PROTECTION_L, 50, new Holder.Direct<>(SoundEvents.GLASS_PLACE), () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(ArsElemental.prefix("light_earth"))), 1.0f, 0));
    public final static Holder<ArmorMaterial> l_air = A_MATERIALS.register("light_air", () -> new ArmorMaterial(ARMOR_SLOT_PROTECTION_L, 50, SoundEvents.ARMOR_EQUIP_ELYTRA, () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(ArsElemental.prefix("light_air"))), 1.0f, 0));

    public final static Holder<ArmorMaterial> fire = A_MATERIALS.register("medium_fire", () -> new ArmorMaterial(ARMOR_SLOT_PROTECTION_M
            , 40, new Holder.Direct<>(SoundEvents.GENERIC_BURN), () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(ArsElemental.prefix("medium_fire"))), 2.0f, 0.25F));
    public final static Holder<ArmorMaterial> water = A_MATERIALS.register("medium_water", () -> new ArmorMaterial(ARMOR_SLOT_PROTECTION_M
            , 40, new Holder.Direct<>(SoundEvents.BREWING_STAND_BREW), () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(ArsElemental.prefix("medium_water"))), 2.0f, 0.025F));
    public final static Holder<ArmorMaterial> earth = A_MATERIALS.register("medium_earth", () -> new ArmorMaterial(ARMOR_SLOT_PROTECTION_M, 40, new Holder.Direct<>(SoundEvents.GLASS_PLACE), () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(ArsElemental.prefix("medium_earth"))), 2.0f, 0.05F));
    public final static Holder<ArmorMaterial> air = A_MATERIALS.register("medium_air", () -> new ArmorMaterial(ARMOR_SLOT_PROTECTION_M, 40, SoundEvents.ARMOR_EQUIP_ELYTRA, () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(ArsElemental.prefix("medium_air"))), 2.0f, 0.025F));

    public final static Holder<ArmorMaterial> h_fire = A_MATERIALS.register("heavy_fire", () -> new ArmorMaterial(ARMOR_SLOT_PROTECTION_H
            , 30, new Holder.Direct<>(SoundEvents.GENERIC_BURN), () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(ArsElemental.prefix("heavy_fire"))), 4.0f, 0.05F));
    public final static Holder<ArmorMaterial> h_water = A_MATERIALS.register("heavy_water", () -> new ArmorMaterial(ARMOR_SLOT_PROTECTION_H
            , 30, new Holder.Direct<>(SoundEvents.BREWING_STAND_BREW), () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(ArsElemental.prefix("heavy_water"))), 4.0f, 0.05F));
    public final static Holder<ArmorMaterial> h_earth = A_MATERIALS.register("heavy_earth", () -> new ArmorMaterial(ARMOR_SLOT_PROTECTION_H, 30, new Holder.Direct<>(SoundEvents.GLASS_PLACE), () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(ArsElemental.prefix("heavy_earth"))), 4.0f, 0.1F));
    public final static Holder<ArmorMaterial> h_air = A_MATERIALS.register("heavy_air", () -> new ArmorMaterial(ARMOR_SLOT_PROTECTION_H, 30, SoundEvents.ARMOR_EQUIP_ELYTRA, () -> Ingredient.EMPTY, List.of(new ArmorMaterial.Layer(ArsElemental.prefix("heavy_air"))), 4.0f, 0.05F));

}