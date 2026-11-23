package com.lord.dominatorworkbench.recipes.enhanced;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class DominatorShapedRecipeEnhanced implements IDominatorRecipeEnhanced {
    private final int width = 5, height = 5;
    private final Object[] ingredients;
    private final ItemStack result;

    public DominatorShapedRecipeEnhanced(ItemStack result, Object... recipe) {
        this.ingredients = recipe;
        this.result = result.copy();
    }

    @Override
    public boolean matches(InventoryCrafting crafting, World world) {
        for (int startX = 0; startX <= 5 - width; startX++) {
            for (int startY = 0; startY <= 5 - height; startY++) {
                if (checkMatch(crafting, startX, startY)) return true;
            }
        }
        return false;
    }

    private boolean checkMatch(InventoryCrafting crafting, int startX, int startY) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int gridX = startX + x;
                int gridY = startY + y;
                int slotIndex = gridX + gridY * 5;
                int recipeIndex = x + y * width;

                if (recipeIndex >= ingredients.length) return false;
                Object recipeItem = ingredients[recipeIndex];
                ItemStack gridItem = crafting.getStackInSlot(slotIndex);

                if (!itemMatches(recipeItem, gridItem)) return false;
            }
        }

        for (int gridX = 0; gridX < 5; gridX++) {
            for (int gridY = 0; gridY < 5; gridY++) {
                if (gridX >= startX && gridX < startX + width && gridY >= startY && gridY < startY + height) continue;
                ItemStack gridItem = crafting.getStackInSlot(gridX + gridY * 5);
                if (!gridItem.isEmpty()) return false;
            }
        }
        return true;
    }

    private boolean itemMatches(Object recipeItem, ItemStack gridItem) {
        if (recipeItem == null) return gridItem.isEmpty();
        if (recipeItem instanceof ItemStack) {
            ItemStack recipeStack = (ItemStack) recipeItem;
            if (recipeStack.isEmpty()) return gridItem.isEmpty();
            return !gridItem.isEmpty() && OreDictionary.itemMatches(recipeStack, gridItem, false);
        }
        if (recipeItem instanceof String) {
            if (gridItem.isEmpty()) return false;
            for (ItemStack oreStack : OreDictionary.getOres((String) recipeItem)) {
                if (OreDictionary.itemMatches(oreStack, gridItem, false)) return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting crafting) { return result.copy(); }
    public ItemStack getRecipeOutput() { return result; }
    public Object[] getIngredients() { return ingredients.clone(); }
}