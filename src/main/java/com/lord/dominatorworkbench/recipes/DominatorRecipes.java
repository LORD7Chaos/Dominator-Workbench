package com.lord.dominatorworkbench.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class DominatorRecipes {

    private static final List<IDominatorRecipe> RECIPES = new ArrayList<>();

    public static void registerRecipes() {
    }

    public static void addRecipe(IDominatorRecipe recipe) {
        if (recipe != null) {
            RECIPES.add(recipe);
        }
    }

    public static void removeRecipe(IDominatorRecipe recipe) {
        RECIPES.remove(recipe);
    }

    public static void removeAllRecipes() {
        RECIPES.clear();
    }

    public static ItemStack findMatchingRecipe(InventoryCrafting crafting, World world) {
        for (IDominatorRecipe recipe : RECIPES) {
            if (recipe.matches(crafting, world)) {
                return recipe.getCraftingResult(crafting);
            }
        }
        return ItemStack.EMPTY;
    }

    public static List<IDominatorRecipe> getRecipes() {
        return new ArrayList<>(RECIPES);
    }

    public static int getRecipeCount() {
        return RECIPES.size();
    }

    public static void debugRecipes() {

    }
}