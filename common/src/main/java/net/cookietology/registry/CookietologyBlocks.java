package net.cookietology.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.cookietology.Cookietology;
import net.cookietology.block.BakerBlock;
import net.cookietology.block.ButteratorBlock;
import net.cookietology.block.ButteratorFanBlock;
import net.cookietology.block.MixingBowlBlock;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class CookietologyBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Cookietology.MODID, Registry.BLOCK_REGISTRY);

    public static final RegistrySupplier<Block> BUTTERATOR = BLOCKS.register("butterator", () -> new ButteratorBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(3.5F)));
    public static final RegistrySupplier<Block> BAKER = BLOCKS.register("baker", () -> new BakerBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(3.5F)));
    public static final RegistrySupplier<Block> BUTTERATOR_FAN = BLOCKS.register("butterator_fan", () -> new ButteratorFanBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(3.5F)));
    public static final RegistrySupplier<Block> BROKEN_BUTTERATOR_FAN = BLOCKS.register("broken_butterator_fan", () -> new ButteratorFanBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(1.0F)));
    public static final RegistrySupplier<Block> MIXING_BOWL = BLOCKS.register("mixing_bowl", () -> new MixingBowlBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(1.0F)));
}
