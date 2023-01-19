package net.cookietology.block;

import net.cookietology.block.entity.BakerBlockEntity;
import net.cookietology.registry.CookietologyBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BakerBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public BakerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));

    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BakerBlockEntity(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : createTickerHelper(blockEntityType, CookietologyBlockEntities.BAKER.get(), BakerBlockEntity::serverTick);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof BakerBlockEntity) {
                player.openMenu((MenuProvider)blockEntity);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource random) {
        if (blockState.getValue(LIT)) {
            Direction facingDirection = blockState.getValue(HorizontalDirectionalBlock.FACING);
            Direction.Axis directionAxis = facingDirection.getAxis();
            double xModifier = directionAxis == Direction.Axis.X ? 0.0D : random.nextDouble() * 0.6D - 0.3D;
            double zModifier = directionAxis == Direction.Axis.Z ? 0.0D : random.nextDouble() * 0.6D - 0.3D;

            Vec3 fireStep = new Vec3(facingDirection.getStepX() / 1.8D + xModifier, -0.3D, facingDirection.getStepZ() / 1.8D + zModifier);
            Vec3 firePos = Vec3.atCenterOf(blockPos).add(fireStep);

            level.addParticle(ParticleTypes.SMOKE, firePos.x(), firePos.y(), firePos.z(), 0.0D, 0.0D, 0.0D);

            if (random.nextDouble() < 0.1D) {
                level.playLocalSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 0.8F, false);
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        return this.defaultBlockState().setValue(FACING, placeContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof BakerBlockEntity baker) {
                baker.setCustomName(itemStack.getHoverName());
            }
        }
    }

    @Override
    public void onRemove(BlockState p_60515_, Level level, BlockPos blockPos, BlockState p_60518_, boolean p_60519_) {
        if (!p_60515_.is(p_60518_.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof BakerBlockEntity baker) {
                Containers.dropContents(level, blockPos, baker);
                level.updateNeighbourForOutputSignal(blockPos, this);
            }
            super.onRemove(p_60515_, level, blockPos, p_60518_, p_60519_);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(blockPos));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }
}
