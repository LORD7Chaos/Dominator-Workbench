package com.lord.dominatorworkbench.jei.dominator;


import com.lord.dominatorworkbench.DominatorWorkbenchMod;
import com.lord.dominatorworkbench.container.dominator.ContainerDominatorWorkbench;
import com.lord.dominatorworkbench.gui.GuiDominatorWorkbench;
import com.lord.dominatorworkbench.jei.dominator.*;
import com.lord.dominatorworkbench.recipes.dominator.DominatorRecipes;
import com.lord.dominatorworkbench.recipes.dominator.DominatorShapedRecipe;
import com.lord.dominatorworkbench.recipes.dominator.DominatorShapelessRecipe;
import com.lord.dominatorworkbench.recipes.dominator.IDominatorRecipe;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class DominatorWorkbenchPlugin implements IModPlugin {

    public static final String DOMINATOR_WORKBENCH_UID = "dominatorworkbench.dominator_workbench";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers helpers = registry.getJeiHelpers();
        final IGuiHelper guiHelper = helpers.getGuiHelper();

        registry.addRecipeCategories(new DominatorWorkbenchCategory(guiHelper));
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        if (DominatorWorkbenchMod.unifiedWorkbench == null) {
            return;
        }

        registry.handleRecipes(DominatorShapedRecipe.class, DominatorRecipeWrapper::new, DOMINATOR_WORKBENCH_UID);
        registry.handleRecipes(DominatorShapelessRecipe.class, DominatorShapelessRecipeWrapper::new, DOMINATOR_WORKBENCH_UID);

        List<IDominatorRecipe> allRecipes = DominatorRecipes.getRecipes();
        List<Object> recipes = new ArrayList<>();

        for (IDominatorRecipe recipe : allRecipes) {
            if (recipe instanceof DominatorShapedRecipe) {
                recipes.add(recipe);
            } else if (recipe instanceof DominatorShapelessRecipe) {
                recipes.add(recipe);
            }
        }

        if (!recipes.isEmpty()) {
            registry.addRecipes(recipes, DOMINATOR_WORKBENCH_UID);
        }

        ItemStack catalyst = new ItemStack(DominatorWorkbenchMod.unifiedWorkbench, 1, 4);
        if (!catalyst.isEmpty()) {
            registry.addRecipeCatalyst(catalyst, DOMINATOR_WORKBENCH_UID);
        }

        registry.addRecipeClickArea(GuiDominatorWorkbench.class,
                205, 95,
                28, 18,
                DOMINATOR_WORKBENCH_UID);

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
        recipeTransferRegistry.addRecipeTransferHandler(
                ContainerDominatorWorkbench.class,
                DOMINATOR_WORKBENCH_UID,
                1, 121,
                122, 36
        );
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
    }
}