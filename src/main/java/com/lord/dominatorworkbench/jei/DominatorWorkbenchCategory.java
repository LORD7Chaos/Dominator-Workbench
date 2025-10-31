package com.lord.dominatorworkbench.jei;

import com.lord.dominatorworkbench.DominatorWorkbenchMod;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class DominatorWorkbenchCategory implements IRecipeCategory<IRecipeWrapper> {

    private static final int WIDTH = 203;
    private static final int HEIGHT = 230;

    private final IDrawable background;
    private final IDrawable icon;
    private final String title;

    public DominatorWorkbenchCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation(DominatorWorkbenchMod.MODID, "textures/gui/jei_dominator_workbench.png");
        this.background = guiHelper.createDrawable(location, 0, 0, WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(DominatorWorkbenchMod.dominatorWorkbench));
        this.title = I18n.format("jei.dominatorworkbench.category");
    }

    @Nonnull
    @Override
    public String getUid() {
        return DominatorWorkbenchPlugin.DOMINATOR_WORKBENCH_UID;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return title;
    }

    @Nonnull
    @Override
    public String getModName() {
        return "Dominator Workbench";
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

        int craftingSlotIndex = 0;
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                int slotX = 2 + x * 18;
                int slotY = 2 + y * 18;
                itemStacks.init(craftingSlotIndex, true, slotX, slotY);
                craftingSlotIndex++;
            }
        }

        int outputSlotX = (WIDTH - 18) / 2;
        int outputSlotY = 5 + 11 * 18 + 5;
        itemStacks.init(121, false, outputSlotX, outputSlotY);

        itemStacks.set(ingredients);
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
    }
}