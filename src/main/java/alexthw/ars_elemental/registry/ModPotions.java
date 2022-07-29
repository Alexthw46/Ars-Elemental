package alexthw.ars_elemental.registry;

import alexthw.ars_elemental.common.mob_effects.*;
import com.hollingsworth.arsnouveau.api.recipe.PotionIngredient;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static alexthw.ars_elemental.ArsElemental.MODID;
import static com.hollingsworth.arsnouveau.common.lib.LibPotions.longPotion;
import static com.hollingsworth.arsnouveau.common.lib.LibPotions.potion;
import static com.hollingsworth.arsnouveau.common.potions.ModPotions.SHOCKED_EFFECT;

public class ModPotions {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, MODID);

    public static final RegistryObject<MobEffect> HELLFIRE;
    public static final RegistryObject<MobEffect> WATER_GRAVE;
    public static final RegistryObject<MobEffect> ENTHRALLED;
    public static final RegistryObject<MobEffect> HYMN_OF_ORDER;
    public static final RegistryObject<LifeLinkEffect> LIFE_LINK;
    public static final RegistryObject<EnderferenceEffect> ENDERFERENCE;

    public static final RegistryObject<Potion> ENDERFERENCE_POTION;
    public static final RegistryObject<Potion> LONG_ENDERFERENCE_POTION;

    public static final RegistryObject<Potion> SHOCK_POTION;
    public static final RegistryObject<Potion> LONG_SHOCK_POTION;


    public static void addPotionRecipes() {
        ItemStack AWKWARD = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD);

        ItemStack enderPot = PotionUtils.setPotion(new ItemStack(Items.POTION), ENDERFERENCE_POTION.get());
        ItemStack enderPotLong = PotionUtils.setPotion(new ItemStack(Items.POTION), LONG_ENDERFERENCE_POTION.get());

        ItemStack shockPot = PotionUtils.setPotion(new ItemStack(Items.POTION), SHOCK_POTION.get());
        ItemStack shockPotLong = PotionUtils.setPotion(new ItemStack(Items.POTION), LONG_SHOCK_POTION.get());

        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(new PotionIngredient(AWKWARD), Ingredient.of(ItemsRegistry.END_FIBER), enderPot));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(new PotionIngredient(enderPot), Ingredient.of(Items.GLOWSTONE_DUST), enderPotLong));

        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(new PotionIngredient(AWKWARD), Ingredient.of(ModItems.FLASHING_POD.get().asItem()), shockPot));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(new PotionIngredient(shockPot), Ingredient.of(Items.GLOWSTONE_DUST), shockPotLong));

    }

    static {
        HELLFIRE = EFFECTS.register("hellfire", HellFireEffect::new);
        WATER_GRAVE = EFFECTS.register("watery_grave", WaterGraveEffect::new);
        ENTHRALLED = EFFECTS.register("enthralled", EnthrallEffect::new);
        LIFE_LINK = EFFECTS.register("life_link", LifeLinkEffect::new);
        HYMN_OF_ORDER = EFFECTS.register("hymn_of_order", OrderEffect::new);
        ENDERFERENCE = EFFECTS.register("enderference", EnderferenceEffect::new);

        ENDERFERENCE_POTION = POTIONS.register(potion("enderference"), () -> new Potion(new MobEffectInstance(ENDERFERENCE.get(), 400)));
        LONG_ENDERFERENCE_POTION = POTIONS.register(longPotion("enderference"), () -> new Potion(new MobEffectInstance(ENDERFERENCE.get(), 1200)));
        SHOCK_POTION = POTIONS.register(potion("shock"), () -> new Potion(new MobEffectInstance(SHOCKED_EFFECT.get(), 600, 1)));
        LONG_SHOCK_POTION = POTIONS.register(longPotion("shock"), () -> new Potion(new MobEffectInstance(SHOCKED_EFFECT.get(), 2400, 1)));

    }

}
