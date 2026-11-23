package com.lord.dominatorworkbench.recipes.enhanced;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import java.util.ArrayList;
import java.util.List;

public class DominatorRecipesEnhanced {
    private static final List<IDominatorRecipeEnhanced> RECIPES = new ArrayList<>();

    public static void registerRecipes() {}
    public static void addRecipe(IDominatorRecipeEnhanced recipe) { if (recipe != null) RECIPES.add(recipe); }
    public static void removeRecipe(IDominatorRecipeEnhanced recipe) { RECIPES.remove(recipe); }
    public static void removeAllRecipes() { RECIPES.clear(); }

    public static ItemStack findMatchingRecipe(InventoryCrafting crafting, World world) {
        IDominatorRecipeEnhanced recipe = findMatchingDominatorRecipe(crafting, world);
        return recipe != null ? recipe.getCraftingResult(crafting) : ItemStack.EMPTY;
    }

    public static IDominatorRecipeEnhanced findMatchingDominatorRecipe(InventoryCrafting crafting, World world) {
        for (IDominatorRecipeEnhanced recipe : RECIPES) {
            if (recipe.matches(crafting, world)) return recipe;
        }
        return null;
    }

    public static List<IDominatorRecipeEnhanced> getRecipes() { return new ArrayList<>(RECIPES); }
    public static int getRecipeCount() { return RECIPES.size(); }
}