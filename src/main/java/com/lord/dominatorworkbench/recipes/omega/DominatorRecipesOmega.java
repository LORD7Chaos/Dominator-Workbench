package com.lord.dominatorworkbench.recipes.omega;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class DominatorRecipesOmega {

    private static final List<IDominatorRecipeOmega> RECIPES = new ArrayList<>();

    public static void registerRecipes() {
    }

    public static void addRecipe(IDominatorRecipeOmega recipe) {
        if (recipe != null) {
            RECIPES.add(recipe);
        }
    }

    public static void removeRecipe(IDominatorRecipeOmega recipe) {
        RECIPES.remove(recipe);
    }

    public static void removeAllRecipes() {
        RECIPES.clear();
    }

    public static ItemStack findMatchingRecipe(InventoryCrafting crafting, World world) {
        IDominatorRecipeOmega recipe = findMatchingDominatorRecipe(crafting, world);
        return recipe != null ? recipe.getCraftingResult(crafting) : ItemStack.EMPTY;
    }

    public static IDominatorRecipeOmega findMatchingDominatorRecipe(InventoryCrafting crafting, World world) {
        for (IDominatorRecipeOmega recipe : RECIPES) {
            if (recipe.matches(crafting, world)) {
                return recipe;
            }
        }
        return null;
    }

    public static List<IDominatorRecipeOmega> getRecipes() {
        return new ArrayList<>(RECIPES);
    }

    public static int getRecipeCount() {
        return RECIPES.size();
    }
}