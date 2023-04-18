package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.common.glyphs.MethodCurvedProjectile;
import alexthw.ars_elemental.common.glyphs.MethodHomingProjectile;
import alexthw.ars_elemental.registry.ModItems;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.common.crafting.recipes.ImbuementRecipe;
import com.hollingsworth.arsnouveau.common.datagen.ImbuementRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.RecipeDatagen;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDecelerate;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.setup.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.io.IOException;
import java.nio.file.Path;

public class AEImbuementProvider extends ImbuementRecipeProvider {

    public AEImbuementProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void run(CachedOutput cache) throws IOException {

        recipes.add(new ImbuementRecipe("anima_essence", RecipeDatagen.SOURCE_GEM, ModItems.ANIMA_ESSENCE.get().getDefaultInstance(), 3000)
                .withPedestalItem(Items.WITHER_SKELETON_SKULL)
                .withPedestalItem(Items.BONE_MEAL)
                .withPedestalItem(Items.GOLDEN_APPLE));

        recipes.add(new ImbuementRecipe("lesser_fire_focus", Ingredient.of(Items.AMETHYST_SHARD), ModItems.LESSER_FIRE_FOCUS.get().getDefaultInstance(), 5000)
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .withPedestalItem(Items.GOLD_INGOT)
                .withPedestalItem(Items.GOLD_INGOT)
                .withPedestalItem(Items.GOLD_INGOT));
        recipes.add(new ImbuementRecipe("lesser_water_focus", Ingredient.of(Items.AMETHYST_SHARD), ModItems.LESSER_WATER_FOCUS.get().getDefaultInstance(), 5000)
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .withPedestalItem(Items.GOLD_INGOT)
                .withPedestalItem(Items.GOLD_INGOT)
                .withPedestalItem(Items.GOLD_INGOT));
        recipes.add(new ImbuementRecipe("lesser_earth_focus", Ingredient.of(Items.AMETHYST_SHARD), ModItems.LESSER_EARTH_FOCUS.get().getDefaultInstance(), 5000)
                .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                .withPedestalItem(Items.GOLD_INGOT)
                .withPedestalItem(Items.GOLD_INGOT)
                .withPedestalItem(Items.GOLD_INGOT));
        recipes.add(new ImbuementRecipe("lesser_air_focus", Ingredient.of(Items.AMETHYST_SHARD), ModItems.LESSER_AIR_FOCUS.get().getDefaultInstance(), 5000)
                .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(Items.GOLD_INGOT)
                .withPedestalItem(Items.GOLD_INGOT)
                .withPedestalItem(Items.GOLD_INGOT)
        );

        recipes.add(new ImbuementRecipe("fire_turret", Ingredient.of(BlockRegistry.ENCHANTED_SPELL_TURRET), new ItemStack(ModItems.FIRE_TURRET.get(), 1), 5000)
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .withPedestalItem(ModItems.FIRE_FOCUS.get())
        );
        recipes.add(new ImbuementRecipe("water_turret", Ingredient.of(BlockRegistry.ENCHANTED_SPELL_TURRET), new ItemStack(ModItems.WATER_TURRET.get(), 1), 5000)
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .withPedestalItem(ModItems.WATER_FOCUS.get())
        );
        recipes.add(new ImbuementRecipe("air_turret", Ingredient.of(BlockRegistry.ENCHANTED_SPELL_TURRET), new ItemStack(ModItems.AIR_TURRET.get(), 1), 5000)
                .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(ModItems.AIR_FOCUS.get())
        );
        recipes.add(new ImbuementRecipe("earth_turret", Ingredient.of(BlockRegistry.ENCHANTED_SPELL_TURRET), new ItemStack(ModItems.EARTH_TURRET.get(), 1), 5000)
                .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                .withPedestalItem(ModItems.EARTH_FOCUS.get())
        );
        recipes.add(new ImbuementRecipe("manipulation_turret", Ingredient.of(BlockRegistry.ENCHANTED_SPELL_TURRET), new ItemStack(ModItems.SHAPING_TURRET.get(), 1), 5000)
                .withPedestalItem(ItemsRegistry.MANIPULATION_ESSENCE)
                .withPedestalItem(ItemsRegistry.MANIPULATION_ESSENCE)
                .withPedestalItem(ItemsRegistry.MANIPULATION_ESSENCE)
                .withPedestalItem(ItemsRegistry.SHAPERS_FOCUS.get())
        );

        recipes.add(new ImbuementRecipe("mark_of_mastery", Ingredient.of(ItemsRegistry.WILDEN_TRIBUTE), new ItemStack(ModItems.MARK_OF_MASTERY.get(), 5), 10000)
                .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(ItemsRegistry.ABJURATION_ESSENCE)
                .withPedestalItem(ItemsRegistry.MANIPULATION_ESSENCE)
                .withPedestalItem(ModItems.ANIMA_ESSENCE.get())
        );

        recipes.add(new ImbuementRecipe("arc_prism_lens", Ingredient.of(Tags.Items.GEMS_QUARTZ), ModItems.ARC_LENS.get().getDefaultInstance(), 2000)
                .withPedestalItem(ItemsRegistry.MANIPULATION_ESSENCE)
                .withPedestalItem(ArsNouveauAPI.getInstance().getGlyphItem(MethodCurvedProjectile.INSTANCE))
        );
        recipes.add(new ImbuementRecipe("homing_prism_lens", Ingredient.of(Tags.Items.GEMS_QUARTZ), ModItems.HOMING_LENS.get().getDefaultInstance(), 2000)
                .withPedestalItem(ItemsRegistry.MANIPULATION_ESSENCE)
                .withPedestalItem(ArsNouveauAPI.getInstance().getGlyphItem(MethodHomingProjectile.INSTANCE))
        );
        recipes.add(new ImbuementRecipe("acceleration_prism_lens", Ingredient.of(Tags.Items.GEMS_QUARTZ), ModItems.ACC_LENS.get().getDefaultInstance(), 2000)
                .withPedestalItem(ItemsRegistry.MANIPULATION_ESSENCE)
                .withPedestalItem(ArsNouveauAPI.getInstance().getGlyphItem(AugmentAccelerate.INSTANCE))
        );
        recipes.add(new ImbuementRecipe("deceleration_prism_lens", Ingredient.of(Tags.Items.GEMS_QUARTZ), ModItems.DEC_LENS.get().getDefaultInstance(), 2000)
                .withPedestalItem(ItemsRegistry.MANIPULATION_ESSENCE)
                .withPedestalItem(ArsNouveauAPI.getInstance().getGlyphItem(AugmentDecelerate.INSTANCE))
        );
        recipes.add(new ImbuementRecipe("piercing_prism_lens", Ingredient.of(Tags.Items.GEMS_QUARTZ), ModItems.PIERCE_LENS.get().getDefaultInstance(), 2000)
                .withPedestalItem(ItemsRegistry.MANIPULATION_ESSENCE)
                .withPedestalItem(ArsNouveauAPI.getInstance().getGlyphItem(AugmentPierce.INSTANCE))
        );
        recipes.add(new ImbuementRecipe("rainbow_prism_lens", Ingredient.of(Tags.Items.GEMS_QUARTZ), ModItems.RGB_LENS.get().getDefaultInstance(), 2000)
                .withPedestalItem(Ingredient.of(Tags.Items.DYES))
                .withPedestalItem(Ingredient.of(Tags.Items.DYES))
                .withPedestalItem(Ingredient.of(Tags.Items.DYES))
                .withPedestalItem(Ingredient.of(Tags.Items.DYES))
                .withPedestalItem(Ingredient.of(Tags.Items.DYES))
                .withPedestalItem(Ingredient.of(Tags.Items.DYES))
                .withPedestalItem(Ingredient.of(Tags.Items.DYES))
        );


        Path output = generator.getOutputFolder();
        for (ImbuementRecipe g : recipes) {
            Path path = getRecipePath(output, g.getId().getPath());
            DataProvider.saveStable(cache, g.asRecipe(), path);
        }

    }

    protected Path getRecipePath(Path pathIn, String str) {
        return pathIn.resolve("data/ars_elemental/recipes/" + str + ".json");
    }

    @Override
    public String getName() {
        return "Ars Elemental Imbuement";
    }

}
