package net.cookietology.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.cookietology.Cookietology;
import net.cookietology.compat.jei.CookietologyJEIRecipeTypes;
import net.cookietology.compat.jei.recipe.ButteratorExampleRecipe;
import net.cookietology.registry.CookietologyBlocks;
import net.cookietology.util.TimeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ButteratorRecipeCategory implements IRecipeCategory<ButteratorExampleRecipe> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Cookietology.MODID, "textures/gui/jei/butterator.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final Component name;
    private final IGuiHelper guiHelper;

    public ButteratorRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 142, 30);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(CookietologyBlocks.BUTTERATOR.get()));
        this.name = Component.translatable("block.cookietology.butterator");
        this.guiHelper = guiHelper;
    }


    @Override
    public RecipeType<ButteratorExampleRecipe> getRecipeType() {
        return CookietologyJEIRecipeTypes.BUTTERATOR;
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
    public void setRecipe(IRecipeLayoutBuilder recipeLayout, ButteratorExampleRecipe recipe, IFocusGroup focuses) {
        recipeLayout.addSlot(RecipeIngredientRole.OUTPUT, 63, 13)
                .addItemStack(recipe.getOutput())
                .addTooltipCallback((slotView, components) ->
                        components.add(Component.translatable("jei.category.butterator.butter_amount").withStyle(ChatFormatting.DARK_GRAY)));
    }

    @Override
    public void draw(ButteratorExampleRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        this.drawButterTime(recipe, stack, 22);
        this.guiHelper.drawableBuilder(TEXTURE, 0, 30, (int) (136.0F * ((float) recipe.getMilkTime() / 9000.0F)), 4).build()
                .draw(stack, 3, 1);
    }

    protected void drawButterTime(ButteratorExampleRecipe recipe, PoseStack poseStack, int y) {
        int cookTime = recipe.getMilkTime();
        if (cookTime > 0) {
            String timeString = TimeUtil.formatTicksToTime(cookTime);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            fontRenderer.draw(poseStack, timeString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }
}
