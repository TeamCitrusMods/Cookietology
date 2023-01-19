package net.cookietology;

import dev.architectury.registry.menu.MenuRegistry;
import net.cookietology.client.gui.screens.inventory.BakerScreen;
import net.cookietology.client.gui.screens.inventory.ButteratorScreen;
import net.cookietology.client.renderer.blockentity.MixingBowlRenderer;
import net.cookietology.registry.CookietologyBlockEntities;
import net.cookietology.registry.CookietologyMenus;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class CookietologyClient {
    public static void init() {
        MenuRegistry.registerScreenFactory(CookietologyMenus.BUTTERATOR.get(), ButteratorScreen::new);
        MenuRegistry.registerScreenFactory(CookietologyMenus.BAKER.get(), BakerScreen::new);
        BlockEntityRenderers.register(CookietologyBlockEntities.MIXING_BOWL.get(), MixingBowlRenderer::new);
    }
}
