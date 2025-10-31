package com.lord.dominatorworkbench.jei;

import com.lord.dominatorworkbench.container.ContainerDominatorWorkbench;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DominatorWorkbenchTransferHandler implements IRecipeTransferHandler<ContainerDominatorWorkbench> {

    private final mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper handlerHelper;

    public DominatorWorkbenchTransferHandler(mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper handlerHelper) {
        this.handlerHelper = handlerHelper;
    }

    @Nonnull
    @Override
    public Class<ContainerDominatorWorkbench> getContainerClass() {
        return ContainerDominatorWorkbench.class;
    }

    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(@Nonnull ContainerDominatorWorkbench container, @Nonnull IRecipeLayout recipeLayout, @Nonnull EntityPlayer player, boolean maxTransfer, boolean doTransfer) {
        if (doTransfer) {
            player.sendMessage(new net.minecraft.util.text.TextComponentString("Large recipe - please place items manually in the Dominator Workbench"));
        }

        return handlerHelper.createUserErrorWithTooltip("Dominator Workbench recipes must be placed manually due to their large size");
    }
}