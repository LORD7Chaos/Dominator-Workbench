package com.lord.dominatorworkbench.jei;

import com.lord.dominatorworkbench.recipes.DominatorShapedRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DominatorRecipeWrapper implements IRecipeWrapper {

    private final DominatorShapedRecipe recipe;
    private final Object[] recipeIngredients;
    private final ItemStack recipeOutput;

    public DominatorRecipeWrapper(DominatorShapedRecipe recipe) {
        this.recipe = recipe;
        this.recipeIngredients = getRecipeIngredients(recipe);
        this.recipeOutput = recipe.getRecipeOutput();
    }

    private Object[] getRecipeIngredients(DominatorShapedRecipe recipe) {
        try {
            java.lang.reflect.Field ingredientsField = DominatorShapedRecipe.class.getDeclaredField("ingredients");
            ingredientsField.setAccessible(true);
            return (Object[]) ingredientsField.get(recipe);
        } catch (Exception e) {
            return new Object[121];
        }
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<List<ItemStack>> inputs = new ArrayList<>();

        for (int i = 0; i < 121; i++) {
            if (i < recipeIngredients.length && recipeIngredients[i] != null) {
                List<ItemStack> inputList = new ArrayList<>();
                Object ingredient = recipeIngredients[i];

                if (ingredient instanceof ItemStack) {
                    ItemStack stack = (ItemStack) ingredient;
                    if (!stack.isEmpty()) {
                        inputList.add(stack.copy());
                    }
                } else if (ingredient instanceof String) {
                    String oreName = (String) ingredient;
                    List<ItemStack> ores = OreDictionary.getOres(oreName);
                    if (!ores.isEmpty()) {
                        inputList.addAll(ores);
                    }
                }

                inputs.add(inputList);
            } else {
                inputs.add(Collections.emptyList());
            }
        }

        ingredients.setInputLists(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, recipeOutput);
    }

    public DominatorShapedRecipe getRecipe() {
        return recipe;
    }

    public Object[] getRecipeIngredients() {
        return recipeIngredients;
    }
}