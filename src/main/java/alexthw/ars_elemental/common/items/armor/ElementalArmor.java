package alexthw.ars_elemental.common.items.armor;

import alexthw.ars_elemental.api.item.IElementalArmor;
import alexthw.ars_elemental.client.TooltipUtils;
import alexthw.ars_elemental.registry.ModItems;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.perk.*;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.util.PerkUtil;
import com.hollingsworth.arsnouveau.common.armor.AnimatedMagicArmor;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static alexthw.ars_elemental.ConfigHandler.Common.ARMOR_MANA_REGEN;
import static alexthw.ars_elemental.ConfigHandler.Common.ARMOR_MAX_MANA;


public class ElementalArmor extends AnimatedMagicArmor implements IElementalArmor {

    final SpellSchool element;

    public ElementalArmor(EquipmentSlot slot, SpellSchool element, Properties builder) {
        super(IElementalArmor.schoolToMaterial(element), slot, builder);
        this.element = element;
    }

    @Override
    public int getMinTier() {
        return 2;
    }

    public SpellSchool getSchool() {
        return element;
    }

    public String getTier() {
        return "medium";
    }

    @Override
    public int getManaDiscount(ItemStack i, Spell spell) {
        return Mth.ceil(getDiscount(spell.recipe));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        IPerkProvider<ItemStack> perkProvider = ArsNouveauAPI.getInstance().getPerkProvider(stack.getItem());
        if (perkProvider != null) {
            tooltip.add(Component.translatable("ars_nouveau.tier", 4).withStyle(ChatFormatting.GOLD));
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
            //check if the player have all the armor pieces of the set. Color the text green if they do, gray if they don't
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
            //add the tooltip for the armor set and the list of equipped armor pieces, then add the description
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

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot pEquipmentSlot, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> attributes = new ImmutableMultimap.Builder<>();
        attributes.putAll(super.getDefaultAttributeModifiers(pEquipmentSlot));
        if (this.slot == pEquipmentSlot) {
            UUID uuid = getModifierForSlot(this.slot);
            IPerkHolder<ItemStack> perkHolder = PerkUtil.getPerkHolder(stack);
            if (perkHolder != null) {
                attributes.put(PerkAttributes.FLAT_MANA_BONUS.get(), new AttributeModifier(uuid, "max_mana_armor", ARMOR_MAX_MANA.get(), AttributeModifier.Operation.ADDITION));
                attributes.put(PerkAttributes.MANA_REGEN_BONUS.get(), new AttributeModifier(uuid, "mana_regen_armor", ARMOR_MANA_REGEN.get(), AttributeModifier.Operation.ADDITION));
                for (PerkInstance perkInstance : perkHolder.getPerkInstances()) {
                    IPerk perk = perkInstance.getPerk();
                    attributes.putAll(perk.getModifiers(this.slot, stack, perkInstance.getSlot().value));
                }

            }
        }
        return attributes.build();
    }

}
