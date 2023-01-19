package net.cookietology.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.cookietology.Cookietology;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class CookietologySounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Cookietology.MODID, Registry.SOUND_EVENT_REGISTRY);

    public static final RegistrySupplier<SoundEvent> FAN_REPAIR = SOUNDS.register("block.butterator_fan.repair", () -> new SoundEvent(new ResourceLocation(Cookietology.MODID, "block.butterator_fan.repair")));
    public static final RegistrySupplier<SoundEvent> MIX = SOUNDS.register("block.mixing_bowl.mix", () -> new SoundEvent(new ResourceLocation(Cookietology.MODID, "block.mixing_bowl.mix")));
}
