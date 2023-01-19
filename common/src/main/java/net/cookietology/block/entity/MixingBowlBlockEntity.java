package net.cookietology.block.entity;

import net.cookietology.item.crafting.IMixingRecipe;
import net.cookietology.registry.CookietologyBlockEntities;
import net.cookietology.registry.CookietologyRecipes;
import net.cookietology.registry.CookietologySounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class MixingBowlBlockEntity extends BlockEntity implements Clearable {
    protected final NonNullList<ItemStack> itemStacks = NonNullList.withSize(6, ItemStack.EMPTY);
    protected int mixAttempts;

    public MixingBowlBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CookietologyBlockEntities.MIXING_BOWL.get(), blockPos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, this.itemStacks);
        compoundTag.putInt("MixAttempts", this.mixAttempts);
    }


    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.itemStacks.clear();
        ContainerHelper.loadAllItems(compoundTag, this.itemStacks);
        this.mixAttempts = compoundTag.getInt("MixAttempts");
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag updateTag = new CompoundTag();
        ContainerHelper.saveAllItems(updateTag, this.itemStacks, true);
        return updateTag;
    }

    @Override
    public void clearContent() {
        this.itemStacks.clear();
    }

    public boolean placeStack(ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            for (int i = 0; i < this.itemStacks.size(); ++i) {
                ItemStack currentStack = this.itemStacks.get(i);
                if (currentStack.isEmpty()) {
                    this.itemStacks.set(i, itemStack.split(1));
                    this.mixAttempts = 0;
                    this.markUpdated();
                    return true;
                }
            }
        }
        return false;
    }

    public InteractionResult mixAttempt(Level level, Player player, InteractionHand interactionHand) {
        if (!this.itemStacks.stream().allMatch(ItemStack::isEmpty)) {
            Optional<IMixingRecipe> recipe = this.getRecipe();

            if (recipe.isPresent()) {
                if (!level.isClientSide()) {
                    if (++mixAttempts >= recipe.get().getAttempts()) {
                        Container container = new SimpleContainer(this.itemStacks.toArray(ItemStack[]::new));
                        ItemStack result = recipe.get().assemble(container);

                        Containers.dropItemStack(level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), result);
                        result.onCraftedBy(level, player, result.getCount());
                        this.clearContent();
                        this.mixAttempts = 0;
                    }
                    this.level.playSound(null, this.getBlockPos(), CookietologySounds.MIX.get(), SoundSource.BLOCKS, 0.5F, 0.6F + level.getRandom().nextFloat());
                    this.spawnMixParticles();
                    this.markUpdated();
                }
            } else {
                Container container = new SimpleContainer(this.itemStacks.toArray(ItemStack[]::new));
                Containers.dropContents(level, this.getBlockPos(), container);
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide());
        }
        return InteractionResult.CONSUME;
    }

    public Optional<IMixingRecipe> getRecipe() {
        return this.level.getRecipeManager().getRecipeFor(CookietologyRecipes.MIXING.get(), new SimpleContainer(this.itemStacks.toArray(ItemStack[]::new)), this.level);
    }

    public NonNullList<ItemStack> getItemStacks() {
        return this.itemStacks;
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public void spawnMixParticles() {
        if (this.level != null && !this.level.isClientSide()) {
            for (ItemStack stack : this.itemStacks) {
                if (!stack.isEmpty()) {
                    ((ServerLevel) this.level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 0.2D, this.getBlockPos().getZ() + 0.5D, 3, 0.15D, 0.3D, 0.15D, 0.04D);
                }
            }
        }
    }
}
