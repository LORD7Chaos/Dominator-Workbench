package com.lord.dominatorworkbench.gui;

import com.lord.dominatorworkbench.container.universe.ContainerDominatorWorkbenchUniverse;
import com.lord.dominatorworkbench.tileentity.TileEntityDominatorWorkbenchUniverse;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiDominatorWorkbenchUniverse extends GuiContainer {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("dominatorworkbench", "textures/gui/dominator_workbench_universe.png");
    private final InventoryPlayer playerInventory;
    private final TileEntityDominatorWorkbenchUniverse tile;

    public GuiDominatorWorkbenchUniverse(InventoryPlayer playerInventory, TileEntityDominatorWorkbenchUniverse tile) {
        super(new ContainerDominatorWorkbenchUniverse(playerInventory, tile));
        this.playerInventory = playerInventory;
        this.tile = tile;
        this.xSize = 360;
        this.ySize = 360;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(365f/256f, 365f/256f, 1.0f);
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