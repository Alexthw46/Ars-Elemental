package alexthw.ars_elemental.common.items.armor;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class AAMaterials {

    public final static ModdedArmorMaterial fire = new ModdedArmorMaterial("ars_elemental:fire", 33, new int[]{3, 6, 8, 3}
            , 40, SoundEvents.GENERIC_BURN, 2.0f, 0);
    public final static ModdedArmorMaterial water = new ModdedArmorMaterial("ars_elemental:water", 33, new int[]{3, 6, 8, 3}
            , 40, SoundEvents.BREWING_STAND_BREW, 2.0f, 0);
    public final static ModdedArmorMaterial earth = new ModdedArmorMaterial("ars_elemental:earth", 33, new int[]{3, 6, 8, 3}
            , 40, SoundEvents.ANVIL_PLACE, 2.0f, 0.02F);
    public final static ModdedArmorMaterial air = new ModdedArmorMaterial("ars_elemental:air", 33, new int[]{3, 6, 8, 3}
            , 40, SoundEvents.ARMOR_EQUIP_ELYTRA, 2.0f, 0);

    public static class ModdedArmorMaterial implements ArmorMaterial {

        private static final int[] Max_Damage_Array = new int[]{15, 17, 18, 13};
        private final String name;
        private final int maxDamageFactor;
        private final int[] damageReductionAmountArray;
        private final int enchantability;

        public ModdedArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, float knockback) {
            this.name = name;
            this.maxDamageFactor = maxDamageFactor;
            this.damageReductionAmountArray = damageReductionAmountArray;
            this.enchantability = enchantability;
            this.soundEvent = soundEvent;
            this.toughness = toughness;
            this.knockback = knockback;
        }

        private final SoundEvent soundEvent;
        private final float toughness;
        private final float knockback;

        @Override
        public int getDurabilityForType(ArmorItem.Type slotIn) {
            return Max_Damage_Array[slotIn.getSlot().getIndex()] * maxDamageFactor;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type slotIn) {
            return damageReductionAmountArray[slotIn.getSlot().getIndex()];
        }

        @Override
        public int getEnchantmentValue() {
            return enchantability;
        }

        @Override
        public @NotNull SoundEvent getEquipSound() {
            return soundEvent;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public @NotNull String getName() {
            return name;
        }

        @Override
        public float getToughness() {
            return toughness;
        }

        @Override
        public float getKnockbackResistance() {
            return knockback;
        }
    }

}