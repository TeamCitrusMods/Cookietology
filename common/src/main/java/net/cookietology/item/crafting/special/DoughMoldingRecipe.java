package net.cookietology.item.crafting.special;

import net.cookietology.item.DoughItem;
import net.cookietology.item.MoldItem;
import net.cookietology.registry.CookietologyItems;
import net.cookietology.registry.CookietologyRecipes;
import net.cookietology.util.CookieHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class DoughMoldingRecipe extends CustomRecipe {
    public DoughMoldingRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        int molds = 0;
        int doughs = 0;

        for (int k = 0; k < craftingContainer.getContainerSize(); ++k) {
            ItemStack slotStack = craftingContainer.getItem(k);
            if (!slotStack.isEmpty()) {
                if (slotStack.getItem() instanceof MoldItem) {
                    ++molds;
                } else {
                    if (!slotStack.is(CookietologyItems.SOFT_DOUGH.get())) {
                        return false;
                    }
                    ++doughs;
                }

                if (doughs > 1 || molds > 1) {
                    return false;
                }
            }
        }
        return molds == 1 && doughs == 1;
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer) {
        ItemStack resultStack = ItemStack.EMPTY;
        DoughItem.DoughProperties doughProperties = DoughItem.DEFAULT_PROPERTIES;

        for (int i = 0; i < craftingContainer.getContainerSize(); ++i) {
            ItemStack slotStack = craftingContainer.getItem(i);
            if (!slotStack.isEmpty()) {
                Item slotItem = slotStack.getItem();
                if (slotItem instanceof MoldItem moldItem) {
                    resultStack = new ItemStack(moldItem.getMoldResult());
                } else {
                    doughProperties = CookieHelper.getDoughProperties(slotStack);
                }
            }
        }

        CookieHelper.saveDoughProperties(resultStack, doughProperties.thickness(), doughProperties.brilliance());

        return resultStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CookietologyRecipes.DOUGH_MOLDING_SERIALIZER.get();
    }
}
