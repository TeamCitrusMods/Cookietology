package net.cookietology.inventory;

import net.cookietology.registry.CookietologyMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ButteratorMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData containerData;

    public ButteratorMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(2), new SimpleContainerData(4));
    }

    public ButteratorMenu(int id, Inventory inventory, Container container, ContainerData containerData) {
        super(CookietologyMenus.BUTTERATOR.get(), id);
        checkContainerSize(container, 2);
        checkContainerDataCount(containerData, 4);
        this.container = container;
        this.containerData = containerData;
        this.addSlot(new MilkSlot(container, 0, 20, 28));
        this.addSlot(new ResultSlot(inventory.player, container, 1, 80, 55));

        for (int inventoryRow = 0; inventoryRow < 3; inventoryRow++) {
            for (int inventoryCol = 0; inventoryCol < 9; inventoryCol++) {
                this.addSlot(new Slot(inventory, inventoryCol + inventoryRow * 9 + 9, 8 + inventoryCol * 18, 84 + inventoryRow * 18));
            }
        }

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            this.addSlot(new Slot(inventory, hotbarSlot, 8 + hotbarSlot * 18, 142));
        }

        this.addDataSlots(containerData);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();

            if (slotIndex == 0) {
                if (!this.moveItemStackTo(slotStack, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex == 1) {
                if (!this.moveItemStackTo(slotStack, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, itemStack);
            } else {
                if (slotStack.is(Items.MILK_BUCKET)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex < 29) {
                    if (!this.moveItemStackTo(slotStack, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex < 38 && !this.moveItemStackTo(slotStack, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }
        return itemStack;
    }

    public boolean hasWorkingFan() {
        return this.containerData.get(0) > 0;
    }

    public int getMilkTime() {
        return this.containerData.get(1);
    }

    public int getResultProgress() {
        return this.containerData.get(2);
    }

    public boolean hasCooldown() {
        return this.containerData.get(3) > 0;
    }

    static class MilkSlot extends Slot {
        public MilkSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack itemStack) {
            return itemStack.is(Items.MILK_BUCKET);
        }

        @Override
        public int getMaxStackSize(ItemStack itemStack) {
            return itemStack.is(Items.BUCKET) ? 1 : super.getMaxStackSize(itemStack);
        }
    }

    static class ResultSlot extends Slot {
        private final Player player;
        private int removeCount;

        public ResultSlot(Player player, Container container, int slot, int x, int y) {
            super(container, slot, x, y);
            this.player = player;
        }

        @Override
        public boolean mayPlace(ItemStack itemStack) {
            return false;
        }

        @Override
        public ItemStack remove(int p_40227_) {
            if (this.hasItem()) {
                this.removeCount += Math.min(p_40227_, this.getItem().getCount());
            }
            return super.remove(p_40227_);
        }

        @Override
        public void onTake(Player player, ItemStack itemStack) {
            itemStack.onCraftedBy(player.level, player, this.removeCount);
            this.removeCount = 0;
            super.onTake(player, itemStack);
        }

        @Override
        protected void onQuickCraft(ItemStack itemStack, int p_40233_) {
            itemStack.onCraftedBy(this.player.level, this.player, this.removeCount + p_40233_);
            this.removeCount = 0;
        }
    }
}
