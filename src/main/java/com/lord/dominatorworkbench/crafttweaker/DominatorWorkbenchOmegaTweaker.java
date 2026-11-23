package com.lord.dominatorworkbench.crafttweaker;

import com.lord.dominatorworkbench.recipes.omega.DominatorRecipesOmega;
import com.lord.dominatorworkbench.recipes.omega.DominatorShapedRecipeOmega;
import com.lord.dominatorworkbench.recipes.omega.DominatorShapelessRecipeOmega;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.recipes.IRecipeFunction;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenClass("mods.dominatorworkbench.DominatorWorkbenchOmega")
public class DominatorWorkbenchOmegaTweaker {

    @ZenMethod
    public static void addShapedRecipe(IItemStack output, IIngredient[][] ingredients) {
        CraftTweakerAPI.apply(new ActionAddDominatorRecipeOmega(output, ingredients, null));
    }

    @ZenMethod
    public static void addShapedRecipe(IItemStack output, IIngredient[][] ingredients, IRecipeFunction function) {
        CraftTweakerAPI.apply(new ActionAddDominatorRecipeOmega(output, ingredients, function));
    }

    @ZenMethod
    public static void addShapelessRecipe(IItemStack output, IIngredient[] ingredients) {
        CraftTweakerAPI.apply(new ActionAddDominatorShapelessRecipeOmega(output, ingredients, null));
    }

    @ZenMethod
    public static void addShapelessRecipe(IItemStack output, IIngredient[] ingredients, IRecipeFunction function) {
        CraftTweakerAPI.apply(new ActionAddDominatorShapelessRecipeOmega(output, ingredients, function));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        CraftTweakerAPI.apply(new ActionRemoveDominatorRecipeOmega(output));
    }

    @ZenMethod
    public static void removeAllRecipes() {
        CraftTweakerAPI.apply(new ActionRemoveAllDominatorRecipesOmega());
    }

    
    static class ActionAddDominatorRecipeOmega implements IAction {
        private final IItemStack output;
        private final IIngredient[][] ingredients;
        private final IRecipeFunction function;

        public ActionAddDominatorRecipeOmega(IItemStack output, IIngredient[][] ingredients, IRecipeFunction function) {
            this.output = output;
            this.ingredients = ingredients;
            this.function = function;
        }

        @Override
        public void apply() {
            ItemStack result = (ItemStack) output.getInternal();
            int width = ingredients[0].length;
            int height = ingredients.length;

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

            DominatorShapedRecipeOmega recipe = new DominatorShapedRecipeOmega(result, recipeIngredients);
            DominatorRecipesOmega.addRecipe(recipe);
        }

        @Override
        public String describe() {
            return "Adding Omega Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionAddDominatorShapelessRecipeOmega implements IAction {
        private final IItemStack output;
        private final IIngredient[] ingredients;
        private final IRecipeFunction function;

        public ActionAddDominatorShapelessRecipeOmega(IItemStack output, IIngredient[] ingredients, IRecipeFunction function) {
            this.output = output;
            this.ingredients = ingredients;
            this.function = function;
        }

        @Override
        public void apply() {
            ItemStack result = (ItemStack) output.getInternal();
            List<Object> recipeIngredients = new ArrayList<>();
            for (IIngredient ingredient : ingredients) {
                if (ingredient != null && !ingredient.getItems().isEmpty()) {
                    recipeIngredients.add(ingredient.getInternal());
                }
            }
            DominatorShapelessRecipeOmega recipe = new DominatorShapelessRecipeOmega(result, recipeIngredients.toArray());
            DominatorRecipesOmega.addRecipe(recipe);
        }

        @Override
        public String describe() {
            return "Adding shapeless Omega Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionRemoveDominatorRecipeOmega implements IAction {
        private final IItemStack output;

        public ActionRemoveDominatorRecipeOmega(IItemStack output) {
            this.output = output;
        }

        @Override
        public void apply() {
            ItemStack target = (ItemStack) output.getInternal();
            DominatorRecipesOmega.getRecipes().removeIf(recipe -> {
                if (recipe instanceof DominatorShapedRecipeOmega) {
                    return ItemStack.areItemStacksEqual(((DominatorShapedRecipeOmega) recipe).getRecipeOutput(), target);
                } else if (recipe instanceof DominatorShapelessRecipeOmega) {
                    return ItemStack.areItemStacksEqual(((DominatorShapelessRecipeOmega) recipe).getRecipeOutput(), target);
                }
                return false;
            });
        }

        @Override
        public String describe() {
            return "Removing Omega Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionRemoveAllDominatorRecipesOmega implements IAction {
        @Override
        public void apply() {
            DominatorRecipesOmega.removeAllRecipes();
        }

        @Override
        public String describe() {
            return "Removing all Omega Workbench recipes";
        }
    }
}