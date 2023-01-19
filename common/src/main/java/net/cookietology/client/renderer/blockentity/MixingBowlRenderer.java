package net.cookietology.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.cookietology.block.entity.MixingBowlBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class MixingBowlRenderer implements BlockEntityRenderer<MixingBowlBlockEntity> {
    public MixingBowlRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(MixingBowlBlockEntity mixingBowl, float p_112308_, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_112311_, int p_112312_) {
        NonNullList<ItemStack> itemStacks = mixingBowl.getItemStacks();

        for (int i = 0; i < itemStacks.size(); ++i) {
            ItemStack itemstack = itemStacks.get(i);
            if (!itemstack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0.5D, 0.15D + (0.05D * i), 0.5D);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F * i));
                poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
                poseStack.scale(0.5F, 0.5F, 0.5F);
                Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemTransforms.TransformType.FIXED, p_112311_, p_112312_, poseStack, multiBufferSource, (int) (mixingBowl.getBlockPos().asLong() + i));
                poseStack.popPose();
            }
        }
    }
}
