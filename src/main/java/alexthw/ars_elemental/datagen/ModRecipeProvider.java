package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.common.rituals.SquirrelRitual;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.common.datagen.Recipes;
import com.hollingsworth.arsnouveau.common.lib.RitualLib;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static alexthw.ars_elemental.ArsElemental.prefix;
import static alexthw.ars_elemental.registry.ModItems.*;


public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {

        Recipes.makeWood(FLASHING_ARCHWOOD_LOG.get(), FLASHING_ARCHWOOD.get(), 3).save(consumer);
        strippedLogToWood(consumer, FLASHING_ARCHWOOD_LOG_STRIPPED.get(), FLASHING_ARCHWOOD_STRIPPED.get());

        shapelessBuilder(getRitualItem(RitualLib.SCRYING))
                .requires(FLASHING_ARCHWOOD_LOG.get())
                .requires(Items.SPIDER_EYE, 2)
                .requires(Items.GLOWSTONE_DUST)
                .requires(Recipes.SOURCE_GEM_BLOCK)
                .save(consumer, prefix("scry_alt"));

        shapelessBuilder(getRitualItem(RitualLib.FLIGHT))
                .requires(FLASHING_ARCHWOOD_LOG.get())
                .requires(ItemsRegistry.WILDEN_WING, 1)
                .requires(Ingredient.of(Tags.Items.GEMS_DIAMOND), 2)
                .requires(Items.FEATHER)
                .save(consumer, prefix("flight_alt"));

        /*
        shapelessBuilder(getRitualItem(TeslaRitual.ID))
                .requires(FLASHING_ARCHWOOD_LOG.get())
                .requires(ItemsRegistry.AIR_ESSENCE)
                .requires(Ingredient.of(Tags.Items.GEMS_DIAMOND), 2)
                .requires(Items.LIGHTNING_ROD)
                .requires(Recipes.SOURCE_GEM_BLOCK)
                .save(consumer, prefix("ritual_" + TeslaRitual.ID));

         */

        shapelessBuilder(getRitualItem(SquirrelRitual.ID))
                .requires(FLASHING_ARCHWOOD_LOG.get())
                .requires(ItemsRegistry.STARBUNCLE_SHARD)
                .requires(Items.SUGAR)
                .requires(Items.RABBIT_FOOT)
                .save(consumer, prefix("ritual_" + SquirrelRitual.ID));

    }

    public Item getRitualItem(String id) {
        return ArsNouveauAPI.getInstance().getRitualItemMap().get(id);
    }

    public ShapelessRecipeBuilder shapelessBuilder(ItemLike result) {
        return shapelessBuilder(result, 1);
    }

    public ShapelessRecipeBuilder shapelessBuilder(ItemLike result, int resultCount) {
        return ShapelessRecipeBuilder.shapeless(result, resultCount).unlockedBy("has_journal", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsRegistry.WORN_NOTEBOOK));
    }

    private static void strippedLogToWood(Consumer<FinishedRecipe> recipeConsumer, ItemLike stripped, ItemLike output) {
        ShapedRecipeBuilder.shaped(output, 3).define('#', stripped).pattern("##").pattern("##").group("bark")
                .unlockedBy("has_journal", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsRegistry.WORN_NOTEBOOK))
                .save(recipeConsumer);
    }

}