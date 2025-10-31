package com.lord.dominatorworkbench.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class DominatorShapelessRecipe implements IDominatorRecipe {

    private final ItemStack result;
    private final List<Object> ingredients;

    public DominatorShapelessRecipe(ItemStack result, Object... ingredients) {
        this.result = result.copy();
        this.ingredients = new ArrayList<>();

        for (Object ingredient : ingredients) {
            if (ingredient instanceof ItemStack) {
                ItemStack stack = (ItemStack) ingredient;
                if (!stack.isEmpty()) {
                    this.ingredients.add(stack.copy());
                }
            } else if (ingredient instanceof String) {
                this.ingredients.add(ingredient);
            } else if (ingredient != null) {
                throw new IllegalArgumentException("Invalid shapeless recipe ingredient: " + ingredient);
            }
        }
    }

    @Override
    public boolean matches(InventoryCrafting crafting, World world) {
        List<Object> required = new ArrayList<>(ingredients);
        List<ItemStack> gridItems = new ArrayList<>();

        for (int i = 0; i < crafting.getSizeInventory(); i++) {
            ItemStack slotStack = crafting.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                gridItems.add(slotStack);
            }
        }

        if (gridItems.size() != required.size()) {
            return false;
        }

        for (ItemStack gridItem : gridItems) {
            boolean found = false;

            for (int j = 0; j < required.size(); j++) {
                Object req = required.get(j);

                if (req instanceof ItemStack) {
                    if (OreDictionary.itemMatches((ItemStack) req, gridItem, false)) {
                        required.remove(j);
                        found = true;
                        break;
                    }
                } else if (req instanceof String) {
                    if (OreDictionary.getOres((String) req).stream().anyMatch(stack -> OreDictionary.itemMatches(stack, gridItem, false))) {
                        required.remove(j);
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                return false;
            }
        }

        return required.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting crafting) {
        return result.copy();
    }

    public ItemStack getRecipeOutput() {
        return result;
    }

    public int getRecipeSize() {
        return ingredients.size();
    }

    public List<Object> getIngredients() {
        return new ArrayList<>(ingredients);
    }
}