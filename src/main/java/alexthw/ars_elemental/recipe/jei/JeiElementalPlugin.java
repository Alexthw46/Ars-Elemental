package alexthw.ars_elemental.recipe.jei;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.recipe.NetheriteUpgradeRecipe;
import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

@JeiPlugin
public class JeiElementalPlugin implements IModPlugin {

    public static final RecipeType<NetheriteUpgradeRecipe> SPELLBOOK_NETHERITE_TYPE = RecipeType.create(ArsElemental.MODID, "netherite_upgrade", NetheriteUpgradeRecipe.class);

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(ArsElemental.MODID, "main");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new SpellBookUpgradeRecipeCategory(registry.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registry) {
        assert Minecraft.getInstance().level != null;
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
        registry.addRecipes(SPELLBOOK_NETHERITE_TYPE, manager.getAllRecipesFor(ModRegistry.NETHERITE_UP.get()).stream().map(RecipeHolder::value).toList());

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(BlockRegistry.ENCHANTING_APP_BLOCK), SPELLBOOK_NETHERITE_TYPE);
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        if (!ConfigHandler.Startup.ENABLE_ARMOR_REWORK.get()) {
            IIngredientManager ingredientManager = jeiRuntime.getJeiHelpers().getIngredientManager();
            ingredientManager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, Stream.of(
                    // heavy armors
                    ModItems.AIR_ARMOR_H.getHat(),
                    ModItems.AIR_ARMOR_H.getChest(),
                    ModItems.AIR_ARMOR_H.getLegs(),
                    ModItems.AIR_ARMOR_H.getBoots(),
                    ModItems.EARTH_ARMOR_H.getHat(),
                    ModItems.EARTH_ARMOR_H.getChest(),
                    ModItems.EARTH_ARMOR_H.getLegs(),
                    ModItems.EARTH_ARMOR_H.getBoots(),
                    ModItems.FIRE_ARMOR_H.getHat(),
                    ModItems.FIRE_ARMOR_H.getChest(),
                    ModItems.FIRE_ARMOR_H.getLegs(),
                    ModItems.FIRE_ARMOR_H.getBoots(),
                    ModItems.WATER_ARMOR_H.getHat(),
                    ModItems.WATER_ARMOR_H.getChest(),
                    ModItems.WATER_ARMOR_H.getLegs(),
                    ModItems.WATER_ARMOR_H.getBoots(),
                    // light armors
                    ModItems.AIR_ARMOR_L.getHat(),
                    ModItems.AIR_ARMOR_L.getChest(),
                    ModItems.AIR_ARMOR_L.getLegs(),
                    ModItems.AIR_ARMOR_L.getBoots(),
                    ModItems.EARTH_ARMOR_L.getHat(),
                    ModItems.EARTH_ARMOR_L.getChest(),
                    ModItems.EARTH_ARMOR_L.getLegs(),
                    ModItems.EARTH_ARMOR_L.getBoots(),
                    ModItems.FIRE_ARMOR_L.getHat(),
                    ModItems.FIRE_ARMOR_L.getChest(),
                    ModItems.FIRE_ARMOR_L.getLegs(),
                    ModItems.FIRE_ARMOR_L.getBoots(),
                    ModItems.WATER_ARMOR_L.getHat(),
                    ModItems.WATER_ARMOR_L.getChest(),
                    ModItems.WATER_ARMOR_L.getLegs(),
                    ModItems.WATER_ARMOR_L.getBoots()
            ).map(Item::getDefaultInstance).toList());
        }
    }

}


