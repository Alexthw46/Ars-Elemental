package alexthw.ars_elemental.recipe;

import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.util.PerkUtil;
import com.hollingsworth.arsnouveau.common.crafting.recipes.ApparatusRecipeInput;
import com.hollingsworth.arsnouveau.common.crafting.recipes.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.common.crafting.recipes.ITextOutput;
import com.hollingsworth.arsnouveau.common.items.data.ArmorPerkHolder;
import com.hollingsworth.arsnouveau.common.util.ANCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ElementalArmorRecipe extends EnchantingApparatusRecipe implements ITextOutput {

    public int tier = 3; // 0 indexed

    public int tier() {
        return tier;
    }

    public ElementalArmorRecipe(Ingredient reagent, ItemStack result, List<Ingredient> pedestalItems, int cost) {
        super(reagent, result, pedestalItems, cost, true);
    }

    public ElementalArmorRecipe(Ingredient reagent, ItemStack result, List<Ingredient> pedestalItems, int cost, int tier) {
        super(reagent, result, pedestalItems, cost, true);
        this.tier = tier;
    }

    @Override
    public boolean matches(ApparatusRecipeInput input, Level level) {
        ArmorPerkHolder perkHolder = PerkUtil.getPerkHolder(input.catalyst());
        if (!(perkHolder instanceof ArmorPerkHolder armorPerkHolder)) {
            return false;
        }
        return armorPerkHolder.getTier() == (tier - 1) && super.matches(input, level);
    }

    @Override
    public @NotNull ItemStack assemble(ApparatusRecipeInput input, HolderLookup.@NotNull Provider provider) {
        ItemStack result = super.assemble(input, provider);
        if (!input.catalyst().isComponentsPatchEmpty()) {
            result.applyComponents(input.catalyst().getComponentsPatch());
            result.setDamageValue(0);
        }
        return result;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRegistry.ELEMENTAL_ARMOR_UP.get();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRegistry.ELEMENTAL_ARMOR_UP_SERIALIZER.get();
    }

    /**
     * Returns the component that should be displayed in the output slot.
     */
    @Override
    public Component getOutputComponent() {
        return Component.translatable("ars_nouveau.armor_upgrade.book_desc", tier);
    }

    @Override
    public boolean excludeJei() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<ElementalArmorRecipe> {
        //CODEC
        public @NotNull MapCodec<ElementalArmorRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, ElementalArmorRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static MapCodec<ElementalArmorRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("reagent").forGetter(ElementalArmorRecipe::reagent),
                ItemStack.CODEC.fieldOf("result").forGetter(ElementalArmorRecipe::result),
                Ingredient.CODEC.listOf().fieldOf("pedestalItems").forGetter(ElementalArmorRecipe::pedestalItems),
                Codec.INT.fieldOf("sourceCost").forGetter(ElementalArmorRecipe::sourceCost),
                Codec.INT.optionalFieldOf("tier", 3).forGetter(recipe -> recipe.tier)
        ).apply(instance, ElementalArmorRecipe::new));

        public static StreamCodec<RegistryFriendlyByteBuf, ElementalArmorRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC,
                ElementalArmorRecipe::reagent,
                ItemStack.STREAM_CODEC,
                ElementalArmorRecipe::result,
                ANCodecs.INGREDIENT_LIST_STREAM,
                ElementalArmorRecipe::pedestalItems,
                ByteBufCodecs.VAR_INT,
                ElementalArmorRecipe::sourceCost,
                ByteBufCodecs.VAR_INT,
                ElementalArmorRecipe::tier,
                ElementalArmorRecipe::new
        );
    }


}
