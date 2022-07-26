package alexthw.ars_elemental.common.items.armor;

import alexthw.ars_elemental.client.TooltipUtils;
import alexthw.ars_elemental.registry.ModItems;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.common.armor.MagicArmor;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
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

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flags) {
        TooltipUtils.addOnShift(list, () -> addInformationAfterShift(stack, world, list, flags), "armorset");
    }

    EquipmentSlot[] OrderedSlots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public void addInformationAfterShift(ItemStack stack, Level world, List<Component> list, TooltipFlag flags) {
        Player player = ArsNouveau.proxy.getPlayer();
        if (player != null) {
            ArmorSet set = getArmorSetFromElement(this.element);
            List<Component> equippedList = new ArrayList<>();
            int equippedCounter = 0;
            for (EquipmentSlot slot : OrderedSlots) {
                Item armor = set.getArmorFromSlot(slot);
                MutableComponent cmp = Component.literal(" - ").append(armor.getDefaultInstance().getHoverName());
                cmp.withStyle(hasArmorSetItem(player.getItemBySlot(slot), armor) ? ChatFormatting.GREEN : ChatFormatting.GRAY);
                equippedList.add(cmp);
            }
            list.add(getArmorSetTitle(set, equippedCounter));
            list.addAll(equippedList);
            addArmorSetDescription(set, list);
        }
    }

    private boolean hasArmorSetItem(ItemStack armor, Item armorFromSlot) {
        return armor.getItem() == armorFromSlot;
    }

    ArmorSet getArmorSetFromElement(SpellSchool element) {
        return switch (element.getId()) {
            case "air" -> ModItems.AIR_ARMOR;
            case "fire" -> ModItems.FIRE_ARMOR;
            case "water" -> ModItems.WATER_ARMOR;
            case "earth" -> ModItems.EARTH_ARMOR;
            default -> null;
        };
    }

    private Component getArmorSetTitle(ArmorSet set, int equipped) {
        return Component.translatable(set.getTranslationKey())
                .append(" (" + equipped + " / 4)")
                .withStyle(ChatFormatting.DARK_AQUA);
    }

    public void addArmorSetDescription(ArmorSet set, List<Component> list) {
        list.add(Component.translatable("ars_elemental.armorset." + set.getName() + ".desc").withStyle(ChatFormatting.GRAY));
    }

}
