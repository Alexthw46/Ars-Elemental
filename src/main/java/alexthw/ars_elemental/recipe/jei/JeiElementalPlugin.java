package alexthw.ars_elemental.recipe.jei;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.recipe.NetheriteUpgradeRecipe;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static mezz.jei.api.recipe.RecipeType.createFromDeferredVanilla;

@JeiPlugin
public class JeiElementalPlugin implements IModPlugin {

    public static final Supplier<RecipeType<RecipeHolder<NetheriteUpgradeRecipe>>> SPELLBOOK_NETHERITE_TYPE = createFromDeferredVanilla(ModRegistry.NETHERITE_UP);

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
        registry.addRecipes(SPELLBOOK_NETHERITE_TYPE.get(), manager.getAllRecipesFor(ModRegistry.NETHERITE_UP.get()).stream().toList());

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(BlockRegistry.ENCHANTING_APP_BLOCK), SPELLBOOK_NETHERITE_TYPE.get());
    }

}


