package com.lord.dominatorworkbench.automation;

import com.lord.dominatorworkbench.recipes.basic.DominatorRecipesBasic;
import com.lord.dominatorworkbench.recipes.enhanced.DominatorRecipesEnhanced;
import com.lord.dominatorworkbench.recipes.master.DominatorRecipesMaster;
import com.lord.dominatorworkbench.recipes.titan.DominatorRecipesTitan;
import com.lord.dominatorworkbench.recipes.dominator.DominatorRecipes;
import com.lord.dominatorworkbench.recipes.omega.DominatorRecipesOmega;
import com.lord.dominatorworkbench.recipes.universe.DominatorRecipesUniverse;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

 
public final class AutoRecipeLookup {
    private AutoRecipeLookup() { }

    public static ItemStack find(int tier, InventoryCrafting matrix, World world) {
        if (world == null) return ItemStack.EMPTY;
        switch (tier) {
            case 0: return DominatorRecipesBasic.findMatchingRecipe(matrix, world);
            case 1: return DominatorRecipesEnhanced.findMatchingRecipe(matrix, world);
            case 2: return DominatorRecipesMaster.findMatchingRecipe(matrix, world);
            case 3: return DominatorRecipesTitan.findMatchingRecipe(matrix, world);
            case 4: return DominatorRecipes.findMatchingRecipe(matrix, world);
            case 5: return DominatorRecipesOmega.findMatchingRecipe(matrix, world);
            case 6: return DominatorRecipesUniverse.findMatchingRecipe(matrix, world);
            default: return ItemStack.EMPTY;
        }
    }
}
