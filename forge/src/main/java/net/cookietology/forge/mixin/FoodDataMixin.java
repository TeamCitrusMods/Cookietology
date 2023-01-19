package net.cookietology.forge.mixin;

import net.cookietology.item.CookieItem;
import net.cookietology.registry.CookietologyItems;
import net.cookietology.util.CookieHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FoodData.class, remap = false)
public abstract class FoodDataMixin {
    @Shadow public abstract void eat(int i, float f);

    @Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"), cancellable = true)
    private void handleCookie(Item item, ItemStack itemStack, LivingEntity entity, CallbackInfo ci) {
        if(item.isEdible() && item == CookietologyItems.COOKIE.get()) {
            CookieItem.CookieProperties cookieProperties = CookieHelper.getCookieProperties(itemStack);
            this.eat(cookieProperties.nutrition(), cookieProperties.saturation());
            ci.cancel();
        }
    }
}
