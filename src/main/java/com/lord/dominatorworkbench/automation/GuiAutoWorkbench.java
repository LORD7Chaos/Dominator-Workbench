package com.lord.dominatorworkbench.automation;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

 
public class GuiAutoWorkbench extends GuiContainer {
    private final TileAutoWorkbench tile;
    private final AutoWorkbenchLayout layout;
    private final ResourceLocation texture;
    private GuiButton redstoneButton;

    public GuiAutoWorkbench(InventoryPlayer inventory, TileAutoWorkbench tile) {
        super(new ContainerAutoWorkbench(inventory, tile));
        this.tile = tile;
        this.layout = ((ContainerAutoWorkbench) inventorySlots).layout();
        this.texture = new ResourceLocation("dominatorworkbench", "textures/gui/auto_workbench_" + tile.getTier() + ".png");
        this.xSize = layout.width;
        this.ySize = layout.height;
    }

    public TileAutoWorkbench tile() { return tile; }
    public AutoWorkbenchLayout layout() { return layout; }

    @Override public void initGui() {
        super.initGui();
        buttonList.clear();
        
        buttonList.add(new GuiButton(2, guiLeft + 8, guiTop + layout.inventoryY + 55, 22, 20, "X"));
        redstoneButton = new GuiButton(1, guiLeft + layout.width - 30,
                guiTop + layout.inventoryY + 55, 22, 20, "R");
        buttonList.add(redstoneButton);
    }

    @Override protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1 || button.id == 2) {
            mc.playerController.sendEnchantPacket(inventorySlots.windowId, button.id);
        }
    }

    @Override protected void drawGuiContainerBackgroundLayer(float partial, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(texture);
        drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0,
                layout.width, layout.height, layout.width, layout.height);
        
        int x = guiLeft + layout.energyX;
        int y = guiTop + layout.energyY;
        drawRect(x, y, x + 6, y + 50, 0xFF373737);
        drawRect(x + 1, y + 1, x + 5, y + 49, 0xFF777777);
        int fill = (int) (47L * tile.getEnergyStored() / Math.max(1, tile.getEnergyCapacity()));
        if (fill > 0) drawRect(x + 1, y + 49 - fill, x + 5, y + 49, 0xFF49C6FF);
    }

    @Override protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        redstoneButton.displayString = new String[]{"R", "H", "L"}[Math.max(0, Math.min(2, tile.getRedstoneMode()))];
        
        ItemStack result = tile.getPreview();
        if (!result.isEmpty()) {
            itemRender.renderItemAndEffectIntoGUI(result, layout.previewX, layout.previewY);
            itemRender.renderItemOverlayIntoGUI(fontRenderer, result, layout.previewX, layout.previewY, null);
        }
    }

    @Override public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        int x = mouseX - guiLeft, y = mouseY - guiTop;
        List<String> tooltip = new ArrayList<>();
        if (x >= layout.energyX && x <= layout.energyX + 6
                && y >= layout.energyY && y <= layout.energyY + 50) {
            tooltip.add(tile.getEnergyStored() + " / " + tile.getEnergyCapacity() + " FE");
            tooltip.add(tile.energyPerOperation() + " FE per craft");
        } else if (x >= layout.previewX && x <= layout.previewX + 16
                && y >= layout.previewY && y <= layout.previewY + 16 && !tile.getPreview().isEmpty()) {
            tooltip.add(tile.getPreview().getDisplayName());
            tooltip.add("Expected result");
        } else if (redstoneButton.isMouseOver()) {
            tooltip.add("Redstone: " + new String[]{"Ignored", "Needs signal", "Needs no signal"}[tile.getRedstoneMode()]);
        } else if (x >= 8 && x <= 30 && y >= layout.inventoryY + 55 && y <= layout.inventoryY + 75) {
            tooltip.add("Clear recipe pattern");
        }
        if (!tooltip.isEmpty()) drawHoveringText(tooltip, mouseX, mouseY);
        renderHoveredToolTip(mouseX, mouseY);
    }
}
