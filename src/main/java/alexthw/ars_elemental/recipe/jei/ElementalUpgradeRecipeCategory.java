package alexthw.ars_elemental.recipe.jei;

import alexthw.ars_elemental.recipe.ElementalArmorRecipe;
import com.hollingsworth.arsnouveau.client.jei.EnchantingApparatusRecipeCategory;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ElementalUpgradeRecipeCategory extends EnchantingApparatusRecipeCategory<ElementalArmorRecipe> {
    public ElementalUpgradeRecipeCategory(IGuiHelper helper) {
        super(helper);
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
