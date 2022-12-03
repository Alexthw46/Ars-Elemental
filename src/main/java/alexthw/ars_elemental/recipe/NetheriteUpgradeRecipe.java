package alexthw.ars_elemental.recipe;

import alexthw.ars_elemental.registry.ModRegistry;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.common.block.tile.EnchantingApparatusTile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NetheriteUpgradeRecipe extends EnchantingApparatusRecipe {

    public NetheriteUpgradeRecipe(ResourceLocation pRecipeId, Ingredient reagent, List<Ingredient> stacks, int cost) {
        this.reagent = reagent;
        this.pedestalItems = stacks;
        this.sourceCost = cost;
        this.id = pRecipeId;
        this.keepNbtOfReagent = true;
    }

    @Override
    public boolean doesReagentMatch(ItemStack reag) {
        return super.doesReagentMatch(reag) && !reag.copy().getOrCreateTag().contains("ae_netherite");
    }

    @Override
    public ItemStack getResult(List<ItemStack> pedestalItems, ItemStack reagent, EnchantingApparatusTile enchantingApparatusTile) {
        ItemStack temp = reagent.copy();
        temp.getOrCreateTag().putBoolean("ae_netherite", true);
        return temp;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRegistry.NETHERITE_UP.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistry.NETHERITE_UP_SERIALIZER.get();
    }


    @Override
    public JsonElement asRecipe() {
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("type", "ars_elemental:netherite_upgrade");
        jsonobject.addProperty("sourceCost", getSourceCost());

        JsonArray reagent = new JsonArray();
        reagent.add(this.reagent.toJson());
        jsonobject.add("reagent", reagent);

        JsonArray pedestalArr = new JsonArray();
        for (Ingredient i : this.pedestalItems) {
            JsonObject object = new JsonObject();
            object.add("item", i.toJson());
            pedestalArr.add(object);
        }
        jsonobject.add("pedestalItems", pedestalArr);
        return jsonobject;
    }

    public static class Serializer implements RecipeSerializer<NetheriteUpgradeRecipe> {

        @Override
        public NetheriteUpgradeRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {

            Ingredient reagent = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "reagent"));
            int cost = json.has("sourceCost") ? GsonHelper.getAsInt(json, "sourceCost") : 0;
            JsonArray pedestalItems = GsonHelper.getAsJsonArray(json, "pedestalItems");
            List<Ingredient> stacks = new ArrayList<>();

            for (JsonElement e : pedestalItems) {
                JsonObject obj = e.getAsJsonObject();
                Ingredient input = GsonHelper.isArrayNode(obj, "item") ?
                        Ingredient.fromJson(GsonHelper.getAsJsonArray(obj, "item")) :
                        Ingredient.fromJson(GsonHelper.getAsJsonObject(obj, "item"));

                stacks.add(input);
            }
            return new NetheriteUpgradeRecipe(pRecipeId, reagent, stacks, cost);
        }

        @Override
        public @Nullable NetheriteUpgradeRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf buffer) {
            int length = buffer.readInt();
            Ingredient reagent = Ingredient.fromNetwork(buffer);
            List<Ingredient> stacks = new ArrayList<>();

            for (int i = 0; i < length; i++) {
                try {
                    stacks.add(Ingredient.fromNetwork(buffer));
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            int cost = buffer.readInt();
            return new NetheriteUpgradeRecipe(pRecipeId, reagent, stacks, cost);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, NetheriteUpgradeRecipe recipe) {
            buf.writeInt(recipe.pedestalItems.size());
            recipe.reagent.toNetwork(buf);
            for (Ingredient i : recipe.pedestalItems) {
                i.toNetwork(buf);
            }
            buf.writeInt(recipe.sourceCost);
        }

    }

}
