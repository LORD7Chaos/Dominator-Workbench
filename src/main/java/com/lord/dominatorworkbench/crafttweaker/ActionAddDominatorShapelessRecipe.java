package com.lord.dominatorworkbench.crafttweaker;

import com.lord.dominatorworkbench.recipes.DominatorRecipes;
import com.lord.dominatorworkbench.recipes.DominatorShapelessRecipe;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.recipes.IRecipeFunction;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ActionAddDominatorShapelessRecipe implements IAction {

    private final IItemStack output;
    private final IIngredient[] ingredients;
    private final IRecipeFunction function;
    private DominatorShapelessRecipe recipe;

    public ActionAddDominatorShapelessRecipe(IItemStack output, IIngredient[] ingredients, IRecipeFunction function) {
        this.output = output;
        this.ingredients = ingredients;
        this.function = function;
    }

    @Override
    public void apply() {
        try {
            ItemStack result = (ItemStack) output.getInternal();

            List<Object> recipeIngredients = new ArrayList<>();
            for (int i = 0; i < ingredients.length; i++) {
                IIngredient ingredient = ingredients[i];
                if (ingredient != null && !ingredient.getItems().isEmpty()) {
                    Object internal = ingredient.getInternal();
                    recipeIngredients.add(internal);
                }
            }

            this.recipe = new DominatorShapelessRecipe(result, recipeIngredients.toArray());
            DominatorRecipes.addRecipe(recipe);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String describe() {
        return "Adding shapeless Dominator Workbench recipe for " + output.getDisplayName();
    }
}