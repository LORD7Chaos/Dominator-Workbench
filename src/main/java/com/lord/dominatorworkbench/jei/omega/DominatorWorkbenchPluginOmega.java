package com.lord.dominatorworkbench.jei.omega;


import com.lord.dominatorworkbench.DominatorWorkbenchMod;
import com.lord.dominatorworkbench.container.omega.ContainerDominatorWorkbenchOmega;
import com.lord.dominatorworkbench.gui.GuiDominatorWorkbenchOmega;
import com.lord.dominatorworkbench.recipes.omega.DominatorRecipesOmega;
import com.lord.dominatorworkbench.recipes.omega.DominatorShapedRecipeOmega;
import com.lord.dominatorworkbench.recipes.omega.DominatorShapelessRecipeOmega;
import com.lord.dominatorworkbench.recipes.omega.IDominatorRecipeOmega;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class DominatorWorkbenchPluginOmega implements IModPlugin {

    public static final String DOMINATOR_WORKBENCH_OMEGA_UID = "dominatorworkbench.dominator_workbench_omega";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers helpers = registry.getJeiHelpers();
        final IGuiHelper guiHelper = helpers.getGuiHelper();

        registry.addRecipeCategories(new DominatorWorkbenchOmegaCategory(guiHelper));
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        if (DominatorWorkbenchMod.unifiedWorkbench == null) {
            return;
        }

        registry.handleRecipes(DominatorShapedRecipeOmega.class, DominatorRecipeWrapperOmega::new, DOMINATOR_WORKBENCH_OMEGA_UID);
        registry.handleRecipes(DominatorShapelessRecipeOmega.class, DominatorShapelessRecipeWrapperOmega::new, DOMINATOR_WORKBENCH_OMEGA_UID);

        List<IDominatorRecipeOmega> allRecipes = DominatorRecipesOmega.getRecipes();
        List<Object> recipes = new ArrayList<>();

        for (IDominatorRecipeOmega recipe : allRecipes) {
            if (recipe instanceof DominatorShapedRecipeOmega) {
                recipes.add(recipe);
            } else if (recipe instanceof DominatorShapelessRecipeOmega) {
                recipes.add(recipe);
            }
        }

        if (!recipes.isEmpty()) {
            registry.addRecipes(recipes, DOMINATOR_WORKBENCH_OMEGA_UID);
        }

        ItemStack catalyst = new ItemStack(DominatorWorkbenchMod.unifiedWorkbench, 1, 5);
        if (!catalyst.isEmpty()) {
            registry.addRecipeCatalyst(catalyst, DOMINATOR_WORKBENCH_OMEGA_UID);
        }

        registry.addRecipeClickArea(GuiDominatorWorkbenchOmega.class,
                249, 116,
                28, 18,
                DOMINATOR_WORKBENCH_OMEGA_UID);

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
        recipeTransferRegistry.addRecipeTransferHandler(
                ContainerDominatorWorkbenchOmega.class,
                DOMINATOR_WORKBENCH_OMEGA_UID,
                1, 169,
                170, 36
        );
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
    }
}