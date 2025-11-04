package alexthw.ars_elemental.common.items.armor;

import alexthw.ars_elemental.ArsElemental;
import com.alexthw.sauce.event.AttributeEventHandler;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static alexthw.ars_elemental.common.items.armor.ArmorSet.weaknessMap;

public class LightArmorE extends ElementalArmor {

    public LightArmorE(ArmorItem.Type slot, SpellSchool element, Properties builder) {
        super(slot, element, schoolToMaterial(element.getId() + "_light"), builder);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flags) {
        super.appendHoverText(stack, context, tooltip, flags);
        tooltip.add(Component.literal("Set bonus, Model and Texture still work in progress, currently same as medium variant.").withStyle(ChatFormatting.RED));
    }

    @Override
    public String getTier() {
        return "light";
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers(@NotNull ItemStack stack) {
        return super.getDefaultAttributeModifiers(stack)
                .withModifierAdded(AttributeEventHandler.schoolToDefenseAttribute.get(weaknessMap.getOrDefault(this.element, SpellSchools.ELEMENTAL)), new AttributeModifier(ArsElemental.prefix("elemental_weakness_armor_" + this.type.getName()), -50, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()))
                .withModifierAdded(AttributeEventHandler.schoolToDefenseAttribute.get(this.element), new AttributeModifier(ArsElemental.prefix("elemental_defense_armor_" + this.type.getName()), 50, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()))
                .withModifierAdded(AttributeEventHandler.schoolToPowerAttribute.get(this.element), new AttributeModifier(ArsElemental.prefix("elemental_power_armor_" + this.type.getName()), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(this.type.getSlot()));
    }


}
