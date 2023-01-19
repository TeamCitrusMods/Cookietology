package net.cookietology.item;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class MoldItem extends Item {
    private final Item moldResult;

    public MoldItem(Properties properties, Item moldResult) {
        super(properties);
        this.moldResult = moldResult;
    }

    public Item getMoldResult() {
        return moldResult;
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
