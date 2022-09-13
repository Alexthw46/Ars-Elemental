package alexthw.ars_elemental.common.enchantments;

import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Wearable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class SoulboundEnchantment extends Enchantment {

    public static final EnchantmentCategory SOULBOUND_CAT = EnchantmentCategory.create("soulbounding", (item -> item.getDefaultInstance().is(ModRegistry.SOULBOUND_ABLE) || item instanceof ShieldItem || item instanceof ICurioItem || item instanceof ICasterTool || item instanceof Wearable || item instanceof TieredItem));

    public SoulboundEnchantment() {
        super(Enchantment.Rarity.VERY_RARE, SOULBOUND_CAT, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    public static final String TAG_SOULBOUND_DROP_COUNT = "tagSoulboundDC";
    public static final String TAG_SOULBOUND_PREFIX = "tagSoulboundPrefix";
    public static final String TAG_SOULBOUND = "tagSoulbound";

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 0;
    }

    @Override
    public int getMaxCost(int enchantmentLevel) {
        return 0;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

}
