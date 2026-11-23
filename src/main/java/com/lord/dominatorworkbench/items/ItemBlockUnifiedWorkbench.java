package com.lord.dominatorworkbench.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockUnifiedWorkbench extends ItemBlock {
    private static final String[] NAMES = {"basic", "enhanced", "master", "titan", "dominator", "omega", "universe"};

    public ItemBlockUnifiedWorkbench(Block block) {
        super(block);
        setRegistryName(block.getRegistryName());
        setHasSubtypes(true);
    }

    @Override public int getMetadata(int damage) { return Math.max(0, Math.min(6, damage)); }

    @Override public String getTranslationKey(ItemStack stack) {
        return "tile.workbench." + NAMES[Math.max(0, Math.min(6, stack.getMetadata()))];
    }
}
