package alexthw.ars_elemental.common.enchantments;

//public class SoulboundEnchantment extends Enchantment {
//
//    public static final EnchantmentCategory SOULBOUND_CAT = EnchantmentCategory.create("soulbounding", (item -> item.getDefaultInstance().is(ModRegistry.SOULBOUND_ABLE) || item instanceof ShieldItem || item instanceof ICurioItem || item instanceof ICasterTool || item instanceof Equipable || item instanceof TieredItem));
//
//    public SoulboundEnchantment() {
//        super(Enchantment.Rarity.VERY_RARE, SOULBOUND_CAT, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
//    }
//
//    public static final String TAG_SOULBOUND_DROP_COUNT = "tagSoulboundDC";
//    public static final String TAG_SOULBOUND_PREFIX = "tagSoulboundPrefix";
//    public static final String TAG_SOULBOUND = "tagSoulbound";
//
//    @Override
//    public int getMinCost(int enchantmentLevel) {
//        return 0;
//    }
//
//    @Override
//    public int getMaxCost(int enchantmentLevel) {
//        return 0;
//    }
//
//    @Override
//    public boolean canEnchant(ItemStack stack) {
//        return true;
//    }
//
//    @Override
//    public boolean canApplyAtEnchantingTable(ItemStack stack) {
//        return false;
//    }
//
//    @Override
//    public boolean isTradeable() {
//        return false;
//    }
//
//    @Override
//    public boolean isAllowedOnBooks() {
//        return true;
//    }
//
//    @Override
//    public boolean isTreasureOnly() {
//        return true;
//    }
//
//    @Override
//    public boolean isDiscoverable() {
//        return ConfigHandler.Common.SOULBOUND_LOOT.get();
//    }
//
//}
