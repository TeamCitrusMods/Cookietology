package net.cookietology.compat.jei.recipe;

import net.minecraft.world.item.ItemStack;

/**
 * Butterator doesn't work with recipes, so this is needed to
 * represent its function in JEI
 */
public class ButteratorExampleRecipe {
    private final int milkTime;
    private final ItemStack output;

    public ButteratorExampleRecipe(int milkTime, ItemStack output) {
        this.milkTime = milkTime;
        this.output = output;
    }

    public int getMilkTime() {
        return milkTime;
    }

    public ItemStack getOutput() {
        return output;
    }
}
