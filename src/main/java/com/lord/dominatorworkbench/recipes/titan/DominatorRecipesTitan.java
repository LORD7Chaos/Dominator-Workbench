package com.lord.dominatorworkbench.recipes.titan;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import java.util.ArrayList;
import java.util.List;

public class DominatorRecipesTitan {
    private static final List<IDominatorRecipeTitan> RECIPES = new ArrayList<>();

    public static void registerRecipes() {}
    public static void addRecipe(IDominatorRecipeTitan recipe) { if (recipe != null) RECIPES.add(recipe); }
    public static void removeRecipe(IDominatorRecipeTitan recipe) { RECIPES.remove(recipe); }
    public static void removeAllRecipes() { RECIPES.clear(); }

    public static ItemStack findMatchingRecipe(InventoryCrafting crafting, World world) {
        IDominatorRecipeTitan recipe = findMatchingDominatorRecipe(crafting, world);
        return recipe != null ? recipe.getCraftingResult(crafting) : ItemStack.EMPTY;
    }

    public static IDominatorRecipeTitan findMatchingDominatorRecipe(InventoryCrafting crafting, World world) {
        for (IDominatorRecipeTitan recipe : RECIPES) {
            if (recipe.matches(crafting, world)) return recipe;
        }
        return null;
    }

    public static List<IDominatorRecipeTitan> getRecipes() { return new ArrayList<>(RECIPES); }
    public static int getRecipeCount() { return RECIPES.size(); }
}