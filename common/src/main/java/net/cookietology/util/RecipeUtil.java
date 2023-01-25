package net.cookietology.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;

public class RecipeUtil {

    public static NonNullList<Ingredient> ingredientsFromJson(JsonArray jsonArray, boolean allowEmpties) {
        NonNullList<Ingredient> ingredientList = NonNullList.create();

        for (int i = 0; i < jsonArray.size(); ++i) {
            Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
            if (!allowEmpties && !(ingredient.isEmpty() || Arrays.stream(ingredient.getItems()).anyMatch(ItemStack::isEmpty))) {
                throw new JsonParseException("Ingredient cannot be allowed to be empty");
            }

            ingredientList.add(ingredient);
        }
        return ingredientList;
    }
}
