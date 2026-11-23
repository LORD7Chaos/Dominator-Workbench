package com.lord.dominatorworkbench.gui;

import com.lord.dominatorworkbench.container.enhanced.ContainerDominatorWorkbenchEnhanced;
import com.lord.dominatorworkbench.tileentity.TileEntityDominatorWorkbenchEnhanced;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiDominatorWorkbenchEnhanced extends GuiContainer {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("dominatorworkbench", "textures/gui/dominator_workbench_enhanced.png");
    private final InventoryPlayer playerInventory;
    private final TileEntityDominatorWorkbenchEnhanced tile;

    public GuiDominatorWorkbenchEnhanced(InventoryPlayer playerInventory, TileEntityDominatorWorkbenchEnhanced tile) {
        super(new ContainerDominatorWorkbenchEnhanced(playerInventory, tile));
        this.playerInventory = playerInventory;
        this.tile = tile;
        this.xSize = 210;
        this.ySize = 222;
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