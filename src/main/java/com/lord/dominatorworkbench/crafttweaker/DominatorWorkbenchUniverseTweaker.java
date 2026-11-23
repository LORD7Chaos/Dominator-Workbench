package com.lord.dominatorworkbench.crafttweaker;

import com.lord.dominatorworkbench.recipes.universe.DominatorRecipesUniverse;
import com.lord.dominatorworkbench.recipes.universe.DominatorShapedRecipeUniverse;
import com.lord.dominatorworkbench.recipes.universe.DominatorShapelessRecipeUniverse;
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
@ZenClass("mods.dominatorworkbench.DominatorWorkbenchUniverse")
public class DominatorWorkbenchUniverseTweaker {

    @ZenMethod
    public static void addShapedRecipe(IItemStack output, IIngredient[][] ingredients) {
        CraftTweakerAPI.apply(new ActionAddDominatorRecipeUniverse(output, ingredients, null));
    }

    @ZenMethod
    public static void addShapedRecipe(IItemStack output, IIngredient[][] ingredients, IRecipeFunction function) {
        CraftTweakerAPI.apply(new ActionAddDominatorRecipeUniverse(output, ingredients, function));
    }

    @ZenMethod
    public static void addShapelessRecipe(IItemStack output, IIngredient[] ingredients) {
        CraftTweakerAPI.apply(new ActionAddDominatorShapelessRecipeUniverse(output, ingredients, null));
    }

    @ZenMethod
    public static void addShapelessRecipe(IItemStack output, IIngredient[] ingredients, IRecipeFunction function) {
        CraftTweakerAPI.apply(new ActionAddDominatorShapelessRecipeUniverse(output, ingredients, function));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        CraftTweakerAPI.apply(new ActionRemoveDominatorRecipeUniverse(output));
    }

    @ZenMethod
    public static void removeAllRecipes() {
        CraftTweakerAPI.apply(new ActionRemoveAllDominatorRecipesUniverse());
    }

    static class ActionAddDominatorRecipeUniverse implements IAction {
        private final IItemStack output;
        private final IIngredient[][] ingredients;
        private final IRecipeFunction function;

        public ActionAddDominatorRecipeUniverse(IItemStack output, IIngredient[][] ingredients, IRecipeFunction function) {
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

            DominatorShapedRecipeUniverse recipe = new DominatorShapedRecipeUniverse(result, recipeIngredients);
            DominatorRecipesUniverse.addRecipe(recipe);
        }

        @Override
        public String describe() {
            return "Adding Universe Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionAddDominatorShapelessRecipeUniverse implements IAction {
        private final IItemStack output;
        private final IIngredient[] ingredients;
        private final IRecipeFunction function;

        public ActionAddDominatorShapelessRecipeUniverse(IItemStack output, IIngredient[] ingredients, IRecipeFunction function) {
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
            DominatorShapelessRecipeUniverse recipe = new DominatorShapelessRecipeUniverse(result, recipeIngredients.toArray());
            DominatorRecipesUniverse.addRecipe(recipe);
        }

        @Override
        public String describe() {
            return "Adding shapeless Universe Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionRemoveDominatorRecipeUniverse implements IAction {
        private final IItemStack output;

        public ActionRemoveDominatorRecipeUniverse(IItemStack output) {
            this.output = output;
        }

        @Override
        public void apply() {
            ItemStack target = (ItemStack) output.getInternal();
            DominatorRecipesUniverse.getRecipes().removeIf(recipe -> {
                if (recipe instanceof DominatorShapedRecipeUniverse) {
                    return ItemStack.areItemStacksEqual(((DominatorShapedRecipeUniverse) recipe).getRecipeOutput(), target);
                } else if (recipe instanceof DominatorShapelessRecipeUniverse) {
                    return ItemStack.areItemStacksEqual(((DominatorShapelessRecipeUniverse) recipe).getRecipeOutput(), target);
                }
                return false;
            });
        }

        @Override
        public String describe() {
            return "Removing Universe Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionRemoveAllDominatorRecipesUniverse implements IAction {
        @Override
        public void apply() {
            DominatorRecipesUniverse.removeAllRecipes();
        }

        @Override
        public String describe() {
            return "Removing all Universe Workbench recipes";
        }
    }
}