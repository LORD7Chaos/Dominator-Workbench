package com.lord.dominatorworkbench.recipes.master;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import java.util.ArrayList;
import java.util.List;

public class DominatorRecipesMaster {
    private static final List<IDominatorRecipeMaster> RECIPES = new ArrayList<>();

    public static void registerRecipes() {}
    public static void addRecipe(IDominatorRecipeMaster recipe) { if (recipe != null) RECIPES.add(recipe); }
    public static void removeRecipe(IDominatorRecipeMaster recipe) { RECIPES.remove(recipe); }
    public static void removeAllRecipes() { RECIPES.clear(); }

    public static ItemStack findMatchingRecipe(InventoryCrafting crafting, World world) {
        IDominatorRecipeMaster recipe = findMatchingDominatorRecipe(crafting, world);
        return recipe != null ? recipe.getCraftingResult(crafting) : ItemStack.EMPTY;
    }

    public static IDominatorRecipeMaster findMatchingDominatorRecipe(InventoryCrafting crafting, World world) {
        for (IDominatorRecipeMaster recipe : RECIPES) {
            if (recipe.matches(crafting, world)) return recipe;
        }
        return null;
    }

    public static List<IDominatorRecipeMaster> getRecipes() { return new ArrayList<>(RECIPES); }
    public static int getRecipeCount() { return RECIPES.size(); }
}