package net.cookietology.item.crafting.special;

import net.cookietology.item.DoughItem;
import net.cookietology.registry.CookietologyItems;
import net.cookietology.registry.CookietologyRecipes;
import net.cookietology.util.CookieHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class DoughCompressingRecipe extends CustomRecipe {
    public DoughCompressingRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        int doughs = 0;

        for (int i = 0; i < craftingContainer.getContainerSize(); ++i) {
            ItemStack slotStack = craftingContainer.getItem(i);
            if (!slotStack.isEmpty()) {
                if (slotStack.is(CookietologyItems.SOFT_DOUGH.get())) {
                    ++doughs;
                } else {
                    return false;
                }
            }
        }

        return doughs > 1;
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer) {
        int doughsThickness = 0;
        int doughsMaxBrilliance = 0;

        for (int i = 0; i < craftingContainer.getContainerSize(); ++i) {
            ItemStack slotStack = craftingContainer.getItem(i);
            if (!slotStack.isEmpty()) {
                if (slotStack.is(CookietologyItems.SOFT_DOUGH.get())) {
                    DoughItem.DoughProperties doughProperties = CookieHelper.getDoughProperties(slotStack);
                    doughsThickness = Math.min(doughsThickness + doughProperties.thickness(), 50);
                    doughsMaxBrilliance = Math.max(doughsMaxBrilliance, doughProperties.brilliance());
                }
            }
        }

        ItemStack resultDough = new ItemStack(CookietologyItems.SOFT_DOUGH.get());
        CookieHelper.saveDoughProperties(resultDough, doughsThickness, doughsMaxBrilliance);

        return resultDough;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CookietologyRecipes.DOUGH_COMPRESSING_SERIALIZER.get();
    }
}
