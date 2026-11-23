package com.lord.dominatorworkbench.jei.automation;

import com.lord.dominatorworkbench.DominatorWorkbenchMod;
import com.lord.dominatorworkbench.automation.ContainerAutoWorkbench;
import com.lord.dominatorworkbench.automation.AutoWorkbenchLayout;
import com.lord.dominatorworkbench.automation.GuiAutoWorkbench;
import com.lord.dominatorworkbench.automation.PatternPacket;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IGhostIngredientHandler;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

 
@JEIPlugin
public class DominatorAutoJei implements IModPlugin {
    private static final String[] UIDS = {
        "dominatorworkbench.basic", "dominatorworkbench.enhanced", "dominatorworkbench.master",
        "dominatorworkbench.titan", "dominatorworkbench.dominator_workbench", "dominatorworkbench.dominator_workbench_omega",
        "dominatorworkbench.dominator_workbench_universe"
    };
    @Override public void register(@Nonnull IModRegistry registry) {
        for (int tier = 0; tier < 7; tier++) {
            registry.addRecipeCatalyst(new ItemStack(DominatorWorkbenchMod.autoWorkbench, 1, tier), UIDS[tier]);
            registry.getRecipeTransferRegistry().addRecipeTransferHandler(new AutoTransferHandler(tier), UIDS[tier]);
        }
        registry.addGhostIngredientHandler(GuiAutoWorkbench.class, new AutoGhostHandler());
    }

    private static final class AutoGhostHandler implements IGhostIngredientHandler<GuiAutoWorkbench> {
        @Nonnull @Override public <I> List<Target<I>> getTargets(@Nonnull GuiAutoWorkbench gui, @Nonnull I ingredient, boolean doStart) {
            List<Target<I>> targets = new ArrayList<>();
            if (!(ingredient instanceof ItemStack)) return targets;
            AutoWorkbenchLayout layout = gui.layout();
            for (int y = 0; y < layout.side; y++) for (int x = 0; x < layout.side; x++) {
                int patternIndex = y * layout.side + x;
                Rectangle area = new Rectangle(gui.getGuiLeft() + layout.patternX + x * 18,
                        gui.getGuiTop() + layout.gridY + y * 18, 18, 18);
                targets.add(new GhostTarget<>(gui, patternIndex, area));
            }
            return targets;
        }
        @Override public void onComplete() { }
    }
    private static final class GhostTarget<I> implements IGhostIngredientHandler.Target<I> {
        private final GuiAutoWorkbench gui;
        private final int index;
        private final Rectangle area;
        GhostTarget(GuiAutoWorkbench gui, int index, Rectangle area) {
            this.gui = gui; this.index = index; this.area = area;
        }
        @Nonnull @Override public Rectangle getArea() { return area; }
        @Override public void accept(@Nonnull I ingredient) {
            if (ingredient instanceof ItemStack) {
                
                
                ItemStack ghost = ((ItemStack) ingredient).copy();
                if (!ghost.isEmpty()) ghost.setCount(1);
                if (index >= 0 && index < gui.tile().size()) {
                    gui.tile().setInventorySlotContents(gui.tile().size() + index, ghost);
                    DominatorWorkbenchMod.autoNetwork.sendToServer(PatternPacket.single(
                            gui.inventorySlots.windowId, index, ghost));
                }
            }
        }
    }

    private static final class AutoTransferHandler implements IRecipeTransferHandler<ContainerAutoWorkbench> {
        private final int tier;
        AutoTransferHandler(int tier) { this.tier = tier; }
        @Nonnull @Override public Class<ContainerAutoWorkbench> getContainerClass() { return ContainerAutoWorkbench.class; }
        @Nullable @Override public IRecipeTransferError transferRecipe(@Nonnull ContainerAutoWorkbench container,
                @Nonnull IRecipeLayout layout, @Nonnull EntityPlayer player, boolean maxTransfer, boolean doTransfer) {
            if (container.tile().getTier() != tier || !doTransfer) return null;
            int n = container.tile().side();
            List<ItemStack> pattern = new ArrayList<>();
            Map<Integer, ? extends IGuiIngredient<ItemStack>> ingredients = layout.getItemStacks().getGuiIngredients();
            
            
            
            for (int i = 0; i < n * n; i++) {
                IGuiIngredient<ItemStack> ingredient = ingredients.get(i);
                ItemStack stack = ingredient != null && ingredient.isInput()
                        ? ingredient.getDisplayedIngredient() : ItemStack.EMPTY;
                ItemStack ghost = stack == null || stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
                if (!ghost.isEmpty()) ghost.setCount(1);
                pattern.add(ghost);
            }
            
            
            
            container.tile().setPatternBatch(pattern);
            DominatorWorkbenchMod.autoNetwork.sendToServer(PatternPacket.full(container.windowId, pattern));
            return null;
        }
    }
}
