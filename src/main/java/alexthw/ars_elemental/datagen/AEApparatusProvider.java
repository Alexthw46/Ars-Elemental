package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.common.datagen.ApparatusRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.Recipes;
import com.hollingsworth.arsnouveau.setup.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.io.IOException;
import java.nio.file.Path;

public class AEApparatusProvider extends ApparatusRecipeProvider {

    public AEApparatusProvider(DataGenerator generatorIn) {
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
                .keepNbtOfReagent(true)
                .build()
        );

        recipes.add(builder()
                .withResult(ModItems.FIRE_FOCUS.get())
                .withReagent(ModItems.LESSER_FIRE_FOCUS.get())
                .withPedestalItem(ItemsRegistry.WILDEN_TRIBUTE)
                .keepNbtOfReagent(true)
                .build()
        );
        recipes.add(builder()
                .withResult(ModItems.AIR_FOCUS.get())
                .withReagent(ModItems.LESSER_AIR_FOCUS.get())
                .withPedestalItem(ItemsRegistry.WILDEN_TRIBUTE)
                .keepNbtOfReagent(true)
                .build()
        );
        recipes.add(builder()
                .withResult(ModItems.EARTH_FOCUS.get())
                .withReagent(ModItems.LESSER_EARTH_FOCUS.get())
                .withPedestalItem(ItemsRegistry.WILDEN_TRIBUTE)
                .keepNbtOfReagent(true)
                .build()
        );
        recipes.add(builder()
                .withResult(ModItems.WATER_FOCUS.get())
                .withReagent(ModItems.LESSER_WATER_FOCUS.get())
                .withPedestalItem(ItemsRegistry.WILDEN_TRIBUTE)
                .keepNbtOfReagent(true)
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
                .withResult(ModItems.ENCHANTER_BANGLE.get())
                .withReagent(ItemsRegistry.RING_OF_POTENTIAL)
                .withPedestalItem(Recipes.SOURCE_GEM_BLOCK)
                .withPedestalItem(Items.GOLD_BLOCK)
                .withPedestalItem(Items.GOLD_BLOCK)
                .withPedestalItem(Items.END_CRYSTAL)
                .build()
        );
        recipes.add(builder()
                .withResult(ModItems.AIR_BANGLE.get())
                .withReagent(ModItems.ENCHANTER_BANGLE.get())
                .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                .build()
        );
        recipes.add(builder()
                .withResult(ModItems.FIRE_BANGLE.get())
                .withReagent(ModItems.ENCHANTER_BANGLE.get())
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .build()
        );
        recipes.add(builder()
                .withResult(ModItems.EARTH_BANGLE.get())
                .withReagent(ModItems.ENCHANTER_BANGLE.get())
                .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                .build()
        );
        recipes.add(builder()
                .withResult(ModItems.WATER_BANGLE.get())
                .withReagent(ModItems.ENCHANTER_BANGLE.get())
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .build()
        );

        //mirror shield enchant
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
                .withPedestalItem(BlockRegistry.SPELL_PRISM)
                .withPedestalItem(Items.TOTEM_OF_UNDYING)
                .withPedestalItem(Recipes.SOURCE_GEM_BLOCK)
                .buildEnchantmentRecipe(ModRegistry.MIRROR.get(), 3, 9000));

        Path output = this.generator.getOutputFolder();
        for (EnchantingApparatusRecipe g : recipes) {
            if (g != null) {
                Path path = getRecipePath(output, g.getId().getPath());
                DataProvider.save(GSON, cache, g.asRecipe(), path);
            }
        }

    }

    protected static Path getRecipePath(Path pathIn, String str) {
        return pathIn.resolve("data/ars_elemental/recipes/" + str + ".json");
    }

    @Override
    public String getName() {
        return "Ars Elemental Apparatus";
    }
}
