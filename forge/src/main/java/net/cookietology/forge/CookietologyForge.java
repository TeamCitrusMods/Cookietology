package net.cookietology.forge;

import dev.architectury.platform.forge.EventBuses;
import net.cookietology.Cookietology;
import net.cookietology.CookietologyClient;
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
        event.enqueueWork(CookietologyClient::init);
    }
}
