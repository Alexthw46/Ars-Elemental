package alexthw.ars_elemental.recipe.jei;

import alexthw.ars_elemental.recipe.ElementalArmorRecipe;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.perk.ArmorPerkHolder;
import com.hollingsworth.arsnouveau.api.perk.IPerkProvider;
import com.hollingsworth.arsnouveau.client.jei.EnchantingApparatusRecipeCategory;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ElementalUpgradeRecipeCategory extends EnchantingApparatusRecipeCategory<ElementalArmorRecipe> {
    public ElementalUpgradeRecipeCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ElementalArmorRecipe recipe, IFocusGroup focuses) {
        MultiProvider provider = multiProvider.apply(recipe);
        List<Ingredient> inputs = provider.input();
        double angleBetweenEach = 360.0 / inputs.size();
        if (provider.optionalCenter() != null) {
            var stacks = provider.optionalCenter().getItems();
            for (ItemStack stack : stacks) {
                IPerkProvider<ItemStack> perkProvider = ArsNouveauAPI.getInstance().getPerkProvider(stack.getItem());
                if (perkProvider != null) {
                    if (perkProvider.getPerkHolder(stack) instanceof ArmorPerkHolder armorPerkHolder) {
                        armorPerkHolder.setTier(2);
                    }
                }
            }
            builder.addSlot(RecipeIngredientRole.INPUT, 48, 45).addItemStacks(List.of(stacks));
        }
        for (Ingredient input : inputs) {
            builder.addSlot(RecipeIngredientRole.INPUT, (int) point.x, (int) point.y).addIngredients(input);
            point = rotatePointAbout(point, center, angleBetweenEach);
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 10).addItemStacks(Collections.singletonList(provider.output()));
    }

    @Override
    public Component getTitle() {
        return Component.translatable("ars_nouveau.armor_upgrade");
    }

    @Override
    public RecipeType<ElementalArmorRecipe> getRecipeType() {
        return JeiArsExtraPlugin.ELEMENTAL_ARMOR_TYPE;
    }

    @Override
    public void draw(ElementalArmorRecipe recipe, @NotNull IRecipeSlotsView slotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        Font renderer = Minecraft.getInstance().font;
        renderer.draw(matrixStack, Component.translatable("ars_nouveau.tier", 1 + recipe.tier), 0.0f, 0.0f, 10);

        if (recipe.consumesSource())
            renderer.draw(matrixStack, Component.translatable("ars_nouveau.source", recipe.sourceCost), 0.0f, 100f, 10);
    }

}
