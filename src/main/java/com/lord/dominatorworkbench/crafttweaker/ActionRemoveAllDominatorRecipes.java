package com.lord.dominatorworkbench.crafttweaker;

import com.lord.dominatorworkbench.recipes.DominatorRecipes;
import crafttweaker.IAction;

public class ActionRemoveAllDominatorRecipes implements IAction {

    @Override
    public void apply() {
        DominatorRecipes.removeAllRecipes();
    }

    @Override
    public String describe() {
        return "Removing all Dominator Workbench recipes";
    }
}