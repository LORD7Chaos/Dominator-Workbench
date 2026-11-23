package com.lord.dominatorworkbench.crafttweaker;

import com.lord.dominatorworkbench.recipes.titan.DominatorRecipesTitan;
import com.lord.dominatorworkbench.recipes.titan.DominatorShapedRecipeTitan;
import com.lord.dominatorworkbench.recipes.titan.DominatorShapelessRecipeTitan;
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
@ZenClass("mods.dominatorworkbench.DominatorWorkbenchTitan")
public class DominatorWorkbenchTitanTweaker {

    @ZenMethod
    public static void addShapedRecipe(IItemStack output, IIngredient[][] ingredients) {
        CraftTweakerAPI.apply(new ActionAddDominatorRecipeTitan(output, ingredients, null));
    }

    @ZenMethod
    public static void addShapedRecipe(IItemStack output, IIngredient[][] ingredients, IRecipeFunction function) {
        CraftTweakerAPI.apply(new ActionAddDominatorRecipeTitan(output, ingredients, function));
    }

    @ZenMethod
    public static void addShapelessRecipe(IItemStack output, IIngredient[] ingredients) {
        CraftTweakerAPI.apply(new ActionAddDominatorShapelessRecipeTitan(output, ingredients, null));
    }

    @ZenMethod
    public static void addShapelessRecipe(IItemStack output, IIngredient[] ingredients, IRecipeFunction function) {
        CraftTweakerAPI.apply(new ActionAddDominatorShapelessRecipeTitan(output, ingredients, function));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        CraftTweakerAPI.apply(new ActionRemoveDominatorRecipeTitan(output));
    }

    @ZenMethod
    public static void removeAllRecipes() {
        CraftTweakerAPI.apply(new ActionRemoveAllDominatorRecipesTitan());
    }

    static class ActionAddDominatorRecipeTitan implements IAction {
        private final IItemStack output;
        private final IIngredient[][] ingredients;
        private final IRecipeFunction function;

        public ActionAddDominatorRecipeTitan(IItemStack output, IIngredient[][] ingredients, IRecipeFunction function) {
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

                DominatorShapedRecipeTitan recipe = new DominatorShapedRecipeTitan(result, recipeIngredients);
                DominatorRecipesTitan.addRecipe(recipe);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public String describe() {
            return "Adding Titan Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionAddDominatorShapelessRecipeTitan implements IAction {
        private final IItemStack output;
        private final IIngredient[] ingredients;
        private final IRecipeFunction function;

        public ActionAddDominatorShapelessRecipeTitan(IItemStack output, IIngredient[] ingredients, IRecipeFunction function) {
            this.output = output;
            this.ingredients = ingredients;
            this.function = function;
        }

        @Override
        public void apply() {
            try {
                ItemStack result = (ItemStack) output.getInternal();
                List<Object> recipeIngredients = new ArrayList<>();
                for (IIngredient ingredient : ingredients) {
                    if (ingredient != null && !ingredient.getItems().isEmpty()) {
                        recipeIngredients.add(ingredient.getInternal());
                    }
                }

                DominatorShapelessRecipeTitan recipe = new DominatorShapelessRecipeTitan(result, recipeIngredients.toArray());
                DominatorRecipesTitan.addRecipe(recipe);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public String describe() {
            return "Adding shapeless Titan Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionRemoveDominatorRecipeTitan implements IAction {
        private final IItemStack output;

        public ActionRemoveDominatorRecipeTitan(IItemStack output) {
            this.output = output;
        }

        @Override
        public void apply() {
            ItemStack target = (ItemStack) output.getInternal();
            DominatorRecipesTitan.getRecipes().removeIf(recipe -> {
                if (recipe instanceof DominatorShapedRecipeTitan) {
                    return ItemStack.areItemStacksEqual(((DominatorShapedRecipeTitan) recipe).getRecipeOutput(), target);
                } else if (recipe instanceof DominatorShapelessRecipeTitan) {
                    return ItemStack.areItemStacksEqual(((DominatorShapelessRecipeTitan) recipe).getRecipeOutput(), target);
                }
                return false;
            });
        }

        @Override
        public String describe() {
            return "Removing Titan Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionRemoveAllDominatorRecipesTitan implements IAction {
        @Override
        public void apply() {
            DominatorRecipesTitan.removeAllRecipes();
        }

        @Override
        public String describe() {
            return "Removing all Titan Workbench recipes";
        }
    }
}