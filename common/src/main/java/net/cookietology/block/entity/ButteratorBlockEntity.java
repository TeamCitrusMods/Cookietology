package net.cookietology.block.entity;

import net.cookietology.block.ButteratorBlock;
import net.cookietology.inventory.ButteratorMenu;
import net.cookietology.registry.CookietologyBlockEntities;
import net.cookietology.registry.CookietologyBlocks;
import net.cookietology.registry.CookietologyItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ButteratorBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    private static final int[] UPPER_SLOTS = new int[]{};
    private static final int[] LOWER_SLOTS = new int[]{1};
    private static final int[] SIDE_SLOTS = new int[]{0};
    protected final ContainerData butteratorData;
    public boolean working;
    protected NonNullList<ItemStack> itemStacks = NonNullList.withSize(2, ItemStack.EMPTY);
    protected boolean hasCooldown;
    protected int cooldownTime;
    private boolean fanWorking;
    private int milkTime;
    private int resultProgress;

    public ButteratorBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(CookietologyBlockEntities.BUTTERATOR.get(), blockPos, blockState);
    }

    protected ButteratorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState state) {
        super(blockEntityType, blockPos, state);
        this.butteratorData = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ButteratorBlockEntity.this.fanWorking ? 1 : 0;
                    case 1 -> ButteratorBlockEntity.this.milkTime;
                    case 2 -> ButteratorBlockEntity.this.resultProgress;
                    case 3 -> ButteratorBlockEntity.this.hasCooldown ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int data) {
                switch (index) {
                    case 0 -> ButteratorBlockEntity.this.fanWorking = data > 0;
                    case 1 -> ButteratorBlockEntity.this.milkTime = data;
                    case 2 -> ButteratorBlockEntity.this.resultProgress = data;
                    case 3 -> ButteratorBlockEntity.this.hasCooldown = data > 0;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, ButteratorBlockEntity butterator) {
        boolean working = butterator.working;
        ItemStack milkStack = butterator.itemStacks.get(0);

        if (butterator.milkTime + 1500 <= 9000 && milkStack.is(Items.MILK_BUCKET)) {
            butterator.milkTime += 1500;
            butterator.itemStacks.set(0, new ItemStack(Items.BUCKET));
            level.playSound(null, blockPos, SoundEvents.COW_MILK, SoundSource.BLOCKS, 0.6F, 0.8F);
            setChanged(level, blockPos, blockState);
        }

        if (butterator.fanWorking && !butterator.hasCooldown) {
            if (butterator.working) {
                --butterator.milkTime;
                ItemStack resultStack = butterator.itemStacks.get(1);

                if (resultStack.getCount() <= resultStack.getMaxStackSize()) {
                    ++butterator.resultProgress;

                    if (butterator.resultProgress >= 2250) {
                        ItemStack butterResultStack = new ItemStack(CookietologyItems.SOFT_BUTTER.get(), level.random.nextInt(1, 5));

                        if (resultStack.isEmpty()) {
                            butterator.itemStacks.set(1, butterResultStack.copy());
                        } else if (resultStack.is(butterResultStack.getItem())) {
                            resultStack.grow(Math.min(butterResultStack.getCount(), resultStack.getMaxStackSize() - resultStack.getCount()));
                        }
                        if (level.random.nextInt(100) <= 20) {
                            butterator.breakFan();
                        }
                        level.playSound(null, blockPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.6F, 0.8F);
                        butterator.spawnCooldownParticles();
                        butterator.addCooldown(300);
                        butterator.resultProgress = 0;
                        butterator.working = false;
                    }
                }
                if (butterator.milkTime <= 0) {
                    butterator.working = false;
                }
                setChanged(level, blockPos, blockState);
            } else {
                if (butterator.milkTime > 0) {
                    butterator.working = true;
                    setChanged(level, blockPos, blockState);
                }
            }
        }

        if (butterator.hasCooldown) {
            if (--butterator.cooldownTime <= 0) {
                butterator.hasCooldown = false;
            }
            setChanged(level, blockPos, blockState);
        }

        if (working != butterator.working) {
            blockState = blockState.setValue(ButteratorBlock.ACTIVE, butterator.working);
            level.setBlock(blockPos, blockState, 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("FanWorking", this.fanWorking);
        compound.putInt("MilkTime", this.milkTime);
        compound.putInt("ResultProgress", this.resultProgress);
        compound.putBoolean("OnCooldown", this.hasCooldown);
        compound.putInt("CooldownTime", this.cooldownTime);
        ContainerHelper.saveAllItems(compound, this.itemStacks);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.itemStacks);
        this.fanWorking = compound.getBoolean("FanWorking");
        this.milkTime = compound.getInt("MilkTime");
        this.resultProgress = compound.getInt("ResultProgress");
        this.hasCooldown = compound.getBoolean("OnCooldown");
        this.cooldownTime = compound.getInt("CooldownTime");
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.butterator");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
        return new ButteratorMenu(id, playerInventory, this, this.butteratorData);
    }

    @Override
    public int getContainerSize() {
        return this.itemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        return this.itemStacks.isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return this.itemStacks.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int p_18943_) {
        return ContainerHelper.removeItem(this.itemStacks, index, p_18943_);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.itemStacks, index);
    }

    @Override
    public void setItem(int index, ItemStack item) {
        this.itemStacks.set(index, item);
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clearContent() {
        this.itemStacks.clear();
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack itemStack) {
        if (index == 0) {
            return this.itemStacks.get(0).isEmpty() && itemStack.is(Items.MILK_BUCKET);
        }
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return LOWER_SLOTS;
        } else {
            return direction == Direction.UP ? UPPER_SLOTS : SIDE_SLOTS;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, Direction direction) {
        return this.canPlaceItem(index, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack itemStack, Direction direction) {
        if (direction == Direction.DOWN) {
            if (index == 0) {
                return itemStack.is(Items.BUCKET);
            } else {
                return index == 1;
            }
        }
        return false;
    }

    public void spawnCooldownParticles() {
        Direction facingDirection = this.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        Vec3 gateStep = new Vec3(facingDirection.getStepX() / 1.8D, -0.25D, facingDirection.getStepZ() / 1.8D);
        Vec3 gatePos = Vec3.atCenterOf(this.getBlockPos()).add(gateStep);

        ((ServerLevel) this.level).sendParticles(ParticleTypes.CLOUD, gatePos.x(), gatePos.y(), gatePos.z(), 5, 0.1D, 0.0D, 0.1D, 0.0D);
    }

    public void addCooldown(int cooldown) {
        if (this.hasCooldown) {
            this.cooldownTime += cooldown;
        } else {
            this.hasCooldown = true;
            this.cooldownTime = cooldown;
        }
    }

    public void breakFan() {
        BlockPos fanPos = this.getBlockPos().above();
        if (this.level.getBlockState(fanPos).is(CookietologyBlocks.BUTTERATOR_FAN.get())) {
            this.level.setBlockAndUpdate(fanPos, CookietologyBlocks.BROKEN_BUTTERATOR_FAN.get().defaultBlockState());
            this.level.playSound(null, fanPos, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.BLOCKS, 1.0F, 0.9F);
        }
    }

    public boolean isFanWorking() {
        return this.fanWorking;
    }

    public void setFanWorking(boolean fanWorking) {
        this.fanWorking = fanWorking;
    }
}
