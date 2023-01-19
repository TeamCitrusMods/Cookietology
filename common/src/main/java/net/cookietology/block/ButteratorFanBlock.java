package net.cookietology.block;

import net.cookietology.registry.CookietologyBlocks;
import net.cookietology.registry.CookietologySounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ButteratorFanBlock extends Block {
    public ButteratorFanBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
        ItemStack handStack = player.getItemInHand(interactionHand);
        if (handStack.is(Items.IRON_INGOT) && blockState.is(CookietologyBlocks.BROKEN_BUTTERATOR_FAN.get())) {
            level.setBlockAndUpdate(blockPos, CookietologyBlocks.BUTTERATOR_FAN.get().defaultBlockState());
            level.playSound(null, blockPos, CookietologySounds.FAN_REPAIR.get(), SoundSource.BLOCKS, 0.8F, 0.9F);
            if (!player.getAbilities().instabuild) {
                handStack.shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource random) {
        if (level.getBlockState(blockPos).is(CookietologyBlocks.BROKEN_BUTTERATOR_FAN.get())) {
            if (random.nextDouble() <= 0.4D) {
                level.addParticle(ParticleTypes.LARGE_SMOKE, blockPos.getX() + random.nextDouble(), blockPos.getY() + 1.0D, blockPos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
