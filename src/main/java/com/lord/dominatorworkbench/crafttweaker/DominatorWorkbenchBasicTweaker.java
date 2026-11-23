package com.lord.dominatorworkbench.crafttweaker;

import com.lord.dominatorworkbench.recipes.basic.DominatorRecipesBasic;
import com.lord.dominatorworkbench.recipes.basic.DominatorShapedRecipeBasic;
import com.lord.dominatorworkbench.recipes.basic.DominatorShapelessRecipeBasic;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.dominatorworkbench.DominatorWorkbenchBasic")
public class DominatorWorkbenchBasicTweaker {

    @ZenMethod
    public static void addShapedRecipe(IItemStack output, IIngredient[][] ingredients) {
        CraftTweakerAPI.apply(new ActionAddDominatorRecipeBasic(output, ingredients));
    }

    @ZenMethod
    public static void addShapelessRecipe(IItemStack output, IIngredient[] ingredients) {
        CraftTweakerAPI.apply(new ActionAddDominatorShapelessRecipeBasic(output, ingredients));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        CraftTweakerAPI.apply(new ActionRemoveDominatorRecipeBasic(output));
    }

    @ZenMethod
    public static void removeAllRecipes() {
        CraftTweakerAPI.apply(new ActionRemoveAllDominatorRecipesBasic());
    }

    static class ActionAddDominatorRecipeBasic implements IAction {
        private final IItemStack output;
        private final IIngredient[][] ingredients;

        public ActionAddDominatorRecipeBasic(IItemStack output, IIngredient[][] ingredients) {
            this.output = output;
            this.ingredients = ingredients;
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

            DominatorShapedRecipeBasic recipe = new DominatorShapedRecipeBasic(result, recipeIngredients);
            DominatorRecipesBasic.addRecipe(recipe);
        }

        @Override
        public String describe() {
            return "Adding Basic Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionAddDominatorShapelessRecipeBasic implements IAction {
        private final IItemStack output;
        private final IIngredient[] ingredients;

        public ActionAddDominatorShapelessRecipeBasic(IItemStack output, IIngredient[] ingredients) {
            this.output = output;
            this.ingredients = ingredients;
        }

        @Override
        public void apply() {
            ItemStack result = (ItemStack) output.getInternal();
            java.util.List<Object> recipeIngredients = new java.util.ArrayList<>();
            for (IIngredient ingredient : ingredients) {
                if (ingredient != null && !ingredient.getItems().isEmpty()) {
                    recipeIngredients.add(ingredient.getInternal());
                }
            }
            DominatorShapelessRecipeBasic recipe = new DominatorShapelessRecipeBasic(result, recipeIngredients.toArray());
            DominatorRecipesBasic.addRecipe(recipe);
        }

        @Override
        public String describe() {
            return "Adding shapeless Basic Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionRemoveDominatorRecipeBasic implements IAction {
        private final IItemStack output;

        public ActionRemoveDominatorRecipeBasic(IItemStack output) {
            this.output = output;
        }

        @Override
        public void apply() {
            ItemStack target = (ItemStack) output.getInternal();
            DominatorRecipesBasic.getRecipes().removeIf(recipe -> {
                if (recipe instanceof DominatorShapedRecipeBasic) {
                    return ItemStack.areItemStacksEqual(((DominatorShapedRecipeBasic) recipe).getRecipeOutput(), target);
                } else if (recipe instanceof DominatorShapelessRecipeBasic) {
                    return ItemStack.areItemStacksEqual(((DominatorShapelessRecipeBasic) recipe).getRecipeOutput(), target);
                }
                return false;
            });
        }

        @Override
        public String describe() {
            return "Removing Basic Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionRemoveAllDominatorRecipesBasic implements IAction {
        @Override
        public void apply() { DominatorRecipesBasic.removeAllRecipes(); }
        @Override
        public String describe() { return "Removing all Basic Workbench recipes"; }
    }
}