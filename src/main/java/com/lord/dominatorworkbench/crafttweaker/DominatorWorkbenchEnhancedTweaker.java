package com.lord.dominatorworkbench.crafttweaker;

import com.lord.dominatorworkbench.recipes.enhanced.DominatorRecipesEnhanced;
import com.lord.dominatorworkbench.recipes.enhanced.DominatorShapedRecipeEnhanced;
import com.lord.dominatorworkbench.recipes.enhanced.DominatorShapelessRecipeEnhanced;
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
@ZenClass("mods.dominatorworkbench.DominatorWorkbenchEnhanced")
public class DominatorWorkbenchEnhancedTweaker {

    @ZenMethod
    public static void addShapedRecipe(IItemStack output, IIngredient[][] ingredients) {
        CraftTweakerAPI.apply(new ActionAddDominatorRecipeEnhanced(output, ingredients, null));
    }

    @ZenMethod
    public static void addShapedRecipe(IItemStack output, IIngredient[][] ingredients, IRecipeFunction function) {
        CraftTweakerAPI.apply(new ActionAddDominatorRecipeEnhanced(output, ingredients, function));
    }

    @ZenMethod
    public static void addShapelessRecipe(IItemStack output, IIngredient[] ingredients) {
        CraftTweakerAPI.apply(new ActionAddDominatorShapelessRecipeEnhanced(output, ingredients, null));
    }

    @ZenMethod
    public static void addShapelessRecipe(IItemStack output, IIngredient[] ingredients, IRecipeFunction function) {
        CraftTweakerAPI.apply(new ActionAddDominatorShapelessRecipeEnhanced(output, ingredients, function));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        CraftTweakerAPI.apply(new ActionRemoveDominatorRecipeEnhanced(output));
    }

    @ZenMethod
    public static void removeAllRecipes() {
        CraftTweakerAPI.apply(new ActionRemoveAllDominatorRecipesEnhanced());
    }

    static class ActionAddDominatorRecipeEnhanced implements IAction {
        private final IItemStack output;
        private final IIngredient[][] ingredients;
        private final IRecipeFunction function;

        public ActionAddDominatorRecipeEnhanced(IItemStack output, IIngredient[][] ingredients, IRecipeFunction function) {
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

                DominatorShapedRecipeEnhanced recipe = new DominatorShapedRecipeEnhanced(result, recipeIngredients);
                DominatorRecipesEnhanced.addRecipe(recipe);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public String describe() {
            return "Adding Enhanced Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionAddDominatorShapelessRecipeEnhanced implements IAction {
        private final IItemStack output;
        private final IIngredient[] ingredients;
        private final IRecipeFunction function;

        public ActionAddDominatorShapelessRecipeEnhanced(IItemStack output, IIngredient[] ingredients, IRecipeFunction function) {
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

                DominatorShapelessRecipeEnhanced recipe = new DominatorShapelessRecipeEnhanced(result, recipeIngredients.toArray());
                DominatorRecipesEnhanced.addRecipe(recipe);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public String describe() {
            return "Adding shapeless Enhanced Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionRemoveDominatorRecipeEnhanced implements IAction {
        private final IItemStack output;

        public ActionRemoveDominatorRecipeEnhanced(IItemStack output) {
            this.output = output;
        }

        @Override
        public void apply() {
            ItemStack target = (ItemStack) output.getInternal();
            DominatorRecipesEnhanced.getRecipes().removeIf(recipe -> {
                if (recipe instanceof DominatorShapedRecipeEnhanced) {
                    return ItemStack.areItemStacksEqual(((DominatorShapedRecipeEnhanced) recipe).getRecipeOutput(), target);
                } else if (recipe instanceof DominatorShapelessRecipeEnhanced) {
                    return ItemStack.areItemStacksEqual(((DominatorShapelessRecipeEnhanced) recipe).getRecipeOutput(), target);
                }
                return false;
            });
        }

        @Override
        public String describe() {
            return "Removing Enhanced Workbench recipe for " + output.getDisplayName();
        }
    }

    static class ActionRemoveAllDominatorRecipesEnhanced implements IAction {
        @Override
        public void apply() {
            DominatorRecipesEnhanced.removeAllRecipes();
        }

        @Override
        public String describe() {
            return "Removing all Enhanced Workbench recipes";
        }
    }
}