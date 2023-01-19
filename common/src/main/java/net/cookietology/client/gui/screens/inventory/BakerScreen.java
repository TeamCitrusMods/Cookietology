package net.cookietology.client.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.cookietology.Cookietology;
import net.cookietology.inventory.BakerMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BakerScreen extends AbstractContainerScreen<BakerMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Cookietology.MODID, "textures/gui/container/baker.png");

    public BakerScreen(BakerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    public void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.titleLabelY = 4;
    }

    @Override
    public void render(PoseStack poseStack, int p_97796_, int p_97797_, float p_97798_) {
        this.renderBackground(poseStack);
        super.render(poseStack, p_97796_, p_97797_, p_97798_);
        this.renderTooltip(poseStack, p_97796_, p_97797_);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int p_97809_, int p_97810_) {
        this.font.draw(poseStack, this.title.copy().withStyle(ChatFormatting.WHITE), (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float p_97854_, int p_97855_, int p_97856_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (this.menu.isLit()) {
            int litProgress = this.menu.getLitProgress();
            this.blit(poseStack, this.leftPos + 56, this.topPos + 36 + 12 - litProgress, 176, 12 - litProgress, 14, litProgress + 1);
        }

        int burnProgress = this.menu.getBurnProgress();
        this.blit(poseStack, this.leftPos + 79, this.topPos + 34, 176, 14, burnProgress + 1, 16);
    }
}
