package alexthw.ars_elemental.common.items.armor;

import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.common.armor.MagicArmor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ElementalArmor extends MagicArmor implements IElementalArmor {

    public static final Map<SpellSchool, List<DamageSource>> damageResistances = new ConcurrentHashMap<>();

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
