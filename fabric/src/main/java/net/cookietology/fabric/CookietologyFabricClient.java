package net.cookietology.fabric;

import net.cookietology.fabriclike.CookietologyFabricLikeClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class CookietologyFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CookietologyFabricLikeClient.init();
    }
}
