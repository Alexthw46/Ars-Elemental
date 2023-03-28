package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.common.glyphs.*;
import alexthw.ars_elemental.common.glyphs.filters.*;
import alexthw.ars_elemental.registry.ModItems;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
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
import net.minecraft.world.level.ItemLike;
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

            addRecipe(EffectConjureTerrain.INSTANCE, ItemsRegistry.EARTH_ESSENCE, Items.DIRT);
            addRecipe(EffectWaterGrave.INSTANCE, Items.KELP, Items.PRISMARINE_SHARD, ItemsRegistry.WATER_ESSENCE);
            addRecipe(EffectSpores.INSTANCE, Items.SPORE_BLOSSOM, Items.RED_MUSHROOM, ItemsRegistry.EARTH_ESSENCE);
            addRecipe(EffectDischarge.INSTANCE, Items.LIGHTNING_ROD, ModItems.FLASHING_POD.get().asItem(), ItemsRegistry.AIR_ESSENCE);
            addRecipe(EffectCharm.INSTANCE, ModItems.ANIMA_ESSENCE.get(), Items.GOLDEN_CARROT, ItemsRegistry.SOURCE_BERRY_PIE, Blocks.CAKE);
            addRecipe(EffectLifeLink.INSTANCE, Items.LEAD, ModItems.ANIMA_ESSENCE.get(), Items.SCULK_SENSOR);
            addRecipe(EffectPhantom.INSTANCE, Items.PHANTOM_MEMBRANE, Items.PHANTOM_MEMBRANE, ModItems.ANIMA_ESSENCE.get());

            addRecipe(MethodCurvedProjectile.INSTANCE, Items.ARROW, Items.SNOWBALL, Items.SLIME_BALL, Items.ENDER_PEARL);
            addRecipe(MethodHomingProjectile.INSTANCE, Items.NETHER_STAR, ItemsRegistry.MANIPULATION_ESSENCE, ItemsRegistry.DOWSING_ROD, Items.ENDER_EYE);

            addRecipe(PropagatorArc.INSTANCE, ItemsRegistry.MANIPULATION_ESSENCE, ArsNouveauAPI.getInstance().getGlyphItem(MethodCurvedProjectile.INSTANCE));
            addRecipe(PropagatorHoming.INSTANCE, ItemsRegistry.MANIPULATION_ESSENCE, ArsNouveauAPI.getInstance().getGlyphItem(MethodHomingProjectile.INSTANCE));

            recipes.add(get(AquaticFilter.INSTANCE).withItem(ItemsRegistry.ALLOW_ITEM_SCROLL).withIngredient(Ingredient.of(ItemTags.FISHES)));
            recipes.add(get(AquaticFilter.NOT_INSTANCE).withItem(ItemsRegistry.DENY_ITEM_SCROLL).withIngredient(Ingredient.of(ItemTags.FISHES)));

            addRecipe(AerialFilter.INSTANCE, ItemsRegistry.ALLOW_ITEM_SCROLL, Items.PHANTOM_MEMBRANE);
            addRecipe(AerialFilter.NOT_INSTANCE, ItemsRegistry.DENY_ITEM_SCROLL, Items.PHANTOM_MEMBRANE);

            addRecipe(FieryFilter.INSTANCE, ItemsRegistry.ALLOW_ITEM_SCROLL, Items.BLAZE_POWDER);
            addRecipe(FieryFilter.NOT_INSTANCE, ItemsRegistry.DENY_ITEM_SCROLL, Items.BLAZE_POWDER);

            addRecipe(UndeadFilter.INSTANCE, ItemsRegistry.ALLOW_ITEM_SCROLL, Items.ROTTEN_FLESH);
            addRecipe(UndeadFilter.NOT_INSTANCE, ItemsRegistry.DENY_ITEM_SCROLL, Items.ROTTEN_FLESH);

            addRecipe(SummonFilter.INSTANCE, ItemsRegistry.ALLOW_ITEM_SCROLL, Items.BONE);
            addRecipe(SummonFilter.NOT_INSTANCE, ItemsRegistry.DENY_ITEM_SCROLL, Items.BONE);

            addRecipe(InsectFilter.INSTANCE, ItemsRegistry.ALLOW_ITEM_SCROLL, Items.SPIDER_EYE);
            addRecipe(InsectFilter.NOT_INSTANCE, ItemsRegistry.DENY_ITEM_SCROLL, Items.SPIDER_EYE);


            for (GlyphRecipe recipe : recipes) {
                Path path = getScribeGlyphPath(output, recipe.output.getItem());
                DataProvider.saveStable(cache, recipe.asRecipe(), path);
            }

        }

        public void addRecipe(AbstractSpellPart part, ItemLike... items) {
            var builder = get(part);
            for (ItemLike item : items) {
                builder.withItem(item);
            }
            recipes.add(builder);
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
