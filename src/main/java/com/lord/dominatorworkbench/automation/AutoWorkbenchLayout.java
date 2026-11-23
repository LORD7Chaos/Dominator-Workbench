package com.lord.dominatorworkbench.automation;

 




public final class AutoWorkbenchLayout {
    public final int side;
    public final int width;
    public final int height;
    public final int inputX;
    public final int patternX;
    public final int gridY = 18;
    public final int inventoryX;
    public final int inventoryY;
    public final int hotbarY;
    public final int previewX;
    public final int previewY;
    public final int outputX;
    public final int outputY;
    public final int energyX;
    public final int energyY;

    public AutoWorkbenchLayout(int tier) {
        this.side = 3 + 2 * Math.max(0, Math.min(6, tier));
        int naturalWidth = 36 * side + 50;
        this.width = Math.max(230, naturalWidth); 
        this.height = 18 * side + 114;
        int offset = (width - naturalWidth) / 2;
        this.inputX = offset + 8;
        this.patternX = offset + 13 + side * 18;
        this.inventoryX = (width - 162) / 2;
        this.inventoryY = side * 18 + 32;
        this.hotbarY = side * 18 + 90;
        this.previewX = offset + side * 36 + 20;
        this.previewY = side * 9 + 8;
        this.outputX = previewX;
        this.outputY = previewY + 25;
        this.energyX = width - 22; 
        this.energyY = inventoryY; 
    }
}
