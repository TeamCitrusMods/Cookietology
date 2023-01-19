package net.cookietology.block.entity;

import dev.architectury.registry.fuel.FuelRegistry;
import net.cookietology.block.BakerBlock;
import net.cookietology.inventory.BakerMenu;
import net.cookietology.item.crafting.IBakingRecipe;
import net.cookietology.registry.CookietologyBlockEntities;
import net.cookietology.registry.CookietologyRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BakerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    private static final int[] UPPER_SLOTS = new int[]{0, 1};
    private static final int[] LOWER_SLOTS = new int[]{3, 2};
    private static final int[] SIDE_SLOTS = new int[]{2};
    protected final ContainerData bakerData;
    protected NonNullList<ItemStack> itemStacks = NonNullList.withSize(4, ItemStack.EMPTY);
    private int litTime;
    private int litDuration;
    private int cookingProgress;
    private int cookingTotalTime;

    public BakerBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(CookietologyBlockEntities.BAKER.get(), blockPos, blockState);
    }

    protected BakerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.bakerData = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> BakerBlockEntity.this.litTime;
                    case 1 -> BakerBlockEntity.this.litDuration;
                    case 2 -> BakerBlockEntity.this.cookingProgress;
                    case 3 -> BakerBlockEntity.this.cookingTotalTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int data) {
                switch (index) {
                    case 0 -> BakerBlockEntity.this.litTime = data;
                    case 1 -> BakerBlockEntity.this.litDuration = data;
                    case 2 -> BakerBlockEntity.this.cookingProgress = data;
                    case 3 -> BakerBlockEntity.this.cookingTotalTime = data;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, BakerBlockEntity baker) {
        boolean lit = baker.isLit();
        boolean changing = false;
        ItemStack fuelStack = baker.itemStacks.get(2);

        if (baker.isLit() || !fuelStack.isEmpty() && !(baker.itemStacks.get(0).isEmpty() && baker.itemStacks.get(1).isEmpty())) {
            IBakingRecipe recipe = level.getRecipeManager().getRecipeFor(CookietologyRecipes.BAKING.get(), baker, level).orElse(null);

            if (!baker.isLit() && baker.canBurn(recipe, baker.itemStacks, baker.getMaxStackSize())) {
                baker.litTime = baker.getBurnDuration(fuelStack);
                baker.litDuration = baker.litTime;
                if (baker.isLit()) {
                    changing = true;
                    if (fuelStack.getItem().hasCraftingRemainingItem())
                        baker.itemStacks.set(2, new ItemStack(fuelStack.getItem().getCraftingRemainingItem()));
                    else if (!fuelStack.isEmpty()) {
                        fuelStack.shrink(1);
                        if (fuelStack.isEmpty()) {
                            baker.itemStacks.set(2, new ItemStack(fuelStack.getItem().getCraftingRemainingItem()));
                        }
                    }
                }
            }

            if (baker.isLit() && baker.canBurn(recipe, baker.itemStacks, baker.getMaxStackSize())) {
                ++baker.cookingProgress;
                if (baker.cookingProgress == baker.cookingTotalTime) {
                    baker.cookingProgress = 0;
                    baker.cookingTotalTime = getTotalCookTime(level, baker);
                    baker.burn(recipe, baker.itemStacks, baker.getMaxStackSize());

                }
            } else {
                baker.cookingProgress = 0;
            }
        } else if (!baker.isLit() && baker.cookingProgress > 0) {
            baker.cookingProgress = Mth.clamp(baker.cookingProgress - 2, 0, baker.cookingTotalTime);
        }

        if (baker.isLit()) {
            --baker.litTime;
        }

        if (lit != baker.isLit()) {
            changing = true;
            blockState = blockState.setValue(BakerBlock.LIT, baker.isLit());
            level.setBlock(blockPos, blockState, 3);
        }

        if (changing) {
            setChanged(level, blockPos, blockState);
        }
    }

    private static int getTotalCookTime(Level level, Container container) {
        return level.getRecipeManager().getRecipeFor(CookietologyRecipes.BAKING.get(), container, level).map(IBakingRecipe::getCookTime).orElse(200);
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("BurnTime", this.litTime);
        compound.putInt("CookTime", this.cookingProgress);
        compound.putInt("CookTimeTotal", this.cookingTotalTime);
        ContainerHelper.saveAllItems(compound, this.itemStacks);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.itemStacks);
        this.litTime = compound.getInt("BurnTime");
        this.cookingProgress = compound.getInt("CookTime");
        this.cookingTotalTime = compound.getInt("CookTimeTotal");
        this.litDuration = this.getBurnDuration(this.itemStacks.get(1));
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.baker");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
        return new BakerMenu(id, playerInventory, this, this.bakerData);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return LOWER_SLOTS;
        } else {
            return direction == Direction.UP ? UPPER_SLOTS : SIDE_SLOTS;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int slotIndex, ItemStack itemStack, Direction direction) {
        return this.canPlaceItem(slotIndex, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slotIndex, ItemStack itemStack, Direction direction) {
        if (direction == Direction.DOWN && slotIndex == 2) {
            return itemStack.is(Items.BUCKET);
        } else {
            return true;
        }
    }

    @Override
    public int getContainerSize() {
        return this.itemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        return this.itemStacks.isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return this.itemStacks.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int p_18943_) {
        return ContainerHelper.removeItem(this.itemStacks, index, p_18943_);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.itemStacks, index);
    }

    @Override
    public void clearContent() {
        this.itemStacks.clear();
    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    private boolean canBurn(IBakingRecipe recipe, NonNullList<ItemStack> itemStacks, int count) {
        if (recipe != null && !(itemStacks.get(0).isEmpty() && itemStacks.get(1).isEmpty())) {
            ItemStack recipeResult = recipe.assemble(this);
            if (recipeResult.isEmpty()) {
                return false;
            } else {
                ItemStack resultStack = itemStacks.get(3);
                if (resultStack.isEmpty()) {
                    return true;
                } else if (!resultStack.sameItem(recipeResult)) {
                    return false;
                } else if (resultStack.getCount() + recipeResult.getCount() <= count && resultStack.getCount() + recipeResult.getCount() <= resultStack.getMaxStackSize()) {
                    return true;
                } else {
                    return resultStack.getCount() + recipeResult.getCount() <= recipeResult.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    private void burn(IBakingRecipe recipe, NonNullList<ItemStack> itemStacks, int count) {
        if (this.canBurn(recipe, itemStacks, count)) {
            List<ItemStack> ingredientStacks = List.of(itemStacks.get(0), itemStacks.get(1));
            ItemStack recipeResult = recipe.assemble(this);
            ItemStack resultStack = itemStacks.get(3);

            if (resultStack.isEmpty()) {
                itemStacks.set(3, recipeResult.copy());
            } else if (resultStack.is(recipeResult.getItem())) {
                resultStack.grow(recipeResult.getCount());
            }

            for (ItemStack stack : ingredientStacks) {
                if (!stack.isEmpty()) {
                    stack.shrink(1);
                }
            }
        }
    }

    protected int getBurnDuration(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        } else {
            return FuelRegistry.get(itemStack);
        }
    }

    public void setItem(int index, ItemStack itemStack) {
        ItemStack itemstack = this.itemStacks.get(index);
        boolean flag = !itemStack.isEmpty() && itemStack.sameItem(itemstack) && ItemStack.tagMatches(itemStack, itemstack);
        this.itemStacks.set(index, itemStack);
        if (itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }

        if ((index == 0 || index == 1) && !flag) {
            this.cookingTotalTime = getTotalCookTime(this.level, this);
            this.cookingProgress = 0;
            this.setChanged();
        }
    }

    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    public boolean canPlaceItem(int index, ItemStack itemStack) {
        if (index == 3) {
            return false;
        } else if (index != 2) {
            return true;
        } else {
            ItemStack fuelStack = this.itemStacks.get(2);
            return FuelRegistry.get(itemStack) > 0 || itemStack.is(Items.BUCKET) && !fuelStack.is(Items.BUCKET);
        }
    }
}
