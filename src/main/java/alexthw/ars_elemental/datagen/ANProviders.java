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

        private final DataGenerator generator;
        private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
        List<GlyphRecipe> recipes = new ArrayList<>();

        public GlyphProvider(DataGenerator generatorIn) {
            super(generatorIn);
            this.generator = generatorIn;
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

        public static Path getScribeGlyphPath(Path pathIn, Item glyph) {
            return pathIn.resolve("data/ars_nouveau/recipes/" + glyph.getRegistryName().getPath() + ".json");
        }

        @Override
        public String getName() {
            return "Ars Elemental Glyph Recipes";
        }
    }

    public static class EnchantingAppProvider extends ApparatusRecipeProvider{

        private final DataGenerator generator;
        private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

        public EnchantingAppProvider(DataGenerator generatorIn) {
            super(generatorIn);
            this.generator = generatorIn;
        }

        List<EnchantingApparatusRecipe> recipes = new ArrayList<>();
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

        private static Path getRecipePath(Path pathIn, String str){
            return pathIn.resolve("data/ars_elemental/recipes/" + str + ".json");
        }
        @Override
        public String getName() {
            return "Ars Elemental Apparatus";
        }
    }

    public static class ImbuementProvider implements DataProvider{

        private final DataGenerator generator;
        List<ImbuementRecipe> recipes = new ArrayList<>();
        private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

        public ImbuementProvider(DataGenerator generatorIn){
            this.generator = generatorIn;
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

            Path output = this.generator.getOutputFolder();
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
        private final DataGenerator generator;

        public PatchouliProvider(DataGenerator generatorIn) {
            super(generatorIn);
            this.generator = generatorIn;
        }

        private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
        public record PatchouliPage(PatchouliBuilder builder, Path path) {
            public JsonObject build(){
                return builder.build();
            }
        }
        public List<PatchouliPage> pages = new ArrayList<>();

        @Override
        public void run(HashCache cache) throws IOException {

            addGlyphPage(EffectConjureDirt.INSTANCE);
            addGlyphPage(EffectWaterGrave.INSTANCE);

            addBasicItem(ModRegistry.CURIO_BAG.get(), EQUIPMENT, new CraftingPage(ModRegistry.CURIO_BAG.get()));
            addPage(new PatchouliBuilder(EQUIPMENT, ModRegistry.NECRO_FOCUS.get())
                            .withLocalizedText()
                            .withPage(new ApparatusPage(ModRegistry.NECRO_FOCUS.get()))
                    ,getPath(EQUIPMENT,"necrotic_focus")
            );

            /* too painful
            addPage(new PatchouliBuilder(EQUIPMENT, "elemental_foci")
                            .withIcon(ModRegistry.FIRE_FOCUS.get())
                            .withLocalizedText()
                            .withPage(new ImbuementPage(ModRegistry.FIRE_FOCUS.get()))
                    ,getPath(EQUIPMENT,"elemental_foci")
            );
            */

            for(PatchouliPage patchouliPage : pages){
                DataProvider.save(GSON, cache, patchouliPage.build(), patchouliPage.path);
            }
        }

        @Override
        public void addPage(PatchouliBuilder builder, Path path){
            this.pages.add(new PatchouliPage(builder, path));
        }

        @Override
        public void addBasicItem(ItemLike item, ResourceLocation category, IPatchouliPage recipePage){
            PatchouliBuilder builder = new PatchouliBuilder(category, item.asItem().getDescriptionId())
                    .withIcon(item.asItem())
                    .withPage(new TextPage("ars_elemental.page." + item.asItem().getRegistryName().getPath()))
                    .withPage(recipePage);
            this.pages.add(new PatchouliPage(builder, getPath(category, item.asItem().getRegistryName().getPath())));
        }

        @Override
        public void addGlyphPage(AbstractSpellPart spellPart){
            ResourceLocation category = switch (spellPart.getTier().value) {
                case 1 -> GLYPHS_1;
                case 2 -> GLYPHS_2;
                default -> GLYPHS_3;
            };
            PatchouliBuilder builder = new PatchouliBuilder(category, spellPart.getName())
                    .withName("ars_nouveau.glyph_name." + spellPart.getId())
                    .withIcon(ArsNouveau.MODID + ":" + spellPart.getItemID())
                    .withSortNum(spellPart instanceof AbstractCastMethod ? 1 : spellPart instanceof AbstractEffect ? 2 : 3)
                    .withPage(new TextPage("ars_nouveau.glyph_desc." + spellPart.getId()))
                    .withPage(new GlyphPressPage(spellPart));
            this.pages.add(new PatchouliPage(builder, getPath(category, "glyph_" + spellPart.getId())));
        }
        /**
         * Gets a name for this provider, to use in logging.
         */
        @Override
        public String getName() {
            return "ArsElemental Patchouli Datagen";
        }

        public Path getPath(ResourceLocation category, String fileName){
            return this.generator.getOutputFolder().resolve("data/ars_elemental/patchouli_books/elemental_notes/en_us/entries/" + category.getPath() +"/" + fileName + ".json");
        }
    }
}
