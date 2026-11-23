package com.lord.dominatorworkbench.crafttweaker;

import com.lord.dominatorworkbench.recipes.master.DominatorRecipesMaster;
import com.lord.dominatorworkbench.recipes.master.DominatorShapedRecipeMaster;
import com.lord.dominatorworkbench.recipes.master.DominatorShapelessRecipeMaster;
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
@ZenClass("mods.dominatorworkbench.DominatorWorkbenchMaster")
public class DominatorWorkbenchMasterTweaker {

    @ZenMethod
    public static void addShapedRecipe(IItemStack output, IIngredient[][] ingredients) {
        CraftTweakerAPI.apply(new ActionAddDominatorRecipeMaster(output, ingredients, null));
    }

    @ZenMethod
    public static void addShapedRecipe(IItemStack output, IIngredient[][] ingredients, IRecipeFunction function) {
        CraftTweakerAPI.apply(new ActionAddDominatorRecipeMaster(output, ingredients, function));
    }

    @ZenMethod
    public static void addShapelessRecipe(IItemStack output, IIngredient[] ingredients) {
        CraftTweakerAPI.apply(new ActionAddDominatorShapelessRecipeMaster(output, ingredients, null));
    }

    @ZenMethod
    public static void addShapelessRecipe(IItemStack output, IIngredient[] ingredients, IRecipeFunction function) {
        CraftTweakerAPI.apply(new ActionAddDominatorShapelessRecipeMaster(output, ingredients, function));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        CraftTweakerAPI.apply(new ActionRemoveDominatorRecipeMaster(output));
    }

    @ZenMethod
    public static void removeAllRecipes() {
        CraftTweakerAPI.apply(new ActionRemoveAllDominatorRecipesMaster());
    }

    static class ActionAddDominatorRecipeMaster implements IAction {
        private final IItemStack output;
        private final IIngredient[][] ingredients;
        private final IRecipeFunction function;

        public ActionAddDominatorRecipeMaster(IItemStack output, IIngredient[][] ingredients, IRecipeFunction function) {
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

                DominatorShapedRecipeMaster recipe = new DominatorShapedRecipeMaster(result, recipeIngredients);
                DominatorRecipesMaster.addRecipe(recipe);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public String describe() {
            return "Adding Master Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionAddDominatorShapelessRecipeMaster implements IAction {
        private final IItemStack output;
        private final IIngredient[] ingredients;
        private final IRecipeFunction function;

        public ActionAddDominatorShapelessRecipeMaster(IItemStack output, IIngredient[] ingredients, IRecipeFunction function) {
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

                DominatorShapelessRecipeMaster recipe = new DominatorShapelessRecipeMaster(result, recipeIngredients.toArray());
                DominatorRecipesMaster.addRecipe(recipe);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public String describe() {
            return "Adding shapeless Master Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionRemoveDominatorRecipeMaster implements IAction {
        private final IItemStack output;

        public ActionRemoveDominatorRecipeMaster(IItemStack output) {
            this.output = output;
        }

        @Override
        public void apply() {
            ItemStack target = (ItemStack) output.getInternal();
            DominatorRecipesMaster.getRecipes().removeIf(recipe -> {
                if (recipe instanceof DominatorShapedRecipeMaster) {
                    return ItemStack.areItemStacksEqual(((DominatorShapedRecipeMaster) recipe).getRecipeOutput(), target);
                } else if (recipe instanceof DominatorShapelessRecipeMaster) {
                    return ItemStack.areItemStacksEqual(((DominatorShapelessRecipeMaster) recipe).getRecipeOutput(), target);
                }
                return false;
            });
        }

        @Override
        public String describe() {
            return "Removing Master Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionRemoveAllDominatorRecipesMaster implements IAction {
        @Override
        public void apply() {
            DominatorRecipesMaster.removeAllRecipes();
        }

        @Override
        public String describe() {
            return "Removing all Master Workbench recipes";
        }
    }
}