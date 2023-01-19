package net.cookietology.util;

import net.cookietology.item.CookieItem;
import net.cookietology.item.DoughItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class CookieHelper {
    public static CookieItem.CookieProperties getCookieProperties(ItemStack itemStack) {
        CompoundTag stackTag = itemStack.getTag();

        if (stackTag != null && stackTag.contains("CookieProperties")) {
            CompoundTag propertiesTag = stackTag.getCompound("CookieProperties");

            return new CookieItem.CookieProperties(
                    propertiesTag.getInt("EatSpeed"),
                    propertiesTag.getFloat("SaturationModifier"),
                    propertiesTag.getInt("Nutrition"));
        } else {
            return CookieItem.DEFAULT_PROPERTIES;
        }
    }

    public static void saveCookieProperties(ItemStack itemStack, int eatSpeed, float saturation, int nutrition) {
        CompoundTag stackTag = itemStack.getOrCreateTag();
        CompoundTag propertiesTag = new CompoundTag();

        propertiesTag.putInt("EatSpeed", eatSpeed);
        propertiesTag.putFloat("SaturationModifier", saturation);
        propertiesTag.putInt("Nutrition", nutrition);

        stackTag.put("CookieProperties", propertiesTag);
    }

    public static DoughItem.DoughProperties getDoughProperties(ItemStack itemStack) {
        CompoundTag stackTag = itemStack.getTag();

        if (stackTag != null && stackTag.contains("DoughProperties")) {
            CompoundTag propertiesTag = stackTag.getCompound("DoughProperties");

            return new DoughItem.DoughProperties(
                    propertiesTag.getInt("Thickness"),
                    propertiesTag.getInt("Brilliance"));
        } else {
            return DoughItem.DEFAULT_PROPERTIES;
        }
    }

    public static void saveDoughProperties(ItemStack itemStack, int thickness, int brilliance) {
        CompoundTag stackTag = itemStack.getOrCreateTag();
        CompoundTag propertiesTag = new CompoundTag();

        propertiesTag.putInt("Thickness", thickness);
        propertiesTag.putInt("Brilliance", brilliance);

        stackTag.put("DoughProperties", propertiesTag);
    }
}
