package alexthw.ars_elemental.common.items.armor;

import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import static alexthw.ars_elemental.registry.ModItems.ArmorProp;
import static alexthw.ars_elemental.registry.ModItems.ITEMS;


public class ArmorSet {
    public ArmorSet(String name, SpellSchool element) {
        this.head = ITEMS.register(name + "_hat", () -> new ElementalHat(element, ArmorProp()));
        this.chest = ITEMS.register(name + "_robes", () -> new ElementalArmor(EquipmentSlot.CHEST, element, ArmorProp()));
        this.legs = ITEMS.register(name + "_leggings", () -> new ElementalArmor(EquipmentSlot.LEGS, element, ArmorProp()));
        this.feet = ITEMS.register(name + "_boots", () -> new ElementalArmor(EquipmentSlot.FEET, element, ArmorProp()));
    }

    RegistryObject<Item> head;
    RegistryObject<Item> chest;
    RegistryObject<Item> legs;
    RegistryObject<Item> feet;

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

}
