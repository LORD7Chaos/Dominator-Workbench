package com.lord.dominatorworkbench.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.recipes.IRecipeFunction;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.dominatorworkbench.DominatorWorkbench")
public class DominatorWorkbenchTweaker {

    @ZenMethod
    public static void addShapedRecipe(IItemStack output, IIngredient[][] ingredients) {
        CraftTweakerAPI.apply(new ActionAddDominatorRecipe(output, ingredients, null));
    }

    @ZenMethod
    public static void addShapedRecipe(IItemStack output, IIngredient[][] ingredients, IRecipeFunction function) {
        CraftTweakerAPI.apply(new ActionAddDominatorRecipe(output, ingredients, function));
    }

    @ZenMethod
    public static void addShapelessRecipe(IItemStack output, IIngredient[] ingredients) {
        CraftTweakerAPI.apply(new ActionAddDominatorShapelessRecipe(output, ingredients, null));
    }

    @ZenMethod
    public static void addShapelessRecipe(IItemStack output, IIngredient[] ingredients, IRecipeFunction function) {
        CraftTweakerAPI.apply(new ActionAddDominatorShapelessRecipe(output, ingredients, function));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        CraftTweakerAPI.apply(new ActionRemoveDominatorRecipe(output));
    }

    @ZenMethod
    public static void removeAllRecipes() {
        CraftTweakerAPI.apply(new ActionRemoveAllDominatorRecipes());
    }
}