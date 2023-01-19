package net.cookietology.quilt;

import net.cookietology.client.gui.screens.inventory.BakerScreen;
import net.cookietology.client.gui.screens.inventory.ButteratorScreen;
import net.cookietology.registry.CookietologyMenus;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

@Environment(EnvType.CLIENT)
public class CookietologyQuiltClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        MenuScreens.register(CookietologyMenus.BUTTERATOR.get(), ButteratorScreen::new);
        MenuScreens.register(CookietologyMenus.BAKER.get(), BakerScreen::new);
    }
}
