package com.lord.dominatorworkbench.container.master;

import com.lord.dominatorworkbench.recipes.master.DominatorRecipesMaster;
import com.lord.dominatorworkbench.tileentity.TileEntityDominatorWorkbenchMaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class ContainerDominatorWorkbenchMaster extends Container {

    private final TileEntityDominatorWorkbenchMaster tile;
    private final EntityPlayer player;

    public ContainerDominatorWorkbenchMaster(InventoryPlayer playerInventory, TileEntityDominatorWorkbenchMaster tile) {
        this.tile = tile;
        this.player = playerInventory.player;

        
        this.addSlotToContainer(new SlotCraftingDominatorMaster(player, tile, tile, 49, 178, 61));

        
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                final int slotX = x;
                final int slotY = y;
                this.addSlotToContainer(new Slot(tile, x + y * 7, 14 + x * 18, 8 + y * 18) {
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
                this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 24 + x * 18, 138 + y * 18));
            }
        }

        
        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(playerInventory, x, 24 + x * 18, 196));
        }

        updateCraftingResult();
    }

    private void updateCraftingResult() {
        if (!tile.getWorld().isRemote) {
            InventoryCrafting craftMatrix = new InventoryCrafting(new Container() {
                @Override
                public boolean canInteractWith(EntityPlayer playerIn) { return false; }
            }, 7, 7);

            for (int i = 0; i < 49; i++) {
                craftMatrix.setInventorySlotContents(i, tile.getStackInSlot(i));
            }

            ItemStack result = DominatorRecipesMaster.findMatchingRecipe(craftMatrix, tile.getWorld());
            tile.setInventorySlotContents(49, result);
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

            if (index == 49) {
                if (!this.mergeItemStack(itemstack1, 50, 86, true)) {
                    return ItemStack.EMPTY;
                }
                updateCraftingResult();
            } else if (index >= 50 && index < 77) {
                if (!this.mergeItemStack(itemstack1, 0, 49, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 77 && index < 86) {
                if (!this.mergeItemStack(itemstack1, 0, 49, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 50, 86, false)) {
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