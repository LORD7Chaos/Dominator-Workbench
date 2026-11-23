package com.lord.dominatorworkbench.jei.titan;

import com.lord.dominatorworkbench.DominatorWorkbenchMod;
import com.lord.dominatorworkbench.container.titan.ContainerDominatorWorkbenchTitan;
import com.lord.dominatorworkbench.gui.GuiDominatorWorkbenchTitan;
import com.lord.dominatorworkbench.recipes.titan.DominatorRecipesTitan;
import com.lord.dominatorworkbench.recipes.titan.DominatorShapedRecipeTitan;
import com.lord.dominatorworkbench.recipes.titan.DominatorShapelessRecipeTitan;
import com.lord.dominatorworkbench.recipes.titan.IDominatorRecipeTitan;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class DominatorWorkbenchPluginTitan implements IModPlugin {

    public static final String DOMINATOR_WORKBENCH_TITAN_UID = "dominatorworkbench.titan";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers helpers = registry.getJeiHelpers();
        final IGuiHelper guiHelper = helpers.getGuiHelper();
        registry.addRecipeCategories(new DominatorWorkbenchTitanCategory(guiHelper));
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        if (DominatorWorkbenchMod.unifiedWorkbench == null) {
            return;
        }

        registry.handleRecipes(DominatorShapedRecipeTitan.class, DominatorRecipeWrapperTitan::new, DOMINATOR_WORKBENCH_TITAN_UID);
        registry.handleRecipes(DominatorShapelessRecipeTitan.class, DominatorShapelessRecipeWrapperTitan::new, DOMINATOR_WORKBENCH_TITAN_UID);

        List<IDominatorRecipeTitan> allRecipes = DominatorRecipesTitan.getRecipes();
        List<Object> recipes = new ArrayList<>();

        for (IDominatorRecipeTitan recipe : allRecipes) {
            if (recipe instanceof DominatorShapedRecipeTitan) {
                recipes.add(recipe);
            } else if (recipe instanceof DominatorShapelessRecipeTitan) {
                recipes.add(recipe);
            }
        }

        if (!recipes.isEmpty()) {
            registry.addRecipes(recipes, DOMINATOR_WORKBENCH_TITAN_UID);
        }

        ItemStack catalyst = new ItemStack(DominatorWorkbenchMod.unifiedWorkbench, 1, 3);
        if (!catalyst.isEmpty()) {
            registry.addRecipeCatalyst(catalyst, DOMINATOR_WORKBENCH_TITAN_UID);
        }

        registry.addRecipeClickArea(GuiDominatorWorkbenchTitan.class, 165, 147, 28, 18, DOMINATOR_WORKBENCH_TITAN_UID);

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
        recipeTransferRegistry.addRecipeTransferHandler(
                ContainerDominatorWorkbenchTitan.class,
                DOMINATOR_WORKBENCH_TITAN_UID,
                0, 81,
                82, 36
        );
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
    }
}