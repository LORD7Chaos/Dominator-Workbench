package com.lord.dominatorworkbench.crafttweaker;

import com.lord.dominatorworkbench.recipes.DominatorRecipes;
import com.lord.dominatorworkbench.recipes.DominatorShapedRecipe;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.recipes.IRecipeFunction;
import net.minecraft.item.ItemStack;

public class ActionAddDominatorRecipe implements IAction {

    private final IItemStack output;
    private final IIngredient[][] ingredients;
    private final IRecipeFunction function;

    public ActionAddDominatorRecipe(IItemStack output, IIngredient[][] ingredients, IRecipeFunction function) {
        this.output = output;
        this.ingredients = ingredients;
        this.function = function;
    }

    @Override
    public void apply() {
        try {
            ItemStack result = (ItemStack) output.getInternal();
            int width = ingredients[0].length;
            int height = ingredients.length;

            // Создаем плоский массив ингредиентов
            Object[] recipeIngredients = new Object[width * height];

            int index = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    IIngredient ingredient = ingredients[y][x];
                    if (ingredient != null && !ingredient.getItems().isEmpty()) {
                        recipeIngredients[index] = ingredient.getInternal();
                    } else {
                        recipeIngredients[index] = null;
                    }
                    index++;
                }
            }

            DominatorShapedRecipe recipe = new DominatorShapedRecipe(result, recipeIngredients);
            DominatorRecipes.addRecipe(recipe);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String describe() {
        return "Adding Dominator Workbench recipe for " + output.getDisplayName();
    }
}