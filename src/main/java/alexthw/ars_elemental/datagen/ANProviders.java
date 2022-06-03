package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.common.entity.familiars.FirenandoHolder;
import alexthw.ars_elemental.common.entity.familiars.MermaidHolder;
import alexthw.ars_elemental.common.glyphs.*;
import alexthw.ars_elemental.common.rituals.DetectionRitual;
import alexthw.ars_elemental.common.rituals.SquirrelRitual;
import alexthw.ars_elemental.common.rituals.TeslaRitual;
import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.api.familiar.AbstractFamiliarHolder;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.crafting.recipes.GlyphRecipe;
import com.hollingsworth.arsnouveau.common.crafting.recipes.ImbuementRecipe;
import com.hollingsworth.arsnouveau.common.datagen.ApparatusRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.GlyphRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.ImbuementRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.Recipes;
import com.hollingsworth.arsnouveau.common.datagen.patchouli.*;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.io.IOException;
import java.nio.file.Path;

import static alexthw.ars_elemental.ArsElemental.prefix;
import static alexthw.ars_elemental.ArsNouveauRegistry.registeredSpells;

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
            recipes.add(get(EffectSpores.INSTANCE).withItem(Items.SPORE_BLOSSOM).withItem(Items.RED_MUSHROOM).withItem(ItemsRegistry.EARTH_ESSENCE));

            recipes.add(get(EffectCharm.INSTANCE).withItem(Items.GOLDEN_APPLE).withItem(Items.GOLDEN_CARROT).withItem(ItemsRegistry.SOURCE_BERRY_PIE).withItem(Blocks.CAKE));
            recipes.add(get(EffectLifeLink.INSTANCE).withItem(Items.LEAD).withItem(ItemsRegistry.ABJURATION_ESSENCE).withItem(Items.GLISTERING_MELON_SLICE).withItem(Items.IRON_SWORD).withItem(Items.FERMENTED_SPIDER_EYE));

            recipes.add(get(MethodCurvedProjectile.INSTANCE).withItem(Items.ARROW).withItem(Items.SNOWBALL).withItem(Items.SLIME_BALL).withItem(Items.ENDER_PEARL));
            recipes.add(get(MethodHomingProjectile.INSTANCE).withItem(Items.NETHER_STAR).withItem(ItemsRegistry.MANIPULATION_ESSENCE).withItem(ItemsRegistry.DOWSING_ROD).withItem(Items.ENDER_EYE));

            recipes.add(get(PropagatorArc.INSTANCE).withItem(ItemsRegistry.MANIPULATION_ESSENCE).withItem(ArsNouveauAPI.getInstance().getGlyphItem(MethodCurvedProjectile.INSTANCE)));
            recipes.add(get(PropagatorHoming.INSTANCE).withItem(ItemsRegistry.MANIPULATION_ESSENCE).withItem(ArsNouveauAPI.getInstance().getGlyphItem(MethodHomingProjectile.INSTANCE)));

            for (GlyphRecipe recipe : recipes) {
                Path path = getScribeGlyphPath(output, recipe.output.getItem());
                DataProvider.save(GSON, cache, recipe.asRecipe(), path);
            }

        }

        @Override
        public String getName() {
            return "Ars Elemental Glyph Recipes";
        }
    }

    public static class EnchantingAppProvider extends ApparatusRecipeProvider{

        public EnchantingAppProvider(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        public void run(HashCache cache) throws IOException {

            recipes.add(builder()
                    .withResult(ModItems.NECRO_FOCUS.get())
                    .withReagent(ItemsRegistry.SUMMONING_FOCUS)
                    .withPedestalItem(2, Items.WITHER_ROSE)
                    .withPedestalItem(1, Items.WITHER_SKELETON_SKULL)
                    .withPedestalItem(1, ItemsRegistry.CONJURATION_ESSENCE)
                    .build()
            );

            recipes.add(builder()
                    .withResult(ModItems.UPSTREAM_BLOCK.get())
                    .withReagent(Items.SOUL_SAND)
                    .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                    .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                    .withPedestalItem(4, Items.PRISMARINE_SHARD)
                    .build()
            );

            recipes.add(builder()
                    .withResult(ModItems.SIREN_CHARM.get())
                    .withReagent(ModItems.SIREN_SHARDS.get())
                    .withPedestalItem(Items.PRISMARINE_SHARD)
                    .withPedestalItem(3, Ingredient.of(ItemTags.FISHES))
                    .withPedestalItem(3, Recipes.SOURCE_GEM)
                    .build()
            );

            recipes.add(builder()
                    .withResult(ModItems.FIRENANDO_CHARM.get())
                    .withReagent(Items.MAGMA_BLOCK)
                    .withPedestalItem(2, ItemsRegistry.FIRE_ESSENCE)
                    .withPedestalItem(Items.NETHERITE_SCRAP)
                    .withPedestalItem(2, Items.NETHER_BRICK)
                    .build()
            );

            recipes.add(builder()
                    .withResult(ModItems.SPELL_HORN.get())
                    .withReagent(ItemsRegistry.WILDEN_HORN)
                    .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                    .withPedestalItem(3, Items.GOLD_INGOT)
                    .withPedestalItem(4, Recipes.SOURCE_GEM)
                    .build()
            );

            recipes.add(builder()
                    .withPedestalItem(ItemsRegistry.ENCHANTERS_MIRROR)
                    .withPedestalItem(ItemsRegistry.MANIPULATION_ESSENCE)
                    .withPedestalItem(ItemsRegistry.ABJURATION_ESSENCE)
                    .buildEnchantmentRecipe(ModRegistry.MIRROR.get(), 1, 3000));

            recipes.add(builder()
                    .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                    .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                    .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                    .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                    .withPedestalItem(Ingredient.of(Tags.Items.STORAGE_BLOCKS_LAPIS))
                    .buildEnchantmentRecipe(ModRegistry.MIRROR.get(), 2, 6000));

            recipes.add(builder()
                    .withPedestalItem(Items.TOTEM_OF_UNDYING)
                    .withPedestalItem(Recipes.SOURCE_GEM_BLOCK)
                    .withPedestalItem(Ingredient.of(Tags.Items.STORAGE_BLOCKS_LAPIS))
                    .buildEnchantmentRecipe(ModRegistry.MIRROR.get(), 3, 9000));

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

            recipes.add(new ImbuementRecipe("fire_focus", Ingredient.of(Items.AMETHYST_SHARD), new ItemStack(ModItems.FIRE_FOCUS.get(), 1), 5000)
                    .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                    .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                    .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                    .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                    .withPedestalItem(ItemsRegistry.WILDEN_TRIBUTE)
            );
            recipes.add(new ImbuementRecipe("water_focus", Ingredient.of(Items.AMETHYST_SHARD), new ItemStack(ModItems.WATER_FOCUS.get(), 1), 5000)
                    .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                    .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                    .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                    .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                    .withPedestalItem(ItemsRegistry.WILDEN_TRIBUTE)
            );
            recipes.add(new ImbuementRecipe("earth_focus", Ingredient.of(Items.AMETHYST_SHARD), new ItemStack(ModItems.EARTH_FOCUS.get(), 1), 5000)
                    .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                    .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                    .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                    .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                    .withPedestalItem(ItemsRegistry.WILDEN_TRIBUTE)
            );
            recipes.add(new ImbuementRecipe("air_focus", Ingredient.of(Items.AMETHYST_SHARD), new ItemStack(ModItems.AIR_FOCUS.get(), 1), 5000)
                    .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                    .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                    .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                    .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                    .withPedestalItem(ItemsRegistry.WILDEN_TRIBUTE)
            );

            Path output = generator.getOutputFolder();
            for(ImbuementRecipe g : recipes){
                Path path = getRecipePath(output, g.getId().getPath());
                DataProvider.save(GSON, cache, g.asRecipe(), path);
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

    @SuppressWarnings("ConstantConditions")
    public static class PatchouliProvider extends com.hollingsworth.arsnouveau.common.datagen.PatchouliProvider {

        public PatchouliProvider(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        public void run(HashCache cache) throws IOException {

            for (AbstractSpellPart spell : registeredSpells) {
                addGlyphPage(spell);
            }

            addBasicItem(ModItems.UPSTREAM_BLOCK.get(), MACHINES, new ApparatusPage(ModItems.UPSTREAM_BLOCK.get()));
            addBasicItem(ModItems.CURIO_BAG.get(), EQUIPMENT, new CraftingPage(ModItems.CURIO_BAG.get()));

            addPage(new PatchouliBuilder(EQUIPMENT, ModItems.SPELL_HORN.get())
                            .withIcon(ModItems.SPELL_HORN.get())
                            .withTextPage("ars_elemental.page1.spell_horn")
                            .withPage(new ApparatusPage(ModItems.SPELL_HORN.get()))
                    , getPath(EQUIPMENT, "spell_horn"));

            addPage(new PatchouliBuilder(EQUIPMENT, ModItems.NECRO_FOCUS.get())
                            .withIcon(ModItems.NECRO_FOCUS.get())
                            .withTextPage("ars_elemental.page1.necrotic_focus")
                            .withPage(new ApparatusPage(ModItems.NECRO_FOCUS.get()))
                            .withTextPage("ars_elemental.page2.necrotic_focus")
                    , getPath(EQUIPMENT, "necrotic_focus")
            );

            addPage(new PatchouliBuilder(EQUIPMENT, ModItems.FIRE_FOCUS.get())
                            .withIcon(ModItems.FIRE_FOCUS.get())
                            .withTextPage("ars_elemental.page1.fire_focus")
                            .withPage(ImbuementPage(ModItems.FIRE_FOCUS.get()))
                            .withTextPage("ars_elemental.page2.fire_focus")
                    , getPath(EQUIPMENT, "fire_focus")
            );
            addPage(new PatchouliBuilder(EQUIPMENT, ModItems.WATER_FOCUS.get())
                            .withIcon(ModItems.WATER_FOCUS.get())
                            .withTextPage("ars_elemental.page1.water_focus")
                            .withPage(ImbuementPage(ModItems.WATER_FOCUS.get()))
                            .withTextPage("ars_elemental.page2.water_focus")
                    , getPath(EQUIPMENT, "water_focus")
            );
            addPage(new PatchouliBuilder(EQUIPMENT, ModItems.AIR_FOCUS.get())
                            .withIcon(ModItems.AIR_FOCUS.get())
                            .withTextPage("ars_elemental.page1.air_focus")
                            .withPage(ImbuementPage(ModItems.AIR_FOCUS.get()))
                            .withTextPage("ars_elemental.page2.air_focus")
                    , getPath(EQUIPMENT, "air_focus")
            );
            addPage(new PatchouliBuilder(EQUIPMENT, ModItems.EARTH_FOCUS.get())
                            .withIcon(ModItems.EARTH_FOCUS.get())
                            .withTextPage("ars_elemental.page1.earth_focus")
                            .withPage(ImbuementPage(ModItems.EARTH_FOCUS.get()))
                            .withTextPage("ars_elemental.page2.earth_focus")
                    , getPath(EQUIPMENT, "earth_focus")
            );

            addPage(new PatchouliBuilder(AUTOMATION, ModItems.SIREN_CHARM.get())
                            .withIcon(ModItems.SIREN_CHARM.get())
                            .withTextPage("ars_elemental.page1.mermaid")
                            .withPage(new ApparatusPage(ModItems.SIREN_CHARM.get()))
                            .withPage(new EntityPage(prefix("siren_entity").toString()))
                            .withTextPage("ars_elemental.page2.mermaid")
                    , getPath(AUTOMATION, "mermaid"));

            addPage(new PatchouliBuilder(AUTOMATION, ModItems.FIRENANDO_CHARM.get())
                            .withIcon(ModItems.FIRENANDO_CHARM.get())
                            .withTextPage("ars_elemental.page1.fire_golem")
                            .withPage(new EntityPage(prefix("firenando_entity").toString()))
                            .withPage(new ApparatusPage(ModItems.FIRENANDO_CHARM.get()))
                    , getPath(AUTOMATION, "fire_golem"));

            addFamiliarPage(new MermaidHolder());
            addFamiliarPage(new FirenandoHolder());

            addRitualPage(new SquirrelRitual());
            addRitualPage(new TeslaRitual());
            addRitualPage(new DetectionRitual());

            addEnchantmentPage(ModRegistry.MIRROR.get());

            for (PatchouliPage patchouliPage : pages) {
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

        public void addFamiliarPage(AbstractFamiliarHolder familiarHolder) {
            PatchouliBuilder builder = new PatchouliBuilder(FAMILIARS, "entity.ars_elemental." + familiarHolder.getId() + "_familiar")
                    .withIcon("ars_nouveau:familiar_" + familiarHolder.getId())
                    .withTextPage("ars_nouveau.familiar_desc." + familiarHolder.getId())
                    .withPage(new EntityPage(prefix(familiarHolder.getEntityKey() + "_familiar").toString()));
            this.pages.add(new PatchouliPage(builder, getPath(FAMILIARS, familiarHolder.getId())));
        }

        public void addRitualPage(AbstractRitual ritual) {
            PatchouliBuilder builder = new PatchouliBuilder(RITUALS, "item.ars_nouveau.ritual_" + ritual.getID())
                    .withIcon("ars_nouveau:ritual_" + ritual.getID())
                    .withTextPage("ars_nouveau.ritual_desc." + ritual.getID())
                    .withPage(new CraftingPage("ars_elemental:ritual_" + ritual.getID()));

            this.pages.add(new PatchouliPage(builder, getPath(RITUALS, ritual.getID())));
        }

        public void addEnchantmentPage(Enchantment enchantment) {
            PatchouliBuilder builder = new PatchouliBuilder(ENCHANTMENTS, enchantment.getDescriptionId())
                    .withIcon(Items.ENCHANTED_BOOK.getRegistryName().toString())
                    .withTextPage("ars_elemental.enchantment_desc." + enchantment.getRegistryName().getPath());

            for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); i++) {
                builder.withPage(new EnchantingPage("ars_nouveau:" + enchantment.getRegistryName().getPath() + "_" + i));
            }
            this.pages.add(new PatchouliPage(builder, getPath(ENCHANTMENTS, enchantment.getRegistryName().getPath())));
        }

        /**
         * Gets a name for this provider, to use in logging.
         */
        @Override
        public String getName() {
            return "Ars Elemental Patchouli Datagen";
        }

        @Override
        public Path getPath(ResourceLocation category, String fileName) {
            return this.generator.getOutputFolder().resolve("data/ars_elemental/patchouli_books/elemental_notes/en_us/entries/" + category.getPath() + "/" + fileName + ".json");
        }

        ImbuementPage ImbuementPage(ItemLike item){
            return new ImbuementPage("ars_elemental:imbuement_" + item.asItem().getRegistryName().getPath());
        }

    }
}
