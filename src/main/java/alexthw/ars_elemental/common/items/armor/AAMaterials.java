package alexthw.ars_elemental.common.items.armor;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AAMaterials {

    public final static ModArmorMaterial sourceSteel = new ModArmorMaterial("ars_arsenal:sourcesteel", 25, new int[]{4, 7, 9, 4}
            , 40, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0f, () -> Ingredient.of(Items.NETHERITE_SCRAP));
    public final static ModArmorMaterial fire = new ModArmorMaterial("ars_arsenal:fire", 33, new int[]{3, 6, 8, 3}
            , 30, SoundEvents.GENERIC_BURN, 3.0f, () -> Ingredient.of(Items.GHAST_TEAR));
    public final static ModArmorMaterial water = new ModArmorMaterial("ars_arsenal:water", 33, new int[]{3, 6, 8, 3}
            , 30, SoundEvents.BREWING_STAND_BREW, 3.0f, () -> Ingredient.of(Items.ICE));
    public final static ModArmorMaterial earth = new ModArmorMaterial("ars_arsenal:earth", 33, new int[]{3, 6, 8, 3}
            , 30, SoundEvents.ANVIL_PLACE, 3.0f, () -> Ingredient.of(Items.IRON_BLOCK));
    public final static ModArmorMaterial air = new ModArmorMaterial("ars_arsenal:air", 33, new int[]{3, 6, 8, 3}
            , 30, SoundEvents.ARMOR_EQUIP_ELYTRA, 3.0f, () -> Ingredient.of(Items.FEATHER));


    /* TODO PR to make public
    Stealing this because mojang bad and Bailey did it better.  Bailey make yours public pls.
     */
    private static class ModArmorMaterial implements ArmorMaterial {

        private static final int[] Max_Damage_Array = new int[]{13, 15, 16, 11};
        private final String name;
        private final int maxDamageFactor;
        private final int[] damageReductionAmountArray;
        private final int enchantability;

        public ModArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, Supplier<Ingredient> supplier) {
            this.name = name;
            this.maxDamageFactor = maxDamageFactor;
            this.damageReductionAmountArray = damageReductionAmountArray;
            this.enchantability = enchantability;
            this.soundEvent = soundEvent;
            this.toughness = toughness;
            this.repairMaterial = new LazyLoadedValue<>(supplier);
        }

        private final SoundEvent soundEvent;
        private final float toughness;
        private final LazyLoadedValue<Ingredient> repairMaterial;


        @Override
        public int getDurabilityForSlot(EquipmentSlot slotIn) {
            return Max_Damage_Array[slotIn.getIndex()] * maxDamageFactor;
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot slotIn) {
            return damageReductionAmountArray[slotIn.getIndex()];
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
            return repairMaterial.get();
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
            return 0;
        }
    }


}