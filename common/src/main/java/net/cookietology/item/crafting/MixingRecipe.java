package net.cookietology.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.cookietology.registry.CookietologyRecipes;
import net.cookietology.util.RecipeMatcher;
import net.cookietology.util.RecipeUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class MixingRecipe implements IMixingRecipe {
    protected final NonNullList<Ingredient> ingredients;
    protected final ItemStack result;
    protected final int attempts;
    private final ResourceLocation id;

    public MixingRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack result, int attempts) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
        this.attempts = attempts;
    }

    @Override
    public boolean matches(Container container, Level level) {
        List<ItemStack> containerStacks = new ArrayList<>();
        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack containerStack = container.getItem(i);
            if (!containerStack.isEmpty()) {
                containerStacks.add(container.getItem(i));
            }
        }

        return RecipeMatcher.findMatches(containerStacks, this.ingredients) != null;
    }

    @Override
    public ItemStack assemble(Container container) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CookietologyRecipes.MIXING_SERIALIZER.get();
    }

    @Override
    public int getAttempts() {
        return this.attempts;
    }

    public static class Serializer implements RecipeSerializer<MixingRecipe> {

        @Override
        public MixingRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
            NonNullList<Ingredient> ingredients = RecipeUtil.ingredientsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"), false);

            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for mixing recipe");
            } else if (ingredients.size() > 6) {
                throw new JsonParseException("Too many ingredients for mixing recipe. The maximum is 6");
            } else {
                ItemStack resultStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
                int attempts = GsonHelper.getAsInt(jsonObject, "attempts");

                return new MixingRecipe(id, ingredients, resultStack, attempts);
            }
        }

        @Override
        public MixingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf byteBuffer) {
            int ingredientsSize = byteBuffer.readVarInt();
            NonNullList<Ingredient> ingredientList = NonNullList.withSize(ingredientsSize, Ingredient.EMPTY);

            for (int i = 0; i < ingredientList.size(); ++i) {
                ingredientList.set(i, Ingredient.fromNetwork(byteBuffer));
            }

            ItemStack result = byteBuffer.readItem();
            int attempts = byteBuffer.readVarInt();

            return new MixingRecipe(id, ingredientList, result, attempts);
        }

        @Override
        public void toNetwork(FriendlyByteBuf byteBuffer, MixingRecipe mixingRecipe) {
            byteBuffer.writeVarInt(mixingRecipe.ingredients.size());
            for (Ingredient ingredient : mixingRecipe.ingredients) {
                ingredient.toNetwork(byteBuffer);
            }
            byteBuffer.writeItem(mixingRecipe.result);
            byteBuffer.writeVarInt(mixingRecipe.attempts);
        }
    }
}
