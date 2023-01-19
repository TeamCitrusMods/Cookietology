package net.cookietology.item.crafting;

import net.cookietology.registry.CookietologyRecipes;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface IMixingRecipe extends Recipe<Container> {
    @Override
    default RecipeType<?> getType() {
        return CookietologyRecipes.MIXING.get();
    }

    int getAttempts();
}
