package com.lord.dominatorworkbench.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class DominatorShapedRecipe implements IDominatorRecipe {

    private final int width;
    private final int height;
    private final Object[] ingredients;
    private final ItemStack result;

    public DominatorShapedRecipe(ItemStack result, Object... recipe) {
        this.width = 11;
        this.height = 11;
        this.ingredients = recipe;
        this.result = result.copy();


    }

    @Override
    public boolean matches(InventoryCrafting crafting, World world) {
        for (int startX = 0; startX <= 11 - width; startX++) {
            for (int startY = 0; startY <= 11 - height; startY++) {
                if (checkMatch(crafting, startX, startY)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkMatch(InventoryCrafting crafting, int startX, int startY) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int gridX = startX + x;
                int gridY = startY + y;
                int slotIndex = gridX + gridY * 11;

                int recipeIndex = x + y * width;
                if (recipeIndex >= ingredients.length) {
                    return false;
                }

                Object recipeItem = ingredients[recipeIndex];
                ItemStack gridItem = crafting.getStackInSlot(slotIndex);

                if (!itemMatches(recipeItem, gridItem)) {
                    return false;
                }
            }
        }

        for (int gridX = 0; gridX < 11; gridX++) {
            for (int gridY = 0; gridY < 11; gridY++) {
                if (gridX >= startX && gridX < startX + width &&
                        gridY >= startY && gridY < startY + height) {
                    continue;
                }

                ItemStack gridItem = crafting.getStackInSlot(gridX + gridY * 11);
                if (!gridItem.isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean itemMatches(Object recipeItem, ItemStack gridItem) {
        if (recipeItem == null) {
            return gridItem.isEmpty();
        } else if (recipeItem instanceof ItemStack) {
            ItemStack recipeStack = (ItemStack) recipeItem;
            if (recipeStack.isEmpty()) {
                return gridItem.isEmpty();
            }
            return !gridItem.isEmpty() && OreDictionary.itemMatches(recipeStack, gridItem, false);
        } else if (recipeItem instanceof String) {
            if (gridItem.isEmpty()) {
                return false;
            }
            for (ItemStack oreStack : OreDictionary.getOres((String) recipeItem)) {
                if (OreDictionary.itemMatches(oreStack, gridItem, false)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting crafting) {
        return result.copy();
    }

    @Override
    public int getRecipeWidth() {
        return width;
    }

    @Override
    public int getRecipeHeight() {
        return height;
    }

    public ItemStack getRecipeOutput() {
        return result;
    }
}