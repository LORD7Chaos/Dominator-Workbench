package com.lord.dominatorworkbench.jei.basic;

import com.lord.dominatorworkbench.DominatorWorkbenchMod;
import com.lord.dominatorworkbench.container.basic.ContainerDominatorWorkbenchBasic;
import com.lord.dominatorworkbench.gui.GuiDominatorWorkbenchBasic;
import com.lord.dominatorworkbench.recipes.basic.DominatorRecipesBasic;
import com.lord.dominatorworkbench.recipes.basic.DominatorShapedRecipeBasic;
import com.lord.dominatorworkbench.recipes.basic.DominatorShapelessRecipeBasic;
import com.lord.dominatorworkbench.recipes.basic.IDominatorRecipeBasic;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class DominatorWorkbenchPluginBasic implements IModPlugin {

    public static final String DOMINATOR_WORKBENCH_BASIC_UID = "dominatorworkbench.basic";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers helpers = registry.getJeiHelpers();
        final IGuiHelper guiHelper = helpers.getGuiHelper();
        registry.addRecipeCategories(new DominatorWorkbenchBasicCategory(guiHelper));
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        if (DominatorWorkbenchMod.unifiedWorkbench == null) {
            return;
        }

        registry.handleRecipes(DominatorShapedRecipeBasic.class, DominatorRecipeWrapperBasic::new, DOMINATOR_WORKBENCH_BASIC_UID);
        registry.handleRecipes(DominatorShapelessRecipeBasic.class, DominatorShapelessRecipeWrapperBasic::new, DOMINATOR_WORKBENCH_BASIC_UID);

        List<IDominatorRecipeBasic> allRecipes = DominatorRecipesBasic.getRecipes();
        List<Object> recipes = new ArrayList<>();

        for (IDominatorRecipeBasic recipe : allRecipes) {
            if (recipe instanceof DominatorShapedRecipeBasic) {
                recipes.add(recipe);
            } else if (recipe instanceof DominatorShapelessRecipeBasic) {
                recipes.add(recipe);
            }
        }

        if (!recipes.isEmpty()) {
            registry.addRecipes(recipes, DOMINATOR_WORKBENCH_BASIC_UID);
        }

        ItemStack catalyst = new ItemStack(DominatorWorkbenchMod.unifiedWorkbench, 1, 0);
        if (!catalyst.isEmpty()) {
            registry.addRecipeCatalyst(catalyst, DOMINATOR_WORKBENCH_BASIC_UID);
        }

        registry.addRecipeClickArea(GuiDominatorWorkbenchBasic.class, 89, 35, 28, 18, DOMINATOR_WORKBENCH_BASIC_UID);

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
        recipeTransferRegistry.addRecipeTransferHandler(
                ContainerDominatorWorkbenchBasic.class,
                DOMINATOR_WORKBENCH_BASIC_UID,
                0, 9,
                10, 36
        );
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {

    }
}