package com.lord.dominatorworkbench.crafttweaker;

import com.lord.dominatorworkbench.recipes.*;
import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;

public class ActionRemoveDominatorRecipe implements IAction {

    private final IItemStack output;

    public ActionRemoveDominatorRecipe(IItemStack output) {
        this.output = output;
    }

    @Override
    public void apply() {
        ItemStack target = (ItemStack) output.getInternal();
        DominatorRecipes.getRecipes().removeIf(recipe -> {
            if (recipe instanceof DominatorShapedRecipe) {
                return ItemStack.areItemStacksEqual(((DominatorShapedRecipe) recipe).getRecipeOutput(), target);
            } else if (recipe instanceof DominatorShapelessRecipe) {
                return ItemStack.areItemStacksEqual(((DominatorShapelessRecipe) recipe).getRecipeOutput(), target);
            }
            return false;
        });
    }

    @Override
    public String describe() {
        return "Removing Dominator Workbench recipe for " + output.getDisplayName();
    }
}