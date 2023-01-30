package net.cookietology.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import mezz.jei.library.recipes.ExtendableRecipeCategoryHelper;
import net.cookietology.Cookietology;
import net.cookietology.compat.jei.CookietologyJEIRecipeTypes;
import net.cookietology.item.crafting.IMixingRecipe;
import net.cookietology.registry.CookietologyBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Iterator;

public class MixingRecipeCategory implements IRecipeCategory<IMixingRecipe> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Cookietology.MODID, "textures/gui/jei/mixer.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final Component name;
    private final ExtendableRecipeCategoryHelper<Recipe<?>, IRecipeCategoryExtension> extendableHelper = new ExtendableRecipeCategoryHelper<>(IMixingRecipe.class);

    public MixingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 118, 36);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(CookietologyBlocks.MIXING_BOWL.get()));
        this.name = Component.translatable("block.cookietology.mixing_bowl");
    }

    @Override
    public RecipeType<IMixingRecipe> getRecipeType() {
        return CookietologyJEIRecipeTypes.MIXING;
    }

    @Override
    public Component getTitle() {
        return this.name;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder recipeLayout, IMixingRecipe recipe, IFocusGroup focuses) {
        Iterator<Ingredient> ingredientIterator = recipe.getIngredients().iterator();

        gridBuilder:
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 3; col++) {

                if (ingredientIterator.hasNext()) {
                    recipeLayout.addSlot(RecipeIngredientRole.INPUT, col * 18 + 1, row * 18 + 1)
                            .addIngredients(ingredientIterator.next());
                } else {
                    break gridBuilder;
                }
            }
        }

        recipeLayout.addSlot(RecipeIngredientRole.OUTPUT, 97, 11)
                .addItemStack(recipe.getResultItem());
    }

    @Override
    public boolean isHandled(IMixingRecipe recipe) {
        return !recipe.isSpecial();
    }
}
