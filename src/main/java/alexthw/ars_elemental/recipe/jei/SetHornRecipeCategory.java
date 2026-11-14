package alexthw.ars_elemental.recipe.jei;

import alexthw.ars_elemental.recipe.SetHornRecipe;
import alexthw.ars_elemental.registry.ModItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.tags.InstrumentTags.GOAT_HORNS;

public class SetHornRecipeCategory implements ICraftingCategoryExtension<SetHornRecipe> {


    public SetHornRecipeCategory() {
    }

    @Override
    public void setRecipe(RecipeHolder<SetHornRecipe> recipeHolder, @NotNull IRecipeLayoutBuilder builder, @NotNull ICraftingGridHelper craftingGridHelper, @NotNull IFocusGroup focuses) {
        var recipe = recipeHolder.value();
        List<List<ItemStack>> inputs = new ArrayList<>(recipe.getIngredients().stream()
                .map(ingredient -> List.of(ingredient.getItems()))
                .toList());
        ItemStack resultItem = recipe.getResultItem(Minecraft.getInstance().level.registryAccess());
        List<ItemStack> results = new ArrayList<>();


        if (resultItem.getItem() == ModItems.SPELL_HORN.get()) {
            var focus = focuses.getItemStackFocuses(RecipeIngredientRole.INPUT)
                    .map(f -> f.getTypedValue().getIngredient())
                    .filter(f -> f.getItem() == Items.GOAT_HORN)
                    .toList();

            if (!focus.isEmpty()) {
                inputs.set(0, focus);
                ItemStack copy = resultItem.copy();
                copy.set(DataComponents.INSTRUMENT, focus.getFirst().get(DataComponents.INSTRUMENT));
                results.add(copy);
            } else {
                var horn_sounds = BuiltInRegistries.INSTRUMENT.getTagOrEmpty(GOAT_HORNS);
                var horns = new ArrayList<ItemStack>();
                for (var sound : horn_sounds) {
                    ItemStack copy = resultItem.copy();
                    ItemStack horn = Items.GOAT_HORN.getDefaultInstance();
                    horn.set(DataComponents.INSTRUMENT, sound);
                    horns.add(horn);
                    copy.set(DataComponents.INSTRUMENT, sound);
                    results.add(copy);
                }
                inputs.set(0, horns);
            }
        }

        craftingGridHelper.createAndSetOutputs(builder, results);
        craftingGridHelper.createAndSetInputs(builder, inputs, 0, 0);
    }

}