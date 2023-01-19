package net.cookietology.block;

import net.cookietology.block.entity.MixingBowlBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.Stream;

public class MixingBowlBlock extends BaseEntityBlock {
    protected static final VoxelShape SHAPE = Stream.of(
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(0, 0, 0, 2, 14, 16),
            Block.box(14, 0, 0, 16, 14, 16),
            Block.box(0, 0, 0, 16, 14, 2),
            Block.box(0, 0, 14, 16, 14, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public MixingBowlBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MixingBowlBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof MixingBowlBlockEntity mixingBowl) {
            ItemStack handStack = player.getItemInHand(interactionHand);

            if (player.isCrouching()) {
                return mixingBowl.mixAttempt(level, player, interactionHand);
            } else {
                if (!level.isClientSide() && mixingBowl.placeStack(player.getAbilities().instabuild ? handStack.copy() : handStack)) {
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState p_60515_, Level level, BlockPos blockPos, BlockState p_60518_, boolean p_60519_) {
        if (!p_60515_.is(p_60518_.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof MixingBowlBlockEntity mixingBowl) {
                Containers.dropContents(level, blockPos, mixingBowl.getItemStacks());
            }
            super.onRemove(p_60515_, level, blockPos, p_60518_, p_60519_);
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}
