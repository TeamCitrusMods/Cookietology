package net.cookietology.item.crafting.special;

import net.cookietology.item.DoughItem;
import net.cookietology.registry.CookietologyItems;
import net.cookietology.registry.CookietologyRecipes;
import net.cookietology.util.CookieHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class DoughBrillianceRecipe extends CustomRecipe {

    public DoughBrillianceRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        int doughs = 0;
        int butters = 0;

        for (int i = 0; i < craftingContainer.getContainerSize(); ++i) {
            ItemStack slotStack = craftingContainer.getItem(i);
            if (!slotStack.isEmpty()) {
                if (slotStack.is(CookietologyItems.SOFT_DOUGH.get())) {
                    ++doughs;
                } else {
                    if (!slotStack.is(CookietologyItems.SOFT_BUTTER.get())) {
                        return false;
                    }
                    ++butters;
                }

                if (doughs > 1) {
                    return false;
                }
            }
        }

        return doughs == 1 && butters >= 1;
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer) {
        DoughItem.DoughProperties doughProperties = DoughItem.DEFAULT_PROPERTIES;
        int totalButters = 0;

        for (int i = 0; i < craftingContainer.getContainerSize(); ++i) {
            ItemStack slotStack = craftingContainer.getItem(i);
            if (!slotStack.isEmpty()) {
                if (slotStack.is(CookietologyItems.SOFT_DOUGH.get())) {
                    doughProperties = CookieHelper.getDoughProperties(slotStack);
                } else if (slotStack.is(CookietologyItems.SOFT_BUTTER.get())) {
                    totalButters++;
                }
            }
        }

        ItemStack resultDough = new ItemStack(CookietologyItems.SOFT_DOUGH.get());
        CookieHelper.saveDoughProperties(resultDough,
                doughProperties.thickness(),
                Math.min(doughProperties.brilliance() + totalButters, 10));

        return resultDough;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CookietologyRecipes.DOUGH_BRILLIANCE_SERIALIZER.get();
    }

    // This is a recipe with custom behavior, the following methods are only used so JEI's CraftingCategoryExtension
    // can show an example, and we don't need to create a new extension

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, Ingredient.of(CookietologyItems.SOFT_DOUGH.get()), Ingredient.of(CookietologyItems.SOFT_BUTTER.get()));
    }

    @Override
    public ItemStack getResultItem() {
        ItemStack resultDough = new ItemStack(CookietologyItems.SOFT_DOUGH.get());
        CookieHelper.saveDoughProperties(resultDough, 5, 2);
        return new ItemStack(CookietologyItems.SOFT_DOUGH.get());
    }
}
