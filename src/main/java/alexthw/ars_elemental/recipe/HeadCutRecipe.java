package alexthw.ars_elemental.recipe;

import alexthw.ars_elemental.registry.ModRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class HeadCutRecipe extends CustomRecipe {

    public int chance() {
        return chance;
    }

    public ResourceLocation mob() {
        return mob;
    }

    public ItemStack result() {
        return result;
    }

    public ResourceLocation mob;
    public ItemStack result; // Result item
    public int chance;

    public HeadCutRecipe(ItemStack result, ResourceLocation mob, int chance) {
        super(CraftingBookCategory.MISC);
        this.mob = mob;
        this.result = result;
        this.chance = chance;
    }

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level pLevel) {
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput pContainer, @NotNull HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRegistry.HEAD_CUT_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRegistry.HEAD_CUT.get();
    }

    public static class Serializer implements RecipeSerializer<HeadCutRecipe> {

        public static MapCodec<HeadCutRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStack.CODEC.fieldOf("output").forGetter(recipe -> recipe.result),
                ResourceLocation.CODEC.fieldOf("mob").forGetter(recipe -> recipe.mob),
                Codec.INT.fieldOf("drop_chance").forGetter(recipe -> recipe.chance)
        ).apply(instance, HeadCutRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, HeadCutRecipe> STREAM = StreamCodec.composite(
                ItemStack.STREAM_CODEC,
                HeadCutRecipe::result,
                ResourceLocation.STREAM_CODEC,
                HeadCutRecipe::mob,
                ByteBufCodecs.VAR_INT,
                HeadCutRecipe::chance,
                HeadCutRecipe::new
        );

        @Override
        public @NotNull MapCodec<HeadCutRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, HeadCutRecipe> streamCodec() {
            return STREAM;
        }

    }
}
