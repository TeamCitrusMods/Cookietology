package net.cookietology.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.cookietology.Cookietology;
import net.cookietology.item.CookieItem;
import net.cookietology.item.CookieShapedDoughItem;
import net.cookietology.item.DoughItem;
import net.cookietology.item.MoldItem;
import net.minecraft.core.Registry;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CookietologyItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Cookietology.MODID, Registry.ITEM_REGISTRY);

    public static final RegistrySupplier<Item> SOFT_BUTTER = ITEMS.register("soft_butter", () -> new Item(new Item.Properties().tab(Cookietology.CREATIVE_TAB)));
    public static final RegistrySupplier<Item> COOKIE = ITEMS.register("cookie", () -> new CookieItem((new Item.Properties()).tab(Cookietology.CREATIVE_TAB).food(Foods.COOKIE)));
    public static final RegistrySupplier<Item> FLOUR = ITEMS.register("flour", () -> new Item((new Item.Properties()).tab(Cookietology.CREATIVE_TAB)));
    public static final RegistrySupplier<Item> SOFT_DOUGH = ITEMS.register("soft_dough", () -> new DoughItem((new Item.Properties()).tab(Cookietology.CREATIVE_TAB)));
    public static final RegistrySupplier<Item> COOKIE_SHAPED_DOUGH = ITEMS.register("cookie_shaped_dough", () -> new CookieShapedDoughItem((new Item.Properties()).tab(Cookietology.CREATIVE_TAB)));
    public static final RegistrySupplier<Item> COOKIE_MOLD = ITEMS.register("cookie_mold", () -> new MoldItem((new Item.Properties()).tab(Cookietology.CREATIVE_TAB)));
    public static final RegistrySupplier<Item> SIMPLE_DISPLAY = ITEMS.register("simple_display", () -> new Item((new Item.Properties()).tab(Cookietology.CREATIVE_TAB)));

    public static final RegistrySupplier<Item> BUTTERATOR = ITEMS.register("butterator", () -> new BlockItem(CookietologyBlocks.BUTTERATOR.get(), new Item.Properties().tab(Cookietology.CREATIVE_TAB)));
    public static final RegistrySupplier<Item> BAKER = ITEMS.register("baker", () -> new BlockItem(CookietologyBlocks.BAKER.get(), new Item.Properties().tab(Cookietology.CREATIVE_TAB)));
    public static final RegistrySupplier<Item> BUTTERATOR_FAN = ITEMS.register("butterator_fan", () -> new BlockItem(CookietologyBlocks.BUTTERATOR_FAN.get(), new Item.Properties().tab(Cookietology.CREATIVE_TAB)));
    public static final RegistrySupplier<Item> BROKEN_BUTTERATOR_FAN = ITEMS.register("broken_butterator_fan", () -> new BlockItem(CookietologyBlocks.BROKEN_BUTTERATOR_FAN.get(), new Item.Properties().tab(Cookietology.CREATIVE_TAB)));
    public static final RegistrySupplier<Item> MIXING_BOWL = ITEMS.register("mixing_bowl", () -> new BlockItem(CookietologyBlocks.MIXING_BOWL.get(), new Item.Properties().tab(Cookietology.CREATIVE_TAB)));
}
