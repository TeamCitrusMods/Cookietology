package net.cookietology.item;

import net.minecraft.world.item.ItemStack;

/**
 * Implementing this on an item that can conform some baking recipe
 * will make it able to modify the resulting stack.
 */
public interface IBakeable {

    void onBaked(ItemStack itemStack, ItemStack result);
}
