package com.lord.dominatorworkbench.recipes.basic;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import java.util.ArrayList;
import java.util.List;

public class DominatorRecipesBasic {
    private static final List<IDominatorRecipeBasic> RECIPES = new ArrayList<>();

    public static void registerRecipes() {}
    public static void addRecipe(IDominatorRecipeBasic recipe) { if (recipe != null) RECIPES.add(recipe); }
    public static void removeRecipe(IDominatorRecipeBasic recipe) { RECIPES.remove(recipe); }
    public static void removeAllRecipes() { RECIPES.clear(); }

    public static ItemStack findMatchingRecipe(InventoryCrafting crafting, World world) {
        IDominatorRecipeBasic recipe = findMatchingDominatorRecipe(crafting, world);
        return recipe != null ? recipe.getCraftingResult(crafting) : ItemStack.EMPTY;
    }

    public static IDominatorRecipeBasic findMatchingDominatorRecipe(InventoryCrafting crafting, World world) {
        for (IDominatorRecipeBasic recipe : RECIPES) {
            if (recipe.matches(crafting, world)) return recipe;
        }
        return null;
    }

    public static List<IDominatorRecipeBasic> getRecipes() { return new ArrayList<>(RECIPES); }
    public static int getRecipeCount() { return RECIPES.size(); }
}