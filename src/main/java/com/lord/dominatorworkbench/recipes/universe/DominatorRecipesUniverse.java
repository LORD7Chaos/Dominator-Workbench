package com.lord.dominatorworkbench.recipes.universe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class DominatorRecipesUniverse {

    private static final List<IDominatorRecipeUniverse> RECIPES = new ArrayList<>();

    public static void registerRecipes() {
    }

    public static void addRecipe(IDominatorRecipeUniverse recipe) {
        if (recipe != null) {
            RECIPES.add(recipe);
        }
    }

    public static void removeRecipe(IDominatorRecipeUniverse recipe) {
        RECIPES.remove(recipe);
    }

    public static void removeAllRecipes() {
        RECIPES.clear();
    }

    public static ItemStack findMatchingRecipe(InventoryCrafting crafting, World world) {
        IDominatorRecipeUniverse recipe = findMatchingDominatorRecipe(crafting, world);
        return recipe != null ? recipe.getCraftingResult(crafting) : ItemStack.EMPTY;
    }

    public static IDominatorRecipeUniverse findMatchingDominatorRecipe(InventoryCrafting crafting, World world) {
        for (IDominatorRecipeUniverse recipe : RECIPES) {
            if (recipe.matches(crafting, world)) {
                return recipe;
            }
        }
        return null;
    }

    public static List<IDominatorRecipeUniverse> getRecipes() {
        return new ArrayList<>(RECIPES);
    }

    public static int getRecipeCount() {
        return RECIPES.size();
    }
}