package net.cookietology.item;

import net.cookietology.registry.CookietologyItems;
import net.cookietology.util.CookieHelper;
import net.minecraft.world.item.ItemStack;

public class CookieShapedDoughItem extends DoughItem {
    public CookieShapedDoughItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onBaked(ItemStack itemStack, ItemStack result) {
        if (result.is(CookietologyItems.COOKIE.get())) {
            DoughProperties properties = CookieHelper.getDoughProperties(itemStack);

            CookieHelper.saveCookieProperties(result,
                    Math.abs(properties.thickness() - properties.brilliance()),
                    properties.brilliance() * (1.0F / properties.thickness()),
                    properties.thickness() / (5 * properties.brilliance()));
        }
    }
}
