package net.cookietology.item;

import net.cookietology.util.CookieHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class DoughItem extends Item implements IBakeable {
    public static final DoughProperties DEFAULT_PROPERTIES = new DoughProperties(5, 1);

    public DoughItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> components, TooltipFlag tooltipFlag) {
        DoughProperties doughProperties = CookieHelper.getDoughProperties(itemStack);
        components.add(Component.translatable("dough.thickness", doughProperties.thickness()).withStyle(ChatFormatting.GRAY));
        components.add(Component.translatable("dough.brilliance", doughProperties.brilliance()).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void onBaked(ItemStack itemStack, ItemStack result) {
    }

    public record DoughProperties(int thickness, int brilliance) { }
}
