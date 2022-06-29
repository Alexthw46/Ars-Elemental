package alexthw.ars_elemental.common.items.armor;

import com.hollingsworth.arsnouveau.common.armor.Materials.ModdedArmorMaterial;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class AAMaterials {

    public final static ModdedArmorMaterial sourceSteel = new ModdedArmorMaterial("ars_arsenal:sourcesteel", 25, new int[]{4, 7, 9, 4}
            , 40, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0f, () -> Ingredient.of(Items.NETHERITE_SCRAP));
    public final static ModdedArmorMaterial fire = new ModdedArmorMaterial("ars_arsenal:fire", 33, new int[]{3, 6, 8, 3}
            , 30, SoundEvents.GENERIC_BURN, 3.0f, () -> Ingredient.of(Items.GHAST_TEAR));
    public final static ModdedArmorMaterial water = new ModdedArmorMaterial("ars_arsenal:water", 33, new int[]{3, 6, 8, 3}
            , 30, SoundEvents.BREWING_STAND_BREW, 3.0f, () -> Ingredient.of(Items.ICE));
    public final static ModdedArmorMaterial earth = new ModdedArmorMaterial("ars_arsenal:earth", 33, new int[]{3, 6, 8, 3}
            , 30, SoundEvents.ANVIL_PLACE, 3.0f, () -> Ingredient.of(Items.IRON_BLOCK));
    public final static ModdedArmorMaterial air = new ModdedArmorMaterial("ars_arsenal:air", 33, new int[]{3, 6, 8, 3}
            , 30, SoundEvents.ARMOR_EQUIP_ELYTRA, 3.0f, () -> Ingredient.of(Items.FEATHER));
}