package net.cookietology.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.cookietology.Cookietology;
import net.cookietology.block.entity.BakerBlockEntity;
import net.cookietology.block.entity.ButteratorBlockEntity;
import net.cookietology.block.entity.MixingBowlBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CookietologyBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Cookietology.MODID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);

    public static final RegistrySupplier<BlockEntityType<ButteratorBlockEntity>> BUTTERATOR = BLOCK_ENTITIES.register("butterator", () -> BlockEntityType.Builder.of(ButteratorBlockEntity::new, CookietologyBlocks.BUTTERATOR.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<BakerBlockEntity>> BAKER = BLOCK_ENTITIES.register("baker", () -> BlockEntityType.Builder.of(BakerBlockEntity::new, CookietologyBlocks.BAKER.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<MixingBowlBlockEntity>> MIXING_BOWL = BLOCK_ENTITIES.register("mixing_bowl", () -> BlockEntityType.Builder.of(MixingBowlBlockEntity::new, CookietologyBlocks.MIXING_BOWL.get()).build(null));
}
