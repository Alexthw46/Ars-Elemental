package alexthw.ars_elemental.common.items.armor;

import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import net.minecraft.world.entity.EquipmentSlot;

public class HeavyArmorE extends ElementalArmor {

    public HeavyArmorE(EquipmentSlot slot, SpellSchool element, Properties builder) {
        super(slot, element, builder);
    }

    @Override
    public String getTier() {
        return "heavy";
    }
}
