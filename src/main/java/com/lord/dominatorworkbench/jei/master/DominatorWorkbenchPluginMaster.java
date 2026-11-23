package com.lord.dominatorworkbench.jei.master;

import com.lord.dominatorworkbench.DominatorWorkbenchMod;
import com.lord.dominatorworkbench.container.master.ContainerDominatorWorkbenchMaster;
import com.lord.dominatorworkbench.gui.GuiDominatorWorkbenchMaster;
import com.lord.dominatorworkbench.recipes.master.DominatorRecipesMaster;
import com.lord.dominatorworkbench.recipes.master.DominatorShapedRecipeMaster;
import com.lord.dominatorworkbench.recipes.master.DominatorShapelessRecipeMaster;
import com.lord.dominatorworkbench.recipes.master.IDominatorRecipeMaster;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class DominatorWorkbenchPluginMaster implements IModPlugin {

    public static final String DOMINATOR_WORKBENCH_MASTER_UID = "dominatorworkbench.master";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers helpers = registry.getJeiHelpers();
        final IGuiHelper guiHelper = helpers.getGuiHelper();
        registry.addRecipeCategories(new DominatorWorkbenchMasterCategory(guiHelper));
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        if (DominatorWorkbenchMod.unifiedWorkbench == null) {
            return;
        }

        registry.handleRecipes(DominatorShapedRecipeMaster.class, DominatorRecipeWrapperMaster::new, DOMINATOR_WORKBENCH_MASTER_UID);
        registry.handleRecipes(DominatorShapelessRecipeMaster.class, DominatorShapelessRecipeWrapperMaster::new, DOMINATOR_WORKBENCH_MASTER_UID);

        List<IDominatorRecipeMaster> allRecipes = DominatorRecipesMaster.getRecipes();
        List<Object> recipes = new ArrayList<>();

        for (IDominatorRecipeMaster recipe : allRecipes) {
            if (recipe instanceof DominatorShapedRecipeMaster) {
                recipes.add(recipe);
            } else if (recipe instanceof DominatorShapelessRecipeMaster) {
                recipes.add(recipe);
            }
        }

        if (!recipes.isEmpty()) {
            registry.addRecipes(recipes, DOMINATOR_WORKBENCH_MASTER_UID);
        }

        ItemStack catalyst = new ItemStack(DominatorWorkbenchMod.unifiedWorkbench, 1, 2);
        if (!catalyst.isEmpty()) {
            registry.addRecipeCatalyst(catalyst, DOMINATOR_WORKBENCH_MASTER_UID);
        }

        registry.addRecipeClickArea(GuiDominatorWorkbenchMaster.class, 140, 122, 28, 18, DOMINATOR_WORKBENCH_MASTER_UID);

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
        recipeTransferRegistry.addRecipeTransferHandler(
                ContainerDominatorWorkbenchMaster.class,
                DOMINATOR_WORKBENCH_MASTER_UID,
                0, 49,
                50, 36
        );
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
    }
}