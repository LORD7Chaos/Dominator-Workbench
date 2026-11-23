package com.lord.dominatorworkbench.automation;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockAutoWorkbench extends ItemBlock {
    private static final String[] NAMES = {"basic", "enhanced", "master", "titan", "dominator", "omega", "universe"};
    public ItemBlockAutoWorkbench(BlockAutoWorkbench block) {
        super(block); setRegistryName(block.getRegistryName()); setHasSubtypes(true);
    }
    @Override public int getMetadata(int damage) { return Math.max(0, Math.min(6, damage)); }
    @Override public String getTranslationKey(ItemStack stack) {
        return "tile.auto_workbench." + NAMES[Math.max(0, Math.min(6, stack.getMetadata()))];
    }
}
