package com.lord.dominatorworkbench.jei.universe;

import com.lord.dominatorworkbench.DominatorWorkbenchMod;
import com.lord.dominatorworkbench.container.universe.ContainerDominatorWorkbenchUniverse;
import com.lord.dominatorworkbench.gui.GuiDominatorWorkbenchUniverse;
import com.lord.dominatorworkbench.jei.universe.*;
import com.lord.dominatorworkbench.recipes.universe.DominatorRecipesUniverse;
import com.lord.dominatorworkbench.recipes.universe.DominatorShapedRecipeUniverse;
import com.lord.dominatorworkbench.recipes.universe.DominatorShapelessRecipeUniverse;
import com.lord.dominatorworkbench.recipes.universe.IDominatorRecipeUniverse;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class DominatorWorkbenchPluginUniverse implements IModPlugin {

    public static final String DOMINATOR_WORKBENCH_UNIVERSE_UID = "dominatorworkbench.dominator_workbench_universe";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers helpers = registry.getJeiHelpers();
        final IGuiHelper guiHelper = helpers.getGuiHelper();

        registry.addRecipeCategories(new DominatorWorkbenchUniverseCategory(guiHelper));
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        if (DominatorWorkbenchMod.unifiedWorkbench == null) {
            return;
        }

        registry.handleRecipes(DominatorShapedRecipeUniverse.class, DominatorRecipeWrapperUniverse::new, DOMINATOR_WORKBENCH_UNIVERSE_UID);
        registry.handleRecipes(DominatorShapelessRecipeUniverse.class, DominatorShapelessRecipeWrapperUniverse::new, DOMINATOR_WORKBENCH_UNIVERSE_UID);

        List<IDominatorRecipeUniverse> allRecipes = DominatorRecipesUniverse.getRecipes();
        List<Object> recipes = new ArrayList<>();

        for (IDominatorRecipeUniverse recipe : allRecipes) {
            if (recipe instanceof DominatorShapedRecipeUniverse) {
                recipes.add(recipe);
            } else if (recipe instanceof DominatorShapelessRecipeUniverse) {
                recipes.add(recipe);
            }
        }

        if (!recipes.isEmpty()) {
            registry.addRecipes(recipes, DOMINATOR_WORKBENCH_UNIVERSE_UID);
        }

        ItemStack catalyst = new ItemStack(DominatorWorkbenchMod.unifiedWorkbench, 1, 6);
        if (!catalyst.isEmpty()) {
            registry.addRecipeCatalyst(catalyst, DOMINATOR_WORKBENCH_UNIVERSE_UID);
        }

        registry.addRecipeClickArea(GuiDominatorWorkbenchUniverse.class,
                286, 140,
                28, 18,
                DOMINATOR_WORKBENCH_UNIVERSE_UID);

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
        recipeTransferRegistry.addRecipeTransferHandler(
                ContainerDominatorWorkbenchUniverse.class,
                DOMINATOR_WORKBENCH_UNIVERSE_UID,
                1, 225,
                226, 36
        );
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
    }
}