package net.cookietology.forge;

import dev.architectury.platform.forge.EventBuses;
import net.cookietology.Cookietology;
import net.cookietology.client.gui.screens.inventory.BakerScreen;
import net.cookietology.client.gui.screens.inventory.ButteratorScreen;
import net.cookietology.registry.CookietologyMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Cookietology.MODID)
public class CookietologyForge {
    public CookietologyForge() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Cookietology.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        Cookietology.init();
        eventBus.addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(CookietologyMenus.BUTTERATOR.get(), ButteratorScreen::new);
            MenuScreens.register(CookietologyMenus.BAKER.get(), BakerScreen::new);
        });
    }
}
