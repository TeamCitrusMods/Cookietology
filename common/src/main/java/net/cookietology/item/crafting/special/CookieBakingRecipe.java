package net.cookietology.item.crafting.special;

import net.cookietology.item.CookieItem;
import net.cookietology.item.DoughItem;
import net.cookietology.item.crafting.IBakingRecipe;
import net.cookietology.registry.CookietologyItems;
import net.cookietology.registry.CookietologyRecipes;
import net.cookietology.util.CookieHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class CookieBakingRecipe implements IBakingRecipe {
    private final ResourceLocation id;

    public CookieBakingRecipe(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public boolean matches(Container container, Level level) {
        boolean dough = false;
        boolean cocoa = false;

        for (int i = 0; i < 2; ++i) {
            ItemStack slotStack = container.getItem(i);
            if (!slotStack.isEmpty()) {
                if (slotStack.is(CookietologyItems.COOKIE_SHAPED_DOUGH.get())) {
                    dough = true;
                } else {
                    if (!slotStack.is(Items.COCOA_BEANS)) {
                        return false;
                    }
                    cocoa = true;
                }
            }
        }
        return dough && cocoa;
    }

    @Override
    public ItemStack assemble(Container container) {
        DoughItem.DoughProperties doughProperties = DoughItem.DEFAULT_PROPERTIES;

        for (int i = 0; i < 2; ++i) {
            ItemStack slotStack = container.getItem(i);
            if (slotStack.is(CookietologyItems.COOKIE_SHAPED_DOUGH.get())) {
                doughProperties = CookieHelper.getDoughProperties(slotStack);
                break;
            }
        }

        return CookieItem.create(8,
                Math.abs(doughProperties.thickness() - doughProperties.brilliance()),
                doughProperties.brilliance() * (1.0F / doughProperties.thickness()),
                doughProperties.thickness() / (5 * doughProperties.brilliance()));
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
        return CookietologyRecipes.COOKIE_BAKING_SERIALIZER.get();
    }

    @Override
    public int getCookTime() {
        return 1200;
    }
}
