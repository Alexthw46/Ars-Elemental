package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.common.items.armor.ArmorSet;
import alexthw.ars_elemental.common.items.armor.ShockPerk;
import alexthw.ars_elemental.common.items.armor.SporePerk;
import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.common.datagen.ApparatusRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.RecipeDatagen;
import com.hollingsworth.arsnouveau.setup.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.io.IOException;
import java.nio.file.Path;

public class AEApparatusProvider extends ApparatusRecipeProvider {

    public AEApparatusProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void run(CachedOutput cache) throws IOException {

        recipes.add(builder()
                .withResult(getPerkItem(ShockPerk.INSTANCE.getRegistryName()))
                .withReagent(ItemsRegistry.BLANK_THREAD)
                .withPedestalItem(2, ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(1, Items.LIGHTNING_ROD)
                .withPedestalItem(1, ModItems.FLASHING_POD.get())
                .build()
        );

        recipes.add(builder()
                .withResult(getPerkItem(SporePerk.INSTANCE.getRegistryName()))
                .withReagent(ItemsRegistry.BLANK_THREAD)
                .withPedestalItem(2, ItemsRegistry.EARTH_ESSENCE)
                .withPedestalItem(1, Items.SPORE_BLOSSOM)
                .withPedestalItem(1, Items.SPIDER_EYE)
                .build()
        );

        recipes.add(builder()
                .withResult(ModItems.NECRO_FOCUS.get())
                .withReagent(ItemsRegistry.SUMMONING_FOCUS)
                .withPedestalItem(2, Items.WITHER_ROSE)
                .withPedestalItem(1, Items.WITHER_SKELETON_SKULL)
                .withPedestalItem(1, ModItems.ANIMA_ESSENCE.get())
                .build()
        );

        recipes.add(builder()
                .withResult(ModItems.WATER_UPSTREAM_BLOCK.get())
                .withReagent(Items.SOUL_SAND)
                .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .withPedestalItem(4, Items.PRISMARINE_SHARD)
                .build()
        );

        recipes.add(builder()
                .withResult(ModItems.WATER_URN.get())
                .withReagent(Blocks.FLOWER_POT)
                .withPedestalItem(RecipeDatagen.SOURCE_GEM)
                .withPedestalItem(2, ItemsRegistry.WATER_ESSENCE)
                .withPedestalItem(2, Items.PRISMARINE_SHARD)
                .build()
        );

        recipes.add(builder()
                .withResult(ModItems.SIREN_CHARM.get())
                .withReagent(ModItems.SIREN_SHARDS.get())
                .withPedestalItem(Items.PRISMARINE_SHARD)
                .withPedestalItem(3, Ingredient.of(ItemTags.FISHES))
                .withPedestalItem(3, RecipeDatagen.SOURCE_GEM)
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
                .withPedestalItem(4, RecipeDatagen.SOURCE_GEM)
                .build()
        );

        recipes.add(builder()
                .withResult(ModItems.ENCHANTER_BANGLE.get())
                .withReagent(ItemsRegistry.RING_OF_POTENTIAL)
                .withPedestalItem(RecipeDatagen.SOURCE_GEM_BLOCK)
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
                .withPedestalItem(Items.PISTON)
                .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                .build()
        );
        recipes.add(builder()
                .withResult(ModItems.FIRE_BANGLE.get())
                .withReagent(ModItems.ENCHANTER_BANGLE.get())
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .withPedestalItem(Items.FIRE_CHARGE)
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .build()
        );
        recipes.add(builder()
                .withResult(ModItems.EARTH_BANGLE.get())
                .withReagent(ModItems.ENCHANTER_BANGLE.get())
                .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                .withPedestalItem(Items.COBWEB)
                .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                .build()
        );
        recipes.add(builder()
                .withResult(ModItems.WATER_BANGLE.get())
                .withReagent(ModItems.ENCHANTER_BANGLE.get())
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .withPedestalItem(Items.POWDER_SNOW_BUCKET)
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .build()
        );

        //focus upgrade

        recipes.add(builder()
                .withResult(ModItems.FIRE_FOCUS.get())
                .withReagent(ModItems.LESSER_FIRE_FOCUS.get())
                .withPedestalItem(ModItems.MARK_OF_MASTERY.get())
                .withSourceCost(5000)
                .keepNbtOfReagent(true)
                .build()
        );
        recipes.add(builder()
                .withResult(ModItems.AIR_FOCUS.get())
                .withReagent(ModItems.LESSER_AIR_FOCUS.get())
                .withPedestalItem(ModItems.MARK_OF_MASTERY.get())
                .withSourceCost(5000)
                .keepNbtOfReagent(true)
                .build()
        );
        recipes.add(builder()
                .withResult(ModItems.EARTH_FOCUS.get())
                .withReagent(ModItems.LESSER_EARTH_FOCUS.get())
                .withPedestalItem(ModItems.MARK_OF_MASTERY.get())
                .withSourceCost(5000)
                .keepNbtOfReagent(true)
                .build()
        );
        recipes.add(builder()
                .withResult(ModItems.WATER_FOCUS.get())
                .withReagent(ModItems.LESSER_WATER_FOCUS.get())
                .withPedestalItem(ModItems.MARK_OF_MASTERY.get())
                .withSourceCost(5000)
                .keepNbtOfReagent(true)
                .build()
        );

        //mirror
        recipes.add(builder()
                .withResult(new ItemStack(ModItems.SPELL_MIRROR.get(), 2))
                .withReagent(RecipeDatagen.SOURCE_GEM_BLOCK)
                .withPedestalItem(2, RecipeDatagen.ARCHWOOD_LOG)
                .withPedestalItem(2, Ingredient.of(Tags.Items.GEMS_QUARTZ))
                .withPedestalItem(2, Items.GOLD_INGOT)
                .build()
        );

        //mirror shield enchant
        recipes.add(builder()
                .withPedestalItem(BlockRegistry.SPELL_PRISM)
                .withPedestalItem(ItemsRegistry.MANIPULATION_ESSENCE)
                .withPedestalItem(ItemsRegistry.ABJURATION_ESSENCE)
                .buildEnchantmentRecipe(ModRegistry.MIRROR.get(), 1, 2000));

        recipes.add(builder()
                .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .withPedestalItem(ModItems.SPELL_MIRROR)
                .buildEnchantmentRecipe(ModRegistry.MIRROR.get(), 2, 5000));

        recipes.add(builder()
                .withPedestalItem(ItemsRegistry.ENCHANTERS_MIRROR)
                .withPedestalItem(Items.TOTEM_OF_UNDYING)
                .withPedestalItem(RecipeDatagen.SOURCE_GEM_BLOCK)
                .buildEnchantmentRecipe(ModRegistry.MIRROR.get(), 3, 8000));

        recipes.add(builder()
                .withPedestalItem(ModItems.ANIMA_ESSENCE)
                .withPedestalItem(Items.TOTEM_OF_UNDYING)
                .withPedestalItem(Blocks.LAPIS_BLOCK)
                .withPedestalItem(RecipeDatagen.SOURCE_GEM_BLOCK)
                .buildEnchantmentRecipe(ModRegistry.SOULBOUND.get(), 1, 10000));

        addArmorRecipes(ModItems.FIRE_ARMOR, ItemsRegistry.FIRE_ESSENCE);
        addArmorRecipes(ModItems.WATER_ARMOR, ItemsRegistry.WATER_ESSENCE);
        addArmorRecipes(ModItems.AIR_ARMOR, ItemsRegistry.AIR_ESSENCE);
        addArmorRecipes(ModItems.EARTH_ARMOR, ItemsRegistry.EARTH_ESSENCE);


        Path output = this.generator.getOutputFolder();
        for (EnchantingApparatusRecipe g : recipes) {
            if (g != null) {
                Path path = getRecipePath(output, g.getId().getPath());
                DataProvider.saveStable(cache, g.asRecipe(), path);
            }
        }

    }

    protected void addArmorRecipes(ArmorSet armorSet, ItemLike essence) {

        recipes.add(builder().withResult(armorSet.getHat()).withReagent(ItemsRegistry.APPRENTICE_HOOD).withPedestalItem(ModItems.MARK_OF_MASTERY.get()).withPedestalItem(Items.NETHERITE_INGOT).withPedestalItem(2, essence).withSourceCost(2000).keepNbtOfReagent(true).build());
        recipes.add(builder().withResult(armorSet.getChest()).withReagent(ItemsRegistry.APPRENTICE_ROBES).withPedestalItem(ModItems.MARK_OF_MASTERY.get()).withPedestalItem(Items.NETHERITE_INGOT).withPedestalItem(2, essence).withSourceCost(2000).keepNbtOfReagent(true).build());
        recipes.add(builder().withResult(armorSet.getLegs()).withReagent(ItemsRegistry.APPRENTICE_LEGGINGS).withPedestalItem(ModItems.MARK_OF_MASTERY.get()).withPedestalItem(Items.NETHERITE_INGOT).withPedestalItem(2, essence).withSourceCost(2000).keepNbtOfReagent(true).build());
        recipes.add(builder().withResult(armorSet.getBoots()).withReagent(ItemsRegistry.APPRENTICE_BOOTS).withPedestalItem(ModItems.MARK_OF_MASTERY.get()).withPedestalItem(Items.NETHERITE_INGOT).withPedestalItem(2, essence).withSourceCost(2000).keepNbtOfReagent(true).build());

    }

    protected static Path getRecipePath(Path pathIn, String str) {
        return pathIn.resolve("data/ars_elemental/recipes/" + str + ".json");
    }

    @Override
    public String getName() {
        return "Ars Elemental Apparatus";
    }
}
