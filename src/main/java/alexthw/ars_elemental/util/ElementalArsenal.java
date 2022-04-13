package alexthw.ars_elemental.util;

import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static com.minttea.minecraft.arsarsenal.setup.registries.ItemRegistry.*;

public class ElementalArsenal {

    public static ItemStack getArmorForSlot(EquipmentSlot slot, SpellSchool school) {
        Item item = switch (slot) {
            case HEAD -> getHatFromElement(school);
            case CHEST -> getChestFromElement(school);
            case LEGS -> getLegsFromElement(school);
            case FEET -> getBootsFromElement(school);
            default -> null;
        };

        if (item == null) return ItemStack.EMPTY;

        return item.getDefaultInstance();
    }

    private static Item getBootsFromElement(SpellSchool school) {
        return switch (school.getId()) {
            case "fire" -> fireBoots;
            case "water" -> aquaBoots;
            case "earth" -> earthBoots;
            case "air" -> airBoots;
            default -> null;
        };
    }

    private static Item getLegsFromElement(SpellSchool school) {
        return switch (school.getId()) {
            case "fire" -> fireLegs;
            case "water" -> aquaLegs;
            case "earth" -> earthLegs;
            case "air" -> airLegs;
            default -> null;
        };
    }

    private static Item getChestFromElement(SpellSchool school) {
        return switch (school.getId()) {
            case "fire" -> fireRobe;
            case "water" -> aquaRobe;
            case "earth" -> earthRobe;
            case "air" -> airRobe;
            default -> null;
        };
    }

    private static Item getHatFromElement(SpellSchool school) {
        return switch (school.getId()) {
            case "fire" -> fireHat;
            case "water" -> aquaHat;
            case "earth" -> earthHat;
            case "air" -> airHat;
            default -> null;
        };
    }


}
