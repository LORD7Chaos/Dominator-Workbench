package com.lord.dominatorworkbench.jei;

import com.lord.dominatorworkbench.recipes.DominatorShapelessRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class DominatorShapelessRecipeWrapper implements IRecipeWrapper {

    private final DominatorShapelessRecipe recipe;
    private final List<List<ItemStack>> inputs;
    private final ItemStack output;

    public DominatorShapelessRecipeWrapper(DominatorShapelessRecipe recipe) {
        this.recipe = recipe;
        this.output = recipe.getRecipeOutput();
        this.inputs = new ArrayList<>();

        List<Object> recipeIngredients = getRecipeIngredients(recipe);

        for (Object ingredient : recipeIngredients) {
            List<ItemStack> inputList = new ArrayList<>();

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

            if (!inputList.isEmpty()) {
                inputs.add(inputList);
            }
        }
    }

    private List<Object> getRecipeIngredients(DominatorShapelessRecipe recipe) {
        try {
            java.lang.reflect.Field ingredientsField = DominatorShapelessRecipe.class.getDeclaredField("ingredients");
            ingredientsField.setAccessible(true);
            return (List<Object>) ingredientsField.get(recipe);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void getIngredients(IIngredients ingredients) {

        ingredients.setInputLists(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, output);
    }

    public DominatorShapelessRecipe getRecipe() {
        return recipe;
    }
}