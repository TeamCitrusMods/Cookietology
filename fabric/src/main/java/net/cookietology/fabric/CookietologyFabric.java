package net.cookietology.fabric;

import net.cookietology.fabriclike.CookietologyFabricLike;
import net.fabricmc.api.ModInitializer;

public class CookietologyFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CookietologyFabricLike.init();
    }
}
