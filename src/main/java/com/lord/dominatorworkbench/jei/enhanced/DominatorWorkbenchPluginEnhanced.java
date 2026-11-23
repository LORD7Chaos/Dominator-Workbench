package com.lord.dominatorworkbench.jei.enhanced;

import com.lord.dominatorworkbench.DominatorWorkbenchMod;
import com.lord.dominatorworkbench.container.enhanced.ContainerDominatorWorkbenchEnhanced;
import com.lord.dominatorworkbench.gui.GuiDominatorWorkbenchEnhanced;
import com.lord.dominatorworkbench.recipes.enhanced.DominatorRecipesEnhanced;
import com.lord.dominatorworkbench.recipes.enhanced.DominatorShapedRecipeEnhanced;
import com.lord.dominatorworkbench.recipes.enhanced.DominatorShapelessRecipeEnhanced;
import com.lord.dominatorworkbench.recipes.enhanced.IDominatorRecipeEnhanced;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class DominatorWorkbenchPluginEnhanced implements IModPlugin {

    public static final String DOMINATOR_WORKBENCH_ENHANCED_UID = "dominatorworkbench.enhanced";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers helpers = registry.getJeiHelpers();
        final IGuiHelper guiHelper = helpers.getGuiHelper();
        registry.addRecipeCategories(new DominatorWorkbenchEnhancedCategory(guiHelper));
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        if (DominatorWorkbenchMod.unifiedWorkbench == null) {
            return;
        }

        registry.handleRecipes(DominatorShapedRecipeEnhanced.class, DominatorRecipeWrapperEnhanced::new, DOMINATOR_WORKBENCH_ENHANCED_UID);
        registry.handleRecipes(DominatorShapelessRecipeEnhanced.class, DominatorShapelessRecipeWrapperEnhanced::new, DOMINATOR_WORKBENCH_ENHANCED_UID);

        List<IDominatorRecipeEnhanced> allRecipes = DominatorRecipesEnhanced.getRecipes();
        List<Object> recipes = new ArrayList<>();

        for (IDominatorRecipeEnhanced recipe : allRecipes) {
            if (recipe instanceof DominatorShapedRecipeEnhanced) {
                recipes.add(recipe);
            } else if (recipe instanceof DominatorShapelessRecipeEnhanced) {
                recipes.add(recipe);
            }
        }

        if (!recipes.isEmpty()) {
            registry.addRecipes(recipes, DOMINATOR_WORKBENCH_ENHANCED_UID);
        }

        ItemStack catalyst = new ItemStack(DominatorWorkbenchMod.unifiedWorkbench, 1, 1);
        if (!catalyst.isEmpty()) {
            registry.addRecipeCatalyst(catalyst, DOMINATOR_WORKBENCH_ENHANCED_UID);
        }

        registry.addRecipeClickArea(GuiDominatorWorkbenchEnhanced.class, 115, 97, 28, 18, DOMINATOR_WORKBENCH_ENHANCED_UID);

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
        recipeTransferRegistry.addRecipeTransferHandler(
                ContainerDominatorWorkbenchEnhanced.class,
                DOMINATOR_WORKBENCH_ENHANCED_UID,
                0, 25,
                26, 36
        );
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
    }
}