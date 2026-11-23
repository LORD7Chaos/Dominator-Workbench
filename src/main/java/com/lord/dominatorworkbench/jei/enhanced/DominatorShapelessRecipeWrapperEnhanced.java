package com.lord.dominatorworkbench.jei.enhanced;

import com.lord.dominatorworkbench.recipes.enhanced.DominatorShapelessRecipeEnhanced;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class DominatorShapelessRecipeWrapperEnhanced implements IRecipeWrapper {

    private final DominatorShapelessRecipeEnhanced recipe;
    private final List<List<ItemStack>> inputs;
    private final ItemStack output;

    public DominatorShapelessRecipeWrapperEnhanced(DominatorShapelessRecipeEnhanced recipe) {
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

    private List<Object> getRecipeIngredients(DominatorShapelessRecipeEnhanced recipe) {
        try {
            java.lang.reflect.Field ingredientsField = DominatorShapelessRecipeEnhanced.class.getDeclaredField("ingredients");
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

    public DominatorShapelessRecipeEnhanced getRecipe() {
        return recipe;
    }
}