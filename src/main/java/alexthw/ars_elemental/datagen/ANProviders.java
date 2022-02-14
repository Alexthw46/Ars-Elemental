package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.ModRegistry;
import alexthw.ars_elemental.common.glyphs.EffectConjureDirt;
import alexthw.ars_elemental.common.glyphs.EffectWaterGrave;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.IEnchantingRecipe;
import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.crafting.recipes.GlyphRecipe;
import com.hollingsworth.arsnouveau.common.crafting.recipes.ImbuementRecipe;
import com.hollingsworth.arsnouveau.common.datagen.ApparatusRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.GlyphRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.ImbuementRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.Recipes;
import com.hollingsworth.arsnouveau.common.datagen.patchouli.*;
import com.hollingsworth.arsnouveau.common.enchantment.EnchantmentRegistry;
import com.hollingsworth.arsnouveau.setup.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ANProviders {

    public static class GlyphProvider extends GlyphRecipeProvider {

        public GlyphProvider(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        public void run(HashCache cache) throws IOException {

            Path output = this.generator.getOutputFolder();

            recipes.add(get(EffectConjureDirt.INSTANCE).withItem(ItemsRegistry.EARTH_ESSENCE).withItem(Items.DIRT));
            recipes.add(get(EffectWaterGrave.INSTANCE).withItem(Items.KELP).withItem(Items.PRISMARINE_SHARD).withItem(ItemsRegistry.WATER_ESSENCE));

            for(GlyphRecipe recipe : recipes){
                Path path = getScribeGlyphPath(output,  recipe.output.getItem());
                DataProvider.save(GSON, cache, recipe.asRecipe(), path);
            }

        }

        @Override
        public String getName() {
            return "Ars Elemental Glyph Recipes";
        }
    }

    public static class EnchantingAppProvider extends ApparatusRecipeProvider{

        List<EnchantingApparatusRecipe> recipes = new ArrayList<>();

        public EnchantingAppProvider(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        public void run(HashCache cache) throws IOException {

            recipes.add(builder()
                    .withResult(ModRegistry.NECRO_FOCUS.get())
                    .withReagent(ItemsRegistry.SUMMONING_FOCUS)
                    .withPedestalItem(2, Items.WITHER_ROSE)
                    .withPedestalItem(1, Items.WITHER_SKELETON_SKULL)
                    .withPedestalItem(1, ItemsRegistry.CONJURATION_ESSENCE)
                    .build()
            );

            Path output = this.generator.getOutputFolder();
            for (EnchantingApparatusRecipe g : recipes){
                if (g != null){
                    Path path = getRecipePath(output, g.getId().getPath());
                    DataProvider.save(GSON, cache, g.asRecipe(), path);
                }
            }

        }

        protected static Path getRecipePath(Path pathIn, String str){
            return pathIn.resolve("data/ars_elemental/recipes/" + str + ".json");
        }

        @Override
        public String getName() {
            return "Ars Elemental Apparatus";
        }
    }

    public static class ImbuementProvider extends ImbuementRecipeProvider{

        public ImbuementProvider(DataGenerator generatorIn){
            super(generatorIn);
        }

        @Override
        public void run(HashCache cache) throws IOException {

            recipes.add(new ImbuementRecipe("fire_focus", Ingredient.of(Items.AMETHYST_SHARD), new ItemStack(ModRegistry.FIRE_FOCUS.get(), 1),5000)
                    .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                    .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                    .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                    .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                    .withPedestalItem(ItemsRegistry.WILDEN_TRIBUTE)
            );
            recipes.add(new ImbuementRecipe("water_focus", Ingredient.of(Items.AMETHYST_SHARD), new ItemStack(ModRegistry.WATER_FOCUS.get(), 1),5000)
                    .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                    .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                    .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                    .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                    .withPedestalItem(ItemsRegistry.WILDEN_TRIBUTE)
            );
            recipes.add(new ImbuementRecipe("earth_focus", Ingredient.of(Items.AMETHYST_SHARD), new ItemStack(ModRegistry.EARTH_FOCUS.get(), 1),5000)
                    .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                    .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                    .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                    .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                    .withPedestalItem(ItemsRegistry.WILDEN_TRIBUTE)
            );
            recipes.add(new ImbuementRecipe("air_focus", Ingredient.of(Items.AMETHYST_SHARD), new ItemStack(ModRegistry.AIR_FOCUS.get(), 1),5000)
                    .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                    .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                    .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                    .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                    .withPedestalItem(ItemsRegistry.WILDEN_TRIBUTE)
            );

            Path output = generator.getOutputFolder();
            for(ImbuementRecipe g : recipes){
                Path path = getRecipePath(output, g.getId().getPath());
                DataProvider.save(GSON, cache,  g.asRecipe(), path);
            }

        }

        protected Path getRecipePath(Path pathIn, String str){
            return pathIn.resolve("data/ars_elemental/recipes/" + str + ".json");
        }

        @Override
        public String getName() {
            return "Ars Elemental Imbuement";
        }

    }

    public static class PatchouliProvider extends com.hollingsworth.arsnouveau.common.datagen.PatchouliProvider {

        public PatchouliProvider(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        public void run(HashCache cache) throws IOException {

            addGlyphPage(EffectConjureDirt.INSTANCE);
            addGlyphPage(EffectWaterGrave.INSTANCE);

            addBasicItem(ModRegistry.CURIO_BAG.get(), EQUIPMENT, new CraftingPage(ModRegistry.CURIO_BAG.get()));
            addPage(new PatchouliBuilder(EQUIPMENT, ModRegistry.NECRO_FOCUS.get())
                            .withIcon(ModRegistry.NECRO_FOCUS.get())
                            .withTextPage("ars_elemental.page1.necrotic_focus")
                            .withPage(new ApparatusPage(ModRegistry.NECRO_FOCUS.get()))
                            .withTextPage("ars_elemental.page2.necrotic_focus")
                    ,getPath(EQUIPMENT,"necrotic_focus")
            );

            addPage(new PatchouliBuilder(EQUIPMENT, ModRegistry.FIRE_FOCUS.get())
                            .withIcon(ModRegistry.FIRE_FOCUS.get())
                            .withTextPage("ars_elemental.page1.fire_focus")
                            .withPage(ImbuementPage(ModRegistry.FIRE_FOCUS.get()))
                            .withTextPage("ars_elemental.page2.fire_focus")
                    ,getPath(EQUIPMENT,"fire_focus")
            );
            addPage(new PatchouliBuilder(EQUIPMENT, ModRegistry.WATER_FOCUS.get())
                            .withIcon(ModRegistry.WATER_FOCUS.get())
                            .withTextPage("ars_elemental.page1.water_focus")
                            .withPage(ImbuementPage(ModRegistry.WATER_FOCUS.get()))
                            .withTextPage("ars_elemental.page2.water_focus")
                    ,getPath(EQUIPMENT,"water_focus")
            );
            addPage(new PatchouliBuilder(EQUIPMENT, ModRegistry.AIR_FOCUS.get())
                            .withIcon(ModRegistry.AIR_FOCUS.get())
                            .withTextPage("ars_elemental.page1.air_focus")
                            .withPage(ImbuementPage(ModRegistry.AIR_FOCUS.get()))
                            .withTextPage("ars_elemental.page2.air_focus")
                    ,getPath(EQUIPMENT,"air_focus")
            );
            addPage(new PatchouliBuilder(EQUIPMENT, ModRegistry.EARTH_FOCUS.get())
                            .withIcon(ModRegistry.EARTH_FOCUS.get())
                            .withTextPage("ars_elemental.page1.earth_focus")
                            .withPage(ImbuementPage(ModRegistry.EARTH_FOCUS.get()))
                            .withTextPage("ars_elemental.page2.earth_focus")
                    ,getPath(EQUIPMENT,"earth_focus")
            );

            for(PatchouliPage patchouliPage : pages){
                DataProvider.save(GSON, cache, patchouliPage.build(), patchouliPage.path());
            }

        }

        @Override
        public void addBasicItem(ItemLike item, ResourceLocation category, IPatchouliPage recipePage){
            PatchouliBuilder builder = new PatchouliBuilder(category, item.asItem().getDescriptionId())
                    .withIcon(item.asItem())
                    .withPage(new TextPage("ars_elemental.page." + item.asItem().getRegistryName().getPath()))
                    .withPage(recipePage);
            this.pages.add(new PatchouliPage(builder, getPath(category, item.asItem().getRegistryName().getPath())));
        }

        /**
         * Gets a name for this provider, to use in logging.
         */
        @Override
        public String getName() {
            return "ArsElemental Patchouli Datagen";
        }

        @Override
        public Path getPath(ResourceLocation category, String fileName){
            return this.generator.getOutputFolder().resolve("data/ars_elemental/patchouli_books/elemental_notes/en_us/entries/" + category.getPath() +"/" + fileName + ".json");
        }

        ImbuementPage ImbuementPage(ItemLike item){
            return new ImbuementPage("ars_elemental:imbuement_" + item.asItem().getRegistryName().getPath());
        }

    }
}
