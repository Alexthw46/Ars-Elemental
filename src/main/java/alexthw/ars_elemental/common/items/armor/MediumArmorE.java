package alexthw.ars_elemental.common.items.armor;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.ConfigHandler;
import com.alexthw.sauce.event.AttributeEventHandler;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;

import static alexthw.ars_elemental.common.items.armor.ArmorSet.weaknessMap;

public class MediumArmorE extends ElementalArmor {

    public MediumArmorE(ArmorItem.Type slot, SpellSchool element, Properties builder) {
        super(slot, element, schoolToMaterial(element.getId()), builder);
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers(@NotNull ItemStack stack) {
        if (FMLEnvironment.production && !ConfigHandler.Startup.ENABLE_ARMOR_REWORK.get())
            return super.getDefaultAttributeModifiers(stack);
        return super.getDefaultAttributeModifiers(stack)
                .withModifierAdded(AttributeEventHandler.schoolToDefenseAttribute.get(weaknessMap.getOrDefault(this.element, SpellSchools.ELEMENTAL)), new AttributeModifier(ArsElemental.prefix("elemental_weakness_armor_" + this.type.getName()), -25, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()))
                .withModifierAdded(AttributeEventHandler.schoolToDefenseAttribute.get(this.element), new AttributeModifier(ArsElemental.prefix("elemental_defense_armor_" + this.type.getName()), 75, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()))
                .withModifierAdded(AttributeEventHandler.schoolToPowerAttribute.get(this.element), new AttributeModifier(ArsElemental.prefix("elemental_power_armor_" + this.type.getName()), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
    }

}
