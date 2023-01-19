package net.cookietology.inventory;

import dev.architectury.registry.fuel.FuelRegistry;
import net.cookietology.registry.CookietologyMenus;
import net.cookietology.registry.CookietologyRecipes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BakerMenu extends AbstractContainerMenu {
    protected final Level level;
    private final Container container;
    private final ContainerData data;

    public BakerMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(4), new SimpleContainerData(4));
    }

    public BakerMenu(int id, Inventory inventory, Container container, ContainerData containerData) {
        super(CookietologyMenus.BAKER.get(), id);
        checkContainerSize(container, 4);
        checkContainerDataCount(containerData, 4);
        this.container = container;
        this.data = containerData;
        this.level = inventory.player.level;
        this.addSlot(new Slot(container, 0, 47, 17));
        this.addSlot(new Slot(container, 1, 65, 17));
        this.addSlot(new FuelSlot(container, 2, 56, 53));
        this.addSlot(new FurnaceResultSlot(inventory.player, container, 3, 116, 35));

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

    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();

            if (slotIndex == 0 || slotIndex == 1 || slotIndex == 3) {
                if (!this.moveItemStackTo(slotStack, 4, 40, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex == 2) {
                if (!this.moveItemStackTo(slotStack, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, itemStack);
            } else {
                if (this.isFuel(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex < 40) {
                    if (!this.moveItemStackTo(slotStack, 0, 2, false)) {
                        if (slotIndex < 31) {
                            if (!this.moveItemStackTo(slotStack, 31, 40, false)) {
                                return ItemStack.EMPTY;
                            }
                        } else {
                            if (!this.moveItemStackTo(slotStack, 4, 31, false)) {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
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

    public boolean isFuel(ItemStack itemStack) {
        return FuelRegistry.get(itemStack) > 0;
    }

    public int getBurnProgress() {
        int i = this.data.get(2);
        int j = this.data.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public int getLitProgress() {
        int i = this.data.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.data.get(0) * 13 / i;
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }

    class FuelSlot extends Slot {
        public FuelSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack itemStack) {
            return BakerMenu.this.isFuel(itemStack);
        }

        @Override
        public int getMaxStackSize(ItemStack itemStack) {
            return itemStack.is(Items.BUCKET) ? 1 : super.getMaxStackSize(itemStack);
        }
    }
}
