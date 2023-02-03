package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.common.glyphs.*;
import alexthw.ars_elemental.common.glyphs.filters.*;
import alexthw.ars_elemental.registry.ModItems;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.common.crafting.recipes.GlyphRecipe;
import com.hollingsworth.arsnouveau.common.datagen.GlyphRecipeProvider;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.io.IOException;
import java.nio.file.Path;

import static com.hollingsworth.arsnouveau.api.RegistryHelper.getRegistryName;

public class AEGlyphProvider {

    public static class GlyphProvider extends GlyphRecipeProvider {

        public GlyphProvider(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        public void run(CachedOutput cache) throws IOException {

            Path output = this.generator.getOutputFolder();

            recipes.add(get(EffectConjureTerrain.INSTANCE).withItem(ItemsRegistry.EARTH_ESSENCE).withItem(Items.DIRT));
            recipes.add(get(EffectWaterGrave.INSTANCE).withItem(Items.KELP).withItem(Items.PRISMARINE_SHARD).withItem(ItemsRegistry.WATER_ESSENCE));
            recipes.add(get(EffectSpores.INSTANCE).withItem(Items.SPORE_BLOSSOM).withItem(Items.RED_MUSHROOM).withItem(ItemsRegistry.EARTH_ESSENCE));
            recipes.add(get(EffectDischarge.INSTANCE).withItem(Items.LIGHTNING_ROD).withItem(ModItems.FLASHING_POD.get().asItem()).withItem(ItemsRegistry.AIR_ESSENCE));
            recipes.add(get(EffectCharm.INSTANCE).withItem(ModItems.ANIMA_ESSENCE.get()).withItem(Items.GOLDEN_CARROT).withItem(ItemsRegistry.SOURCE_BERRY_PIE).withItem(Blocks.CAKE));
            recipes.add(get(EffectLifeLink.INSTANCE).withItem(Items.LEAD).withItem(ModItems.ANIMA_ESSENCE.get()).withItem(Items.SCULK_SENSOR));

            recipes.add(get(MethodCurvedProjectile.INSTANCE).withItem(Items.ARROW).withItem(Items.SNOWBALL).withItem(Items.SLIME_BALL).withItem(Items.ENDER_PEARL));
            recipes.add(get(MethodHomingProjectile.INSTANCE).withItem(Items.NETHER_STAR).withItem(ItemsRegistry.MANIPULATION_ESSENCE).withItem(ItemsRegistry.DOWSING_ROD).withItem(Items.ENDER_EYE));

            recipes.add(get(PropagatorArc.INSTANCE).withItem(ItemsRegistry.MANIPULATION_ESSENCE).withItem(ArsNouveauAPI.getInstance().getGlyphItem(MethodCurvedProjectile.INSTANCE)));
            recipes.add(get(PropagatorHoming.INSTANCE).withItem(ItemsRegistry.MANIPULATION_ESSENCE).withItem(ArsNouveauAPI.getInstance().getGlyphItem(MethodHomingProjectile.INSTANCE)));

            recipes.add(get(AquaticFilter.INSTANCE).withItem(ItemsRegistry.ALLOW_ITEM_SCROLL).withIngredient(Ingredient.of(ItemTags.FISHES)));
            recipes.add(get(AquaticFilter.NOT_INSTANCE).withItem(ItemsRegistry.DENY_ITEM_SCROLL).withIngredient(Ingredient.of(ItemTags.FISHES)));

            recipes.add(get(AerialFilter.INSTANCE).withItem(ItemsRegistry.ALLOW_ITEM_SCROLL).withItem(Items.PHANTOM_MEMBRANE));
            recipes.add(get(AerialFilter.NOT_INSTANCE).withItem(ItemsRegistry.DENY_ITEM_SCROLL).withItem(Items.PHANTOM_MEMBRANE));

            recipes.add(get(FieryFilter.INSTANCE).withItem(ItemsRegistry.ALLOW_ITEM_SCROLL).withItem(Items.BLAZE_POWDER));
            recipes.add(get(FieryFilter.NOT_INSTANCE).withItem(ItemsRegistry.DENY_ITEM_SCROLL).withItem(Items.BLAZE_POWDER));

            recipes.add(get(UndeadFilter.INSTANCE).withItem(ItemsRegistry.ALLOW_ITEM_SCROLL).withItem(Items.ROTTEN_FLESH));
            recipes.add(get(UndeadFilter.NOT_INSTANCE).withItem(ItemsRegistry.DENY_ITEM_SCROLL).withItem(Items.ROTTEN_FLESH));

            recipes.add(get(InsectFilter.INSTANCE).withItem(ItemsRegistry.ALLOW_ITEM_SCROLL).withItem(Items.SPIDER_EYE));
            recipes.add(get(InsectFilter.NOT_INSTANCE).withItem(ItemsRegistry.DENY_ITEM_SCROLL).withItem(Items.SPIDER_EYE));


            for (GlyphRecipe recipe : recipes) {
                Path path = getScribeGlyphPath(output, recipe.output.getItem());
                DataProvider.saveStable(cache, recipe.asRecipe(), path);
            }

        }

        protected static Path getScribeGlyphPath(Path pathIn, Item glyph) {
            return pathIn.resolve("data/ars_elemental/recipes/" + getRegistryName(glyph).getPath() + ".json");
        }

        @Override
        public String getName() {
            return "Ars Elemental Glyph Recipes";
        }
    }

}
