package net.cookietology.item.crafting;

import com.google.gson.JsonObject;
import net.cookietology.item.DoughItem;
import net.cookietology.registry.CookietologyItems;
import net.cookietology.registry.CookietologyRecipes;
import net.cookietology.util.CookieHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class MoldingRecipe implements CraftingRecipe {
    protected final Ingredient mold;
    protected final ItemStack result;
    private final ResourceLocation id;

    public MoldingRecipe(ResourceLocation id, Ingredient mold, ItemStack result) {
        this.mold = mold;
        this.result = result;
        this.id = id;
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        boolean mold = false;
        boolean dough = false;

        for (int k = 0; k < craftingContainer.getContainerSize(); ++k) {
            ItemStack slotStack = craftingContainer.getItem(k);
            if (!slotStack.isEmpty()) {
                if (!mold && this.mold.test(slotStack)) {
                    mold = true;
                } else {
                    if (!slotStack.is(CookietologyItems.SOFT_DOUGH.get())) {
                        return false;
                    }
                    dough = true;
                }
            }
        }
        return mold && dough;
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer) {
        DoughItem.DoughProperties doughProperties = DoughItem.DEFAULT_PROPERTIES;

        for (int i = 0; i < craftingContainer.getContainerSize(); ++i) {
            ItemStack slotStack = craftingContainer.getItem(i);
            if (!slotStack.isEmpty()) {

                if (slotStack.is(CookietologyItems.SOFT_DOUGH.get())) {
                    doughProperties = CookieHelper.getDoughProperties(slotStack);
                    break;
                }
            }
        }

        ItemStack resultStack = this.result.copy();
        CookieHelper.saveDoughProperties(resultStack, doughProperties.thickness(), doughProperties.brilliance());

        return resultStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, Ingredient.of(CookietologyItems.SOFT_DOUGH.get()), this.mold);
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CookietologyRecipes.MOLDING_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<MoldingRecipe> {

        @Override
        public MoldingRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
            Ingredient mold = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "mold"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            return new MoldingRecipe(id, mold, result);
        }

        @Override
        public MoldingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf byteBuffer) {
            Ingredient mold = Ingredient.fromNetwork(byteBuffer);
            ItemStack result = byteBuffer.readItem();
            return new MoldingRecipe(id, mold, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf byteBuffer, MoldingRecipe moldingRecipe) {
            moldingRecipe.mold.toNetwork(byteBuffer);
            byteBuffer.writeItem(moldingRecipe.getResultItem());
        }
    }
}
