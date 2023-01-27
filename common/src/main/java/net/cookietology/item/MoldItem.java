package net.cookietology.item;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class MoldItem extends Item {

    public MoldItem(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public Item getCraftingRemainingItem() {
        return this;
    }

    @Override
    public boolean hasCraftingRemainingItem() {
        return true;
    }
}
