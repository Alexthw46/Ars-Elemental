package alexthw.ars_elemental.common.items.armor;

import alexthw.ars_elemental.api.item.IElementalArmor;
import alexthw.ars_elemental.client.TooltipUtils;
import alexthw.ars_elemental.registry.ModItems;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.perk.IPerkProvider;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.common.armor.AnimatedMagicArmor;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;


public class ElementalArmor extends AnimatedMagicArmor implements IElementalArmor {

    final SpellSchool element;

    public ElementalArmor(EquipmentSlot slot, SpellSchool element, Properties builder) {
        super(IElementalArmor.schoolToMaterial(element), slot, builder);
        this.element = element;
    }

    public SpellSchool getSchool() {
        return element;
    }

    public String getTier() {
        return "medium";
    }

    @Override
    public void inventoryTick(ItemStack stack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(stack, pLevel, pEntity, pSlotId, pIsSelected);
        IPerkProvider<ItemStack> perkProvider = ArsNouveauAPI.getInstance().getPerkProvider(stack.getItem());
        if (perkProvider != null) {
            //TODO Remove after transition is complete - serverside
            if (perkProvider.getPerkHolder(stack).getTier() != 2) perkProvider.getPerkHolder(stack).setTier(2);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        IPerkProvider<ItemStack> perkProvider = ArsNouveauAPI.getInstance().getPerkProvider(stack.getItem());
        if (perkProvider != null) {
            //TODO Remove after transition is complete - clientside
            if (perkProvider.getPerkHolder(stack).getTier() != 2) perkProvider.getPerkHolder(stack).setTier(2);
            perkProvider.getPerkHolder(stack).appendPerkTooltip(tooltip, stack);
        }
        TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, world, tooltip, flags), "armor_set");
    }

    EquipmentSlot[] OrderedSlots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    @OnlyIn(Dist.CLIENT)
    public void addInformationAfterShift(ItemStack stack, Level world, List<Component> list, TooltipFlag flags) {
        Player player = ArsNouveau.proxy.getPlayer();
        if (player != null) {
            ArmorSet set = getArmorSetFromElement(this.element);
            List<Component> equippedList = new ArrayList<>();
            int equippedCounter = 0;
            for (EquipmentSlot slot : OrderedSlots) {
                Item armor = set.getArmorFromSlot(slot);
                MutableComponent cmp = Component.literal(" - ").append(armor.getDefaultInstance().getHoverName());
                if (hasArmorSetItem(player.getItemBySlot(slot), armor)) {
                    cmp.withStyle(ChatFormatting.GREEN);
                    equippedCounter++;
                } else cmp.withStyle(ChatFormatting.GRAY);

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
        list.add(Component.translatable("ars_elemental.armor_set." + set.getName() + ".desc").withStyle(ChatFormatting.GRAY));
    }

}
