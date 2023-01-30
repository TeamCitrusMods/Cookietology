package net.cookietology.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.extensions.IExtendableRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import mezz.jei.library.plugins.vanilla.crafting.CraftingCategoryExtension;
import net.cookietology.Cookietology;
import net.cookietology.compat.jei.category.BakingRecipeCategory;
import net.cookietology.compat.jei.category.ButteratorRecipeCategory;
import net.cookietology.compat.jei.category.MixingRecipeCategory;
import net.cookietology.compat.jei.recipe.ButteratorExampleRecipe;
import net.cookietology.item.crafting.special.DoughBrillianceRecipe;
import net.cookietology.item.crafting.special.DoughCompressingRecipe;
import net.cookietology.registry.CookietologyBlocks;
import net.cookietology.registry.CookietologyItems;
import net.cookietology.registry.CookietologyRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;

import java.util.Collections;

@JeiPlugin
public class CookietologyPlugin implements IModPlugin {
    private static final ResourceLocation ID = new ResourceLocation(Cookietology.MODID, "main");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(CookietologyBlocks.BUTTERATOR.get()), CookietologyJEIRecipeTypes.BUTTERATOR);
        registration.addRecipeCatalyst(new ItemStack(CookietologyBlocks.MIXING_BOWL.get()), CookietologyJEIRecipeTypes.MIXING);
        registration.addRecipeCatalyst(new ItemStack(CookietologyBlocks.BAKER.get()), CookietologyJEIRecipeTypes.BAKING);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new ButteratorRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new MixingRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new BakingRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        IExtendableRecipeCategory<CraftingRecipe, ICraftingCategoryExtension> craftingCategory = registration.getCraftingCategory();
        craftingCategory.addCategoryExtension(DoughBrillianceRecipe.class, CraftingCategoryExtension::new);
        craftingCategory.addCategoryExtension(DoughCompressingRecipe.class, CraftingCategoryExtension::new);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            registration.addRecipes(CookietologyJEIRecipeTypes.BUTTERATOR, Collections.singletonList(
                    new ButteratorExampleRecipe(2250, new ItemStack(CookietologyItems.SOFT_BUTTER.get(), 4))));
            registration.addRecipes(CookietologyJEIRecipeTypes.MIXING, level.getRecipeManager().getAllRecipesFor(CookietologyRecipes.MIXING.get()));
            registration.addRecipes(CookietologyJEIRecipeTypes.BAKING, level.getRecipeManager().getAllRecipesFor(CookietologyRecipes.BAKING.get()));
        }
    }
}
