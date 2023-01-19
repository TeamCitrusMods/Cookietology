package net.cookietology.quilt;

import net.cookietology.fabriclike.CookietologyFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class CookietologyQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        CookietologyFabricLike.init();
    }
}
