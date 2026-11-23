package com.lord.dominatorworkbench.recipes.dominator;

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
        IDominatorRecipe recipe = findMatchingDominatorRecipe(crafting, world);
        return recipe != null ? recipe.getCraftingResult(crafting) : ItemStack.EMPTY;
    }

    public static IDominatorRecipe findMatchingDominatorRecipe(InventoryCrafting crafting, World world) {
        for (IDominatorRecipe recipe : RECIPES) {
            if (recipe.matches(crafting, world)) {
                return recipe;
            }
        }
        return null;
    }

    public static List<IDominatorRecipe> getRecipes() {
        return new ArrayList<>(RECIPES);
    }

    public static int getRecipeCount() {
        return RECIPES.size();
    }

    public static void debugRecipes() {
        System.out.println("=== Dominator Recipes Debug ===");
        System.out.println("Total recipes: " + RECIPES.size());
        for (int i = 0; i < RECIPES.size(); i++) {
            IDominatorRecipe recipe = RECIPES.get(i);
            ItemStack output = recipe.getCraftingResult(null);
            System.out.println("Recipe " + i + ": " + output.getDisplayName() + " x" + output.getCount());
        }
        System.out.println("===============================");
    }
}