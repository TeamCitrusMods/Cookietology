package net.cookietology.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.cookietology.Cookietology;
import net.cookietology.inventory.BakerMenu;
import net.cookietology.inventory.ButteratorMenu;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

public class CookietologyMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Cookietology.MODID, Registry.MENU_REGISTRY);

    public static final RegistrySupplier<MenuType<ButteratorMenu>> BUTTERATOR = MENUS.register("butterator", () -> new MenuType<>(ButteratorMenu::new));
    public static final RegistrySupplier<MenuType<BakerMenu>> BAKER = MENUS.register("baker", () -> new MenuType<>(BakerMenu::new));
}
