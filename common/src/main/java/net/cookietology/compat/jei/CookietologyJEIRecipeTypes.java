package net.cookietology.compat.jei;

import mezz.jei.api.recipe.RecipeType;
import net.cookietology.Cookietology;
import net.cookietology.compat.jei.recipe.ButteratorExampleRecipe;
import net.cookietology.item.crafting.IBakingRecipe;
import net.cookietology.item.crafting.IMixingRecipe;

public class CookietologyJEIRecipeTypes {
    public static final RecipeType<ButteratorExampleRecipe> BUTTERATOR = RecipeType.create(Cookietology.MODID, "butterator", ButteratorExampleRecipe.class);
    public static final RecipeType<IMixingRecipe> MIXING = RecipeType.create(Cookietology.MODID, "mixing", IMixingRecipe.class);
    public static final RecipeType<IBakingRecipe> BAKING = RecipeType.create(Cookietology.MODID, "baking", IBakingRecipe.class);
}
