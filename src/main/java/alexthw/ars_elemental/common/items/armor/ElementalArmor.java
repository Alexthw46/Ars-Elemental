package alexthw.ars_elemental.common.items.armor;

import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.common.armor.MagicArmor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class ElementalArmor extends MagicArmor implements IElementalArmor {

    final SpellSchool element;

    public ElementalArmor(EquipmentSlot slot, SpellSchool element, Properties builder) {
        super(IElementalArmor.schoolToMaterial(element), slot, builder);
        this.element = element;
    }

    public SpellSchool getSchool() {
        return element;
    }

    @Override
    public int getMaxManaBoost(ItemStack i) {
        return 100;
    }

    @Override
    public int getManaRegenBonus(ItemStack i) {
        return 9;
    }

}
