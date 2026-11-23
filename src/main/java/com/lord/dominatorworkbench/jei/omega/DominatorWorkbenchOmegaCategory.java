package com.lord.dominatorworkbench.jei.omega;


import com.lord.dominatorworkbench.DominatorWorkbenchMod;
import com.lord.dominatorworkbench.jei.omega.*;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class DominatorWorkbenchOmegaCategory implements IRecipeCategory<IRecipeWrapper> {

    private static final int WIDTH = 240;
    private static final int HEIGHT = 266;

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(DominatorWorkbenchMod.MODID, "textures/gui/jei_dominator_workbench_omega.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final String title;

    public DominatorWorkbenchOmegaCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(DominatorWorkbenchMod.unifiedWorkbench, 1, 5));
        this.title = I18n.format("jei.dominatorworkbench.omega.category");
    }

    @Nonnull
    @Override
    public String getUid() {
        return DominatorWorkbenchPluginOmega.DOMINATOR_WORKBENCH_OMEGA_UID;
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
        for (int y = 0; y < 13; y++) {
            for (int x = 0; x < 13; x++) {
                int slotX = 2 + x * 18;
                int slotY = 2 + y * 18;
                itemStacks.init(craftingSlotIndex, true, slotX, slotY);
                craftingSlotIndex++;
            }
        }

        int outputSlotX = (WIDTH - 18) / 2;
        int outputSlotY = HEIGHT - 22;
        itemStacks.init(169, false, outputSlotX, outputSlotY);

        itemStacks.set(ingredients);
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
        GlStateManager.pushMatrix();

        float scaleX = 300f / 256f;
        float scaleY = 300f / 256f;
        GlStateManager.scale(scaleX, scaleY, 1.0f);

        minecraft.getTextureManager().bindTexture(TEXTURE);
        int drawHeight = (int)(HEIGHT / scaleY);
        minecraft.currentScreen.drawTexturedModalRect(0, 0, 0, 0, WIDTH, drawHeight);

        GlStateManager.popMatrix();
    }
}