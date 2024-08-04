package alexthw.ars_elemental.datagen;

import com.hollingsworth.arsnouveau.common.crafting.recipes.CrushRecipe;
import com.hollingsworth.arsnouveau.common.datagen.CrushRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.SimpleDataProvider;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AECrushProvider extends SimpleDataProvider {

    public List<CrushRecipeProvider.CrushWrapper> recipes = new ArrayList<>();

    public AECrushProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void collectJsons(CachedOutput pOutput) {

        recipes.add(new CrushRecipeProvider.CrushWrapper("blaze_powder", Ingredient.of(Items.BLAZE_ROD.getDefaultInstance()))
                .withItems(new ItemStack(Items.BLAZE_POWDER, 3)).withItems(new ItemStack(Items.BLAZE_POWDER), 0.5f));

        recipes.add(new CrushRecipeProvider.CrushWrapper("bone_meal", Ingredient.of(Items.BONE.getDefaultInstance()))
                .withItems(new ItemStack(Items.BONE_MEAL, 3))
                .withItems(new ItemStack(Items.BONE_MEAL), 0.5f)
                .withItems(new ItemStack(Items.BONE_MEAL), 0.5f));

        recipes.add(new CrushRecipeProvider.CrushWrapper("wool_to_string", Ingredient.of(ItemTags.WOOL))
                .withItems(new ItemStack(Items.STRING, 3))
                .withItems(new ItemStack(Items.STRING), 0.5f));

        for (CrushRecipeProvider.CrushWrapper g : recipes) {
            Path path = getRecipePath(output, g.path.getPath());
            saveStable(pOutput, CrushRecipe.CODEC.encodeStart(JsonOps.INSTANCE, g.asRecipe()).getOrThrow(), path);
        }
    }

    private static Path getRecipePath(Path pathIn, String str) {
        return pathIn.resolve("data/ars_elemental/recipe/" + str + ".json");
    }

    @Override
    public @NotNull String getName() {
        return "Elemental Crush";
    }
}
