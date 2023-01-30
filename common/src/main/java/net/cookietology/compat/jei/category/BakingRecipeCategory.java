package net.cookietology.compat.jei.category;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.cookietology.Cookietology;
import net.cookietology.compat.jei.CookietologyJEIRecipeTypes;
import net.cookietology.item.crafting.IBakingRecipe;
import net.cookietology.registry.CookietologyBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class BakingRecipeCategory implements IRecipeCategory<IBakingRecipe> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Cookietology.MODID, "textures/gui/jei/baker.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final Component name;
    private final IDrawableAnimated animatedFlame;
    private final LoadingCache<Integer, IDrawableAnimated> cachedAnimatedArrow;

    public BakingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 91, 54);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(CookietologyBlocks.BAKER.get()));
        this.name = Component.translatable("block.cookietology.baker");
        this.animatedFlame = guiHelper.drawableBuilder(TEXTURE, 91, 0, 14, 14)
                .buildAnimated(300, IDrawableAnimated.StartDirection.TOP, true);
        this.cachedAnimatedArrow = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return guiHelper.drawableBuilder(TEXTURE, 91, 14, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    @Override
    public RecipeType<IBakingRecipe> getRecipeType() {
        return CookietologyJEIRecipeTypes.BAKING;
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
    public void setRecipe(IRecipeLayoutBuilder recipeLayout, IBakingRecipe recipe, IFocusGroup focuses) {
        recipeLayout.addSlot(RecipeIngredientRole.INPUT, 1, 1)
                .addIngredients(recipe.getIngredients().get(0));

        recipeLayout.addSlot(RecipeIngredientRole.INPUT, 19, 1)
                .addIngredients(recipe.getIngredients().get(1));

        recipeLayout.addSlot(RecipeIngredientRole.OUTPUT, 70, 19)
                .addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(IBakingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        this.animatedFlame.draw(poseStack, 10, 20);
        this.getArrow(recipe).draw(poseStack, 33, 18);
        this.drawCookTime(recipe, poseStack, 45);
    }

    @Override
    public boolean isHandled(IBakingRecipe recipe) {
        return !recipe.isSpecial();
    }

    protected IDrawableAnimated getArrow(IBakingRecipe recipe) {
        int cookTime = recipe.getBakeTime();
        if (cookTime <= 0) {
            cookTime = 200;
        }
        return this.cachedAnimatedArrow.getUnchecked(cookTime);
    }

    protected void drawCookTime(IBakingRecipe recipe, PoseStack poseStack, int y) {
        int cookTime = recipe.getBakeTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            fontRenderer.draw(poseStack, timeString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }
}
