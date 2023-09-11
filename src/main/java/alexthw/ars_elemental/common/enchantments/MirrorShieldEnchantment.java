package alexthw.ars_elemental.common.enchantments;

import com.hollingsworth.arsnouveau.common.items.EnchantersShield;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class MirrorShieldEnchantment extends Enchantment {

    public MirrorShieldEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.create("magic_shield", EnchantersShield.class::isInstance), new EquipmentSlot[]{EquipmentSlot.OFFHAND});
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinCost(int pEnchantmentLevel) {
        return pEnchantmentLevel * 25;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return this.getMinCost(pEnchantmentLevel) + 50;
    }

    /**
     * Checks if the enchantment should be considered a treasure enchantment. These enchantments can not be obtained
     * using the enchantment table. The mending enchantment is an example of a treasure enchantment.
     * @return Whether the enchantment is a treasure enchantment.
     */
    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    /**
     * @return Whether the enchantment can go in loot tables.
     */
    @Override
    public boolean isDiscoverable() {
        return false;
    }

    /**
     * @return Whether the enchantment can go in villager trades.
     */
    @Override
    public boolean isTradeable() {
        return false;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return 4;
    }

}
