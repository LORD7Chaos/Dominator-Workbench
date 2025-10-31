package com.lord.dominatorworkbench.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import com.lord.dominatorworkbench.container.ContainerDominatorWorkbench;

public class GuiDominatorWorkbench extends GuiContainer {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("dominatorworkbench", "textures/gui/dominator_workbench.png");

    public GuiDominatorWorkbench(EntityPlayer player) {
        super(new ContainerDominatorWorkbench(player.inventory, player.world));
        this.xSize = 300;
        this.ySize = 300;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(300f/256f, 300f/256f, 1.0f);
        this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
        GlStateManager.popMatrix();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}