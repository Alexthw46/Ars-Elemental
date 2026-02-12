package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.common.items.armor.ArmorSet;
import alexthw.ars_elemental.common.items.armor.ShockPerk;
import alexthw.ars_elemental.common.items.armor.SporePerk;
import alexthw.ars_elemental.common.items.armor.SummonPerk;
import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.registry.ModRegistry;
import com.alexthw.sauce.common.recipe.ElementalArmorRecipe;
import com.alexthw.sauce.registry.SauceTags;
import com.hollingsworth.arsnouveau.common.crafting.recipes.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.common.datagen.ApparatusRecipeBuilder;
import com.hollingsworth.arsnouveau.common.datagen.ApparatusRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.ItemTagProvider;
import com.hollingsworth.arsnouveau.common.datagen.RecipeDatagen;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class AEApparatusProvider extends ApparatusRecipeProvider {

    public AEApparatusProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void collectJsons(CachedOutput cache) {

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
                .withResult(getPerkItem(SummonPerk.INSTANCE.getRegistryName()))
                .withReagent(ItemsRegistry.BLANK_THREAD)
                .withPedestalItem(2, ItemsRegistry.CONJURATION_ESSENCE)
                .withPedestalItem(1, Items.ECHO_SHARD)
                .withPedestalItem(2, Ingredient.of(ItemTagProvider.WILDEN_DROP_TAG))
                .build()
        );

        recipes.add(builder()
                .withResult(ModItems.NECRO_FOCUS.get())
                .withReagent(ItemsRegistry.SUMMONING_FOCUS)
                .withPedestalItem(2, Items.WITHER_ROSE)
                .withPedestalItem(1, Items.WITHER_SKELETON_SKULL)
                .withPedestalItem(Ingredient.of(SauceTags.ANIMA_ESSENCE))
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
                .withResult(ModItems.LAVA_UPSTREAM_BLOCK.get())
                .withReagent(Items.SOUL_SAND)
                .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .withPedestalItem(4, Items.MAGMA_BLOCK)
                .build()
        );

        recipes.add(builder()
                .withResult(ModItems.AIR_UPSTREAM_BLOCK.get())
                .withReagent(Items.GOLD_BLOCK)
                .withPedestalItem(4, ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(ItemsRegistry.WILDEN_WING)
                .withPedestalItem(2, Items.SHULKER_SHELL)
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
                .withResult(ModItems.FLASHJACK_CHARM.get())
                .withReagent(ModItems.FLASHJACK_SHARDS.get())
                .withPedestalItem(2, ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(2, Items.GOLD_INGOT)
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
        recipes.add(builder()
                .withResult(ModItems.SUMMON_BANGLE.get())
                .withReagent(ModItems.ENCHANTER_BANGLE.get())
                .withPedestalItem(ItemsRegistry.CONJURATION_ESSENCE)
                .withPedestalItem(ItemsRegistry.CONJURATION_ESSENCE)
                .withPedestalItem(Items.BONE)
                .withPedestalItem(ItemsRegistry.WILDEN_HORN)
                .build()
        );
        recipes.add(builder()
                .withResult(ModItems.ANIMA_BANGLE.get())
                .withReagent(ModItems.ENCHANTER_BANGLE.get())
                .withPedestalItem(2, Ingredient.of(SauceTags.ANIMA_ESSENCE))
                .withPedestalItem(Items.GHAST_TEAR)
                .withPedestalItem(Items.WITHER_ROSE)
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

        recipes.add(builder()
                .withResult(ModItems.CHAIN_LENS.get().getDefaultInstance())
                .withReagent(Ingredient.of(Tags.Items.GEMS_QUARTZ))
                .withPedestalItem(ItemsRegistry.MANIPULATION_ESSENCE)
                .withPedestalItem(BlockRegistry.SOURCE_GEM_BLOCK)
                .withPedestalItem(ItemsRegistry.BLANK_PARCHMENT)
                .build()
        );

        //mirror shield enchant
        recipes.add(builder()
                .withPedestalItem(BlockRegistry.SPELL_PRISM)
                .withPedestalItem(ItemsRegistry.MANIPULATION_ESSENCE)
                .withPedestalItem(ItemsRegistry.ABJURATION_ESSENCE)
                .buildEnchantmentRecipe(ModRegistry.MIRROR, 1, 2000));

        recipes.add(builder()
                .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .withPedestalItem(ModItems.SPELL_MIRROR.get())
                .buildEnchantmentRecipe(ModRegistry.MIRROR, 2, 5000));

        recipes.add(builder()
                .withPedestalItem(ItemsRegistry.ENCHANTERS_MIRROR)
                .withPedestalItem(Items.TOTEM_OF_UNDYING)
                .withPedestalItem(RecipeDatagen.SOURCE_GEM_BLOCK)
                .buildEnchantmentRecipe(ModRegistry.MIRROR, 3, 8000));

        recipes.add(builder()
                .withPedestalItem(Ingredient.of(SauceTags.ANIMA_ESSENCE))
                .withPedestalItem(Items.TOTEM_OF_UNDYING)
                .withPedestalItem(Blocks.LAPIS_BLOCK)
                .withPedestalItem(RecipeDatagen.SOURCE_GEM_BLOCK)
                .buildEnchantmentRecipe(ModRegistry.SOULBOUND, 1, 10000));

        addArmorRecipes(ModItems.FIRE_ARMOR, ItemsRegistry.FIRE_ESSENCE, "medium");
        addArmorRecipes(ModItems.WATER_ARMOR, ItemsRegistry.WATER_ESSENCE, "medium");
        addArmorRecipes(ModItems.AIR_ARMOR, ItemsRegistry.AIR_ESSENCE, "medium");
        addArmorRecipes(ModItems.EARTH_ARMOR, ItemsRegistry.EARTH_ESSENCE, "medium");

        addArmorRecipes(ModItems.FIRE_ARMOR_L, ItemsRegistry.FIRE_ESSENCE, "light");
        addArmorRecipes(ModItems.WATER_ARMOR_L, ItemsRegistry.WATER_ESSENCE, "light");
        addArmorRecipes(ModItems.AIR_ARMOR_L, ItemsRegistry.AIR_ESSENCE, "light");
        addArmorRecipes(ModItems.EARTH_ARMOR_L, ItemsRegistry.EARTH_ESSENCE, "light");

        addArmorRecipes(ModItems.FIRE_ARMOR_H, ItemsRegistry.FIRE_ESSENCE, "heavy");
        addArmorRecipes(ModItems.WATER_ARMOR_H, ItemsRegistry.WATER_ESSENCE, "heavy");
        addArmorRecipes(ModItems.AIR_ARMOR_H, ItemsRegistry.AIR_ESSENCE, "heavy");
        addArmorRecipes(ModItems.EARTH_ARMOR_H, ItemsRegistry.EARTH_ESSENCE, "heavy");

        recipes.add(builder()
                .withResult(new ItemStack(ModItems.MARK_OF_MASTERY.get(), 5))
                .withSourceCost(10000)
                .withReagent(ItemsRegistry.WILDEN_TRIBUTE)
                .withPedestalItem(ItemsRegistry.ABJURATION_ESSENCE)
                .withPedestalItem(ItemsRegistry.FIRE_ESSENCE)
                .withPedestalItem(ItemsRegistry.CONJURATION_ESSENCE)
                .withPedestalItem(ItemsRegistry.AIR_ESSENCE)
                .withPedestalItem(Ingredient.of(SauceTags.ANIMA_ESSENCE))
                .withPedestalItem(ItemsRegistry.WATER_ESSENCE)
                .withPedestalItem(ItemsRegistry.MANIPULATION_ESSENCE)
                .withPedestalItem(ItemsRegistry.EARTH_ESSENCE)
                .build()
        );

        recipes.add(builder()
                .withResult(ModItems.CASTER_BAG.get())
                .withReagent(ModItems.CURIO_BAG.get())
                .withPedestalItem(ItemsRegistry.MANIPULATION_ESSENCE)
                .withPedestalItem(ItemsRegistry.MANIPULATION_ESSENCE)
                .withPedestalItem(Items.BLAZE_POWDER)
                .withPedestalItem(Items.BLAZE_POWDER)
                .withPedestalItem(Items.GOLD_BLOCK)
                .withPedestalItem(Items.GOLD_BLOCK)
                .keepNbtOfReagent(true)
                .build());

        recipes.add(builder().withResult(ModItems.FIRE_RELAY.get()).withReagent(BlockRegistry.RELAY_COLLECTOR.asItem()).withPedestalItem(2, ItemsRegistry.FIRE_ESSENCE).withPedestalItem(2, Tags.Items.GEMS_DIAMOND).build());
        recipes.add(builder().withResult(ModItems.WATER_RELAY.get()).withReagent(BlockRegistry.RELAY_SPLITTER.asItem()).withPedestalItem(2, ItemsRegistry.WATER_ESSENCE).withPedestalItem(2, Tags.Items.GEMS_DIAMOND).build());
        recipes.add(builder().withResult(ModItems.AIR_RELAY.get()).withReagent(BlockRegistry.RELAY_WARP.asItem()).withPedestalItem(2, ItemsRegistry.AIR_ESSENCE).withPedestalItem(2, Tags.Items.GEMS_DIAMOND).build());
        recipes.add(builder().withResult(ModItems.EARTH_RELAY.get()).withReagent(BlockRegistry.RELAY_DEPOSIT.asItem()).withPedestalItem(2, ItemsRegistry.EARTH_ESSENCE).withPedestalItem(2, Tags.Items.GEMS_DIAMOND).build());

        Path output = this.generator.getPackOutput().getOutputFolder();
        for (ApparatusRecipeBuilder.RecipeWrapper<? extends EnchantingApparatusRecipe> g : recipes) {
            if (g != null) {
                Path path = getRecipePath(output, g.id().getPath());
                saveStable(cache, g.serialize(), path);
            }
        }

    }

    protected void addArmorRecipes(ArmorSet armorSet, ItemLike essence, String armorType) {

        recipes.add(Abuilder().withResult(armorSet.getHat()).withReagent(Ingredient.of(
                switch (armorType) {
                    case "light" -> ItemsRegistry.SORCERER_HOOD.asItem();
                    case "heavy" -> ItemsRegistry.BATTLEMAGE_HOOD.asItem();
                    default -> ItemsRegistry.ARCANIST_HOOD.asItem();
                }
        )).withPedestalItem(ModItems.MARK_OF_MASTERY.get()).withPedestalItem(Items.NETHERITE_INGOT).withPedestalItem(2, essence).withSourceCost(7000).keepNbtOfReagent(true).build());
        recipes.add(Abuilder().withResult(armorSet.getChest()).withReagent(Ingredient.of(
                switch (armorType) {
                    case "light" -> ItemsRegistry.SORCERER_ROBES.asItem();
                    case "heavy" -> ItemsRegistry.BATTLEMAGE_ROBES.asItem();
                    default -> ItemsRegistry.ARCANIST_ROBES.asItem();
                }
        )).withPedestalItem(ModItems.MARK_OF_MASTERY.get()).withPedestalItem(Items.NETHERITE_INGOT).withPedestalItem(2, essence).withSourceCost(7000).keepNbtOfReagent(true).build());
        recipes.add(Abuilder().withResult(armorSet.getLegs()).withReagent(Ingredient.of(
                switch (armorType) {
                    case "light" -> ItemsRegistry.SORCERER_LEGGINGS.asItem();
                    case "heavy" -> ItemsRegistry.BATTLEMAGE_LEGGINGS.asItem();
                    default -> ItemsRegistry.ARCANIST_LEGGINGS.asItem();
                }
        )).withPedestalItem(ModItems.MARK_OF_MASTERY.get()).withPedestalItem(Items.NETHERITE_INGOT).withPedestalItem(2, essence).withSourceCost(7000).keepNbtOfReagent(true).build());
        recipes.add(Abuilder().withResult(armorSet.getBoots()).withReagent(Ingredient.of(
                switch (armorType) {
                    case "light" -> ItemsRegistry.SORCERER_BOOTS.asItem();
                    case "heavy" -> ItemsRegistry.BATTLEMAGE_BOOTS.asItem();
                    default -> ItemsRegistry.ARCANIST_BOOTS.asItem();
                }
        )).withPedestalItem(ModItems.MARK_OF_MASTERY.get()).withPedestalItem(Items.NETHERITE_INGOT).withPedestalItem(2, essence).withSourceCost(7000).keepNbtOfReagent(true).build());

    }

    protected static Path getRecipePath(Path pathIn, String str) {
        return pathIn.resolve("data/ars_elemental/recipe/" + str + ".json");
    }

    @Override
    public @NotNull String getName() {
        return "Ars Elemental Apparatus";
    }

    ArmorBuilder Abuilder() {
        return new ArmorBuilder();
    }

    public static class ArmorBuilder extends ApparatusRecipeBuilder {

        @Override
        public RecipeWrapper<EnchantingApparatusRecipe> build() {
            var wrapper = super.build();
            return new RecipeWrapper<>(wrapper.id(), new ElementalArmorRecipe(wrapper.recipe().reagent(), wrapper.recipe().result(), wrapper.recipe().pedestalItems(), wrapper.recipe().sourceCost()), ElementalArmorRecipe.CODEC);
        }
    }

}
