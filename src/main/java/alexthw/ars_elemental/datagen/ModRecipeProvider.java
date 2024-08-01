package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.common.rituals.*;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.registry.RitualRegistry;
import com.hollingsworth.arsnouveau.common.datagen.RecipeDatagen;
import com.hollingsworth.arsnouveau.common.lib.RitualLib;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static alexthw.ars_elemental.ArsElemental.prefix;
import static alexthw.ars_elemental.registry.ModItems.*;


public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator pGenerator, CompletableFuture<HolderLookup.Provider> provider) {
        super(pGenerator.getPackOutput(), provider);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput consumer) {

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ADVANCED_PRISM.get())
                .define('P', BlockRegistry.SPELL_PRISM.asItem())
                .define('Q', Items.QUARTZ)
                .define('S', RecipeDatagen.SOURCE_GEM)
                .pattern("QSQ").pattern("SPS").pattern("QSQ")
                .unlockedBy("has_journal", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsRegistry.WORN_NOTEBOOK))
                .save(consumer);
        shapelessBuilder(GROUND_BLOSSOM.get()).requires(Blocks.SPORE_BLOSSOM).save(consumer);
        shapelessBuilder(Blocks.SPORE_BLOSSOM).requires(GROUND_BLOSSOM.get()).save(consumer, prefix("alt_blossom"));

        RecipeDatagen.makeWood(FLASHING_ARCHWOOD_LOG.get(), FLASHING_ARCHWOOD.get(), 3).save(consumer);
        strippedLogToWood(consumer, FLASHING_ARCHWOOD_LOG_STRIPPED.get(), FLASHING_ARCHWOOD_STRIPPED.get());

        shapelessBuilder(getRitualItem(ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, RitualLib.FLIGHT)))
                .requires(FLASHING_ARCHWOOD_LOG.get())
                .requires(ItemsRegistry.WILDEN_WING, 1)
                .requires(Ingredient.of(Tags.Items.GEMS_DIAMOND), 2)
                .requires(Items.FEATHER)
                .save(consumer, prefix("flight_alt"));

        shapelessBuilder(getRitualItem(prefix(TeslaRitual.ID)))
                .requires(FLASHING_ARCHWOOD_LOG.get())
                .requires(ItemsRegistry.AIR_ESSENCE)
                .requires(Ingredient.of(Tags.Items.GEMS_DIAMOND), 2)
                .requires(Items.LIGHTNING_ROD)
                .requires(RecipeDatagen.SOURCE_GEM_BLOCK)
                .save(consumer, prefix("tablet_" + TeslaRitual.ID));


        shapelessBuilder(getRitualItem(prefix(SquirrelRitual.ID)))
                .requires(FLASHING_ARCHWOOD_LOG.get())
                .requires(ItemsRegistry.STARBUNCLE_SHARD)
                .requires(Items.SUGAR)
                .requires(Items.RABBIT_FOOT)
                .save(consumer, prefix("tablet_" + SquirrelRitual.ID));

        shapelessBuilder(getRitualItem(prefix(DetectionRitual.ID)))
                .requires(FLASHING_ARCHWOOD_LOG.get())
                .requires(Items.SPIDER_EYE, 2)
                .requires(Items.GLOWSTONE_DUST)
                .requires(RecipeDatagen.SOURCE_GEM_BLOCK)
                .save(consumer, prefix("tablet_" + DetectionRitual.ID));

        shapelessBuilder(getRitualItem(prefix(AttractionRitual.ID)))
                .requires(BlockRegistry.FLOURISHING_LOG)
                .requires(Ingredient.of(Tags.Items.INGOTS_IRON), 2)
                .requires(ItemsRegistry.EARTH_ESSENCE)
                .save(consumer, prefix("tablet_" + AttractionRitual.ID));

        shapelessBuilder(getRitualItem(prefix(RepulsionRitual.ID)))
                .requires(FLASHING_ARCHWOOD_LOG.get())
                .requires(ItemsRegistry.AIR_ESSENCE, 2)
                .requires(Blocks.PISTON, 1)
                .save(consumer, prefix("tablet_" + RepulsionRitual.ID));

    }

    public Item getRitualItem(ResourceLocation id) {
        return RitualRegistry.getRitualItemMap().get(id);
    }

    public ShapelessRecipeBuilder shapelessBuilder(ItemLike result) {
        return shapelessBuilder(result, 1);
    }

    public ShapelessRecipeBuilder shapelessBuilder(ItemLike result, int resultCount) {
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,result, resultCount).unlockedBy("has_journal", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsRegistry.WORN_NOTEBOOK));
    }

    private static void strippedLogToWood(RecipeOutput recipeConsumer, ItemLike stripped, ItemLike output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,output, 3).define('#', stripped).pattern("##").pattern("##").group("bark")
                .unlockedBy("has_journal", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsRegistry.WORN_NOTEBOOK))
                .save(recipeConsumer);
    }

}