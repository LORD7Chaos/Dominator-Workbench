
package com.lord.dominatorworkbench.gui;

import com.lord.dominatorworkbench.container.omega.ContainerDominatorWorkbenchOmega;
import com.lord.dominatorworkbench.tileentity.TileEntityDominatorWorkbenchOmega;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiDominatorWorkbenchOmega extends GuiContainer {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("dominatorworkbench", "textures/gui/dominator_workbench_omega.png");
    private final InventoryPlayer playerInventory;
    private final TileEntityDominatorWorkbenchOmega tile;

    public GuiDominatorWorkbenchOmega(InventoryPlayer playerInventory, TileEntityDominatorWorkbenchOmega tile) {
        super(new ContainerDominatorWorkbenchOmega(playerInventory, tile));
        this.playerInventory = playerInventory;
        this.tile = tile;
        this.xSize = 330;
        this.ySize = 330;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(330f/256f, 330f/256f, 1.0f);
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