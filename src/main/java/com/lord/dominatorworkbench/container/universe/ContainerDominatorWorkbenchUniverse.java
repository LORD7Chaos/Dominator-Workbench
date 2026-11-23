package com.lord.dominatorworkbench.container.universe;

import com.lord.dominatorworkbench.recipes.universe.DominatorRecipesUniverse;
import com.lord.dominatorworkbench.tileentity.TileEntityDominatorWorkbenchUniverse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class ContainerDominatorWorkbenchUniverse extends Container {

    private final TileEntityDominatorWorkbenchUniverse tile;
    private final EntityPlayer player;

    public ContainerDominatorWorkbenchUniverse(InventoryPlayer playerInventory, TileEntityDominatorWorkbenchUniverse tile) {
        this.tile = tile;
        this.player = playerInventory.player;

        this.addSlotToContainer(new SlotCraftingDominatorUniverse(player, tile, tile, 225, 318, 134));

        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 15; x++) {
                this.addSlotToContainer(new Slot(tile, x + y * 15, 12 + x * 18, 8 + y * 18) {
                    @Override
                    public void onSlotChanged() {
                        super.onSlotChanged();
                        updateCraftingResult();
                    }
                });
            }
        }

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 93 + x * 18, 282 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(playerInventory, x, 93 + x * 18, 340));
        }

        updateCraftingResult();
    }

    private void updateCraftingResult() {
        if (!tile.getWorld().isRemote) {
            InventoryCrafting craftMatrix = new InventoryCrafting(new Container() {
                @Override
                public boolean canInteractWith(EntityPlayer playerIn) {
                    return false;
                }
            }, 15, 15);

            for (int i = 0; i < 225; i++) {
                craftMatrix.setInventorySlotContents(i, tile.getStackInSlot(i));
            }

            ItemStack result = DominatorRecipesUniverse.findMatchingRecipe(craftMatrix, tile.getWorld());
            tile.setInventorySlotContents(225, result);
            detectAndSendChanges();
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        updateCraftingResult();
        super.onCraftMatrixChanged(inventoryIn);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tile.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 225) {
                if (!this.mergeItemStack(itemstack1, 226, 262, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack1, itemstack);
                updateCraftingResult();
            }
            else if (index >= 226 && index < 253) {
                if (!this.mergeItemStack(itemstack1, 0, 225, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 253 && index < 262) {
                if (!this.mergeItemStack(itemstack1, 0, 225, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 0 && index < 225) {
                if (!this.mergeItemStack(itemstack1, 226, 262, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 226, 262, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}