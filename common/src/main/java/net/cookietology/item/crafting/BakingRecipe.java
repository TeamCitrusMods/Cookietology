package net.cookietology.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.cookietology.item.IBakeable;
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

public class BakingRecipe implements IBakingRecipe {
    protected final NonNullList<Ingredient> ingredients;
    protected final ItemStack result;
    protected final int bakeTime;
    private final ResourceLocation id;

    public BakingRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack result, int bakeTime) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
        this.bakeTime = bakeTime;
    }

    @Override
    public boolean matches(Container container, Level level) {
        List<ItemStack> inputStacks = new ArrayList<>();
        for (int i = 0; i < 2; ++i) {
            ItemStack slotStack = container.getItem(i);
            if (!slotStack.isEmpty()) {
                inputStacks.add(container.getItem(i));
            }
        }

        return RecipeMatcher.findMatches(inputStacks, this.ingredients) != null;
    }

    @Override
    public ItemStack assemble(Container container) {
        ItemStack result = this.result.copy();

        for (int i = 0; i < 2; ++i) {
            ItemStack slotStack = container.getItem(i);
            if (slotStack.getItem() instanceof IBakeable bakeable) {
                bakeable.onBaked(slotStack, result);
            }
        }
        return result;
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
        return CookietologyRecipes.BAKING_SERIALIZER.get();
    }

    @Override
    public int getBakeTime() {
        return this.bakeTime;
    }

    public static class Serializer implements RecipeSerializer<BakingRecipe> {

        @Override
        public BakingRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
            NonNullList<Ingredient> ingredients = RecipeUtil.ingredientsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"), false);

            if (ingredients.size() != 2) {
                throw new JsonParseException("Baking recipe needs and accepts a total of 2 ingredients");
            } else {
                ItemStack resultStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
                int bakeTime = GsonHelper.getAsInt(jsonObject, "bake_time");

                return new BakingRecipe(id, ingredients, resultStack, bakeTime);
            }
        }

        @Override
        public BakingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf byteBuffer) {
            NonNullList<Ingredient> ingredientList = NonNullList.withSize(2, Ingredient.EMPTY);

            for (int i = 0; i < ingredientList.size(); ++i) {
                ingredientList.set(i, Ingredient.fromNetwork(byteBuffer));
            }

            ItemStack result = byteBuffer.readItem();
            int bakeTime = byteBuffer.readVarInt();

            return new BakingRecipe(id, ingredientList, result, bakeTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf byteBuffer, BakingRecipe bakingRecipe) {
            for (Ingredient ingredient : bakingRecipe.ingredients) {
                ingredient.toNetwork(byteBuffer);
            }
            byteBuffer.writeItem(bakingRecipe.result);
            byteBuffer.writeVarInt(bakingRecipe.bakeTime);
        }
    }
}
