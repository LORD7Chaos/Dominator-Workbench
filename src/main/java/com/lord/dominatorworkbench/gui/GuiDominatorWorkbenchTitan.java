package com.lord.dominatorworkbench.gui;

import com.lord.dominatorworkbench.container.titan.ContainerDominatorWorkbenchTitan;
import com.lord.dominatorworkbench.tileentity.TileEntityDominatorWorkbenchTitan;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiDominatorWorkbenchTitan extends GuiContainer {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("dominatorworkbench", "textures/gui/dominator_workbench_titan.png");
    private final InventoryPlayer playerInventory;
    private final TileEntityDominatorWorkbenchTitan tile;

    public GuiDominatorWorkbenchTitan(InventoryPlayer playerInventory, TileEntityDominatorWorkbenchTitan tile) {
        super(new ContainerDominatorWorkbenchTitan(playerInventory, tile));
        this.playerInventory = playerInventory;
        this.tile = tile;
        this.xSize = 256;
        this.ySize = 256;
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