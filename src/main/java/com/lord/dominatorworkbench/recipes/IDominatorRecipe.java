package com.lord.dominatorworkbench.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IDominatorRecipe {

    boolean matches(InventoryCrafting crafting, World world);

    ItemStack getCraftingResult(InventoryCrafting crafting);

    default ItemStack[] getRemainingItems(InventoryCrafting crafting) {
        ItemStack[] remaining = new ItemStack[crafting.getSizeInventory()];
        for (int i = 0; i < remaining.length; i++) {
            ItemStack itemstack = crafting.getStackInSlot(i);
            remaining[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }
        return remaining;
    }

    default int getRecipeWidth() {
        return 11;
    }

    default int getRecipeHeight() {
        return 11;
    }
}