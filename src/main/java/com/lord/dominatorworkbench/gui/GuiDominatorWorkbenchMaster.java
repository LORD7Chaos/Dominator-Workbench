package com.lord.dominatorworkbench.gui;

import com.lord.dominatorworkbench.container.master.ContainerDominatorWorkbenchMaster;
import com.lord.dominatorworkbench.tileentity.TileEntityDominatorWorkbenchMaster;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiDominatorWorkbenchMaster extends GuiContainer {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("dominatorworkbench", "textures/gui/dominator_workbench_master.png");
    private final InventoryPlayer playerInventory;
    private final TileEntityDominatorWorkbenchMaster tile;

    public GuiDominatorWorkbenchMaster(InventoryPlayer playerInventory, TileEntityDominatorWorkbenchMaster tile) {
        super(new ContainerDominatorWorkbenchMaster(playerInventory, tile));
        this.playerInventory = playerInventory;
        this.tile = tile;
        this.xSize = 240;
        this.ySize = 252;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}