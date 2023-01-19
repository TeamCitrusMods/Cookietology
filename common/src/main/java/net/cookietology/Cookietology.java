package net.cookietology;

import dev.architectury.registry.CreativeTabRegistry;
import net.cookietology.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Cookietology {
    public static final String MODID = "cookietology";
    public static final CreativeModeTab CREATIVE_TAB = CreativeTabRegistry.create(new ResourceLocation(MODID, MODID), () -> new ItemStack(Items.COOKIE)).setBackgroundSuffix("cookietology.png");
    
    public static void init() {
        CookietologyBlocks.BLOCKS.register();
        CookietologyItems.ITEMS.register();
        CookietologyBlockEntities.BLOCK_ENTITIES.register();
        CookietologyMenus.MENUS.register();
        CookietologyRecipes.RECIPE_SERIALIZERS.register();
        CookietologyRecipes.RECIPE_TYPES.register();
        CookietologySounds.SOUNDS.register();
    }
}
