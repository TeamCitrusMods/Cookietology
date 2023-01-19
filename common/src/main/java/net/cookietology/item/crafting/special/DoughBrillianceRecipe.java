package net.cookietology.item.crafting.special;

import net.cookietology.item.DoughItem;
import net.cookietology.item.crafting.IMixingRecipe;
import net.cookietology.registry.CookietologyItems;
import net.cookietology.registry.CookietologyRecipes;
import net.cookietology.util.CookieHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class DoughBrillianceRecipe implements IMixingRecipe {
    private final ResourceLocation id;

    public DoughBrillianceRecipe(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public boolean matches(Container container, Level level) {
        int doughs = 0;
        int butters = 0;

        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack slotStack = container.getItem(i);
            if (!slotStack.isEmpty()) {
                if (slotStack.is(CookietologyItems.SOFT_DOUGH.get())) {
                    ++doughs;
                } else {
                    if (!slotStack.is(CookietologyItems.SOFT_BUTTER.get())) {
                        return false;
                    }

                    ++butters;
                }
                if (butters > 1 || doughs > 1) {
                    return false;
                }
            }
        }

        return doughs == 1 && butters == 1;
    }

    @Override
    public ItemStack assemble(Container container) {
        DoughItem.DoughProperties doughProperties = DoughItem.DEFAULT_PROPERTIES;

        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack slotStack = container.getItem(i);
            if (!slotStack.isEmpty()) {
                if (slotStack.is(CookietologyItems.SOFT_DOUGH.get())) {
                    doughProperties = CookieHelper.getDoughProperties(slotStack);
                    break;
                }
            }
        }

        ItemStack resultDough = new ItemStack(CookietologyItems.SOFT_DOUGH.get());
        CookieHelper.saveDoughProperties(resultDough, doughProperties.thickness(), doughProperties.brilliance() < 10 && Math.random() <= 0.25D ? doughProperties.brilliance() + 1 : doughProperties.brilliance());

        return resultDough;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CookietologyRecipes.DOUGH_BRILLIANCE_SERIALIZER.get();
    }

    @Override
    public int getAttempts() {
        return 5;
    }
}
