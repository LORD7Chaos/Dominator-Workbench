package com.lord.dominatorworkbench.container.titan;

import com.lord.dominatorworkbench.recipes.titan.DominatorRecipesTitan;
import com.lord.dominatorworkbench.tileentity.TileEntityDominatorWorkbenchTitan;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class ContainerDominatorWorkbenchTitan extends Container {

    private final TileEntityDominatorWorkbenchTitan tile;
    private final EntityPlayer player;

    public ContainerDominatorWorkbenchTitan(InventoryPlayer playerInventory, TileEntityDominatorWorkbenchTitan tile) {
        this.tile = tile;
        this.player = playerInventory.player;

        
        this.addSlotToContainer(new SlotCraftingDominatorTitan(player, tile, tile, 81, 214, 79));

        
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                final int slotX = x;
                final int slotY = y;
                this.addSlotToContainer(new Slot(tile, x + y * 9, 14 + x * 18, 8 + y * 18) {
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
                this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 42 + x * 18, 174 + y * 18));
            }
        }

        
        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(playerInventory, x, 42 + x * 18, 232));
        }

        updateCraftingResult();
    }

    private void updateCraftingResult() {
        if (!tile.getWorld().isRemote) {
            InventoryCrafting craftMatrix = new InventoryCrafting(new Container() {
                @Override
                public boolean canInteractWith(EntityPlayer playerIn) { return false; }
            }, 9, 9);

            for (int i = 0; i < 81; i++) {
                craftMatrix.setInventorySlotContents(i, tile.getStackInSlot(i));
            }

            ItemStack result = DominatorRecipesTitan.findMatchingRecipe(craftMatrix, tile.getWorld());
            tile.setInventorySlotContents(81, result);
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

            if (index == 81) {
                if (!this.mergeItemStack(itemstack1, 82, 118, true)) {
                    return ItemStack.EMPTY;
                }
                updateCraftingResult();
            } else if (index >= 82 && index < 109) {
                if (!this.mergeItemStack(itemstack1, 0, 81, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 109 && index < 118) {
                if (!this.mergeItemStack(itemstack1, 0, 81, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 82, 118, false)) {
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