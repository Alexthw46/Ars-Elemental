package alexthw.ars_elemental.common.items.armor;

import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Map;

import static alexthw.ars_elemental.registry.ModItems.ArmorProp;
import static alexthw.ars_elemental.registry.ModItems.ITEMS;


public abstract class ArmorSet {
    public String getName() {
        return name;
    }

    public static final Map<SpellSchool, SpellSchool> weaknessMap = Map.of(
            SpellSchools.ELEMENTAL_FIRE, SpellSchools.ELEMENTAL_WATER,
            SpellSchools.ELEMENTAL_WATER, SpellSchools.ELEMENTAL_AIR,
            SpellSchools.ELEMENTAL_EARTH, SpellSchools.ELEMENTAL_FIRE,
            SpellSchools.ELEMENTAL_AIR, SpellSchools.ELEMENTAL_EARTH
    );
    public static final Map<SpellSchool, SpellSchool> resistanceMap = Map.of(
            SpellSchools.ELEMENTAL_WATER, SpellSchools.ELEMENTAL_FIRE,
            SpellSchools.ELEMENTAL_EARTH, SpellSchools.ELEMENTAL_AIR,
            SpellSchools.ELEMENTAL_AIR, SpellSchools.ELEMENTAL_WATER,
            SpellSchools.ELEMENTAL_FIRE, SpellSchools.ELEMENTAL_EARTH
    );
    protected String name;
    protected DeferredHolder<Item, ElementalArmor> head;
    protected DeferredHolder<Item, ElementalArmor> chest;
    protected DeferredHolder<Item, ElementalArmor> legs;
    protected DeferredHolder<Item, ElementalArmor> feet;

    public static class Light extends ArmorSet {
        public Light(String name, SpellSchool element) {
            this.name = name;
            this.head = ITEMS.register(name + "_hood", () -> new LightArmorE(ArmorItem.Type.HELMET, element, ArmorProp()));
            this.chest = ITEMS.register(name + "_tunic", () -> new LightArmorE(ArmorItem.Type.CHESTPLATE, element, ArmorProp()));
            this.legs = ITEMS.register(name + "_pants", () -> new LightArmorE(ArmorItem.Type.LEGGINGS, element, ArmorProp()));
            this.feet = ITEMS.register(name + "_shoes", () -> new LightArmorE(ArmorItem.Type.BOOTS, element, ArmorProp()));
        }

        public String getTranslationKey() {
            return super.getTranslationKey() + "_light";
        }

    }

    public static class Medium extends ArmorSet {
        public Medium(String name, SpellSchool element) {
            this.name = name;
            this.head = ITEMS.register(name + "_hat", () -> new MediumArmorE(ArmorItem.Type.HELMET, element, ArmorProp()));
            this.chest = ITEMS.register(name + "_robes", () -> new MediumArmorE(ArmorItem.Type.CHESTPLATE, element, ArmorProp()));
            this.legs = ITEMS.register(name + "_leggings", () -> new MediumArmorE(ArmorItem.Type.LEGGINGS, element, ArmorProp()));
            this.feet = ITEMS.register(name + "_boots", () -> new MediumArmorE(ArmorItem.Type.BOOTS, element, ArmorProp()));
        }
    }

    public static class Heavy extends ArmorSet {
        public Heavy(String name, SpellSchool element) {
            this.name = name;
            this.head = ITEMS.register(name + "_helmet", () -> new HeavyArmorE(ArmorItem.Type.HELMET, element, ArmorProp()));
            this.chest = ITEMS.register(name + "_chestplate", () -> new HeavyArmorE(ArmorItem.Type.CHESTPLATE, element, ArmorProp()));
            this.legs = ITEMS.register(name + "_leggings_heavy", () -> new HeavyArmorE(ArmorItem.Type.LEGGINGS, element, ArmorProp()));
            this.feet = ITEMS.register(name + "_boots_heavy", () -> new HeavyArmorE(ArmorItem.Type.BOOTS, element, ArmorProp()));
        }

        public String getTranslationKey() {
            return super.getTranslationKey() + "_heavy";
        }
    }

    public Item getHat() {
        return head.get();
    }

    public Item getChest() {
        return chest.get();
    }

    public Item getLegs() {
        return legs.get();
    }

    public Item getBoots() {
        return feet.get();
    }

    public Item getArmorFromSlot(EquipmentSlot slot) {
        return switch (slot) {
            case CHEST -> getChest();
            case LEGS -> getLegs();
            case FEET -> getBoots();
            default -> getHat();
        };
    }

    public String getTranslationKey() {
        return "ars_elemental.armor_set." + this.name;
    }
}

