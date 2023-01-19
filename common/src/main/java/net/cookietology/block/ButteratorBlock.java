package net.cookietology.block;

import net.cookietology.block.entity.ButteratorBlockEntity;
import net.cookietology.registry.CookietologyBlockEntities;
import net.cookietology.registry.CookietologyBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ButteratorBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public ButteratorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(ACTIVE, false));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ButteratorBlockEntity(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : createTickerHelper(blockEntityType, CookietologyBlockEntities.BUTTERATOR.get(), ButteratorBlockEntity::serverTick);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof ButteratorBlockEntity) {
                player.openMenu((MenuProvider) blockEntity);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        return this.defaultBlockState().setValue(FACING, placeContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof ButteratorBlockEntity butterator) {
                butterator.setCustomName(itemStack.getHoverName());
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState facingState, LevelAccessor level, BlockPos blockPos, BlockPos facingPos) {
        this.updateFanState(level, blockPos);
        return super.updateShape(blockState, direction, facingState, level, blockPos, facingPos);
    }

    @Override
    public void onPlace(BlockState p_60566_, Level level, BlockPos blockPos, BlockState p_60569_, boolean p_60570_) {
        this.updateFanState(level, blockPos);
    }

    @Override
    public void onRemove(BlockState p_60515_, Level level, BlockPos blockPos, BlockState p_60518_, boolean p_60519_) {
        if (!p_60515_.is(p_60518_.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof ButteratorBlockEntity butterator) {
                Containers.dropContents(level, blockPos, butterator);
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
        builder.add(FACING, ACTIVE);
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    public void updateFanState(LevelAccessor level, BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof ButteratorBlockEntity butterator) {
            butterator.setFanWorking(level.getBlockState(blockPos.above()).is(CookietologyBlocks.BUTTERATOR_FAN.get()));
        }
    }
}
