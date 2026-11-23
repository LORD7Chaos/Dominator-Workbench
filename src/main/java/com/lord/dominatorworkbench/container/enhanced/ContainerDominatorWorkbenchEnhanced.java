package com.lord.dominatorworkbench.container.enhanced;

import com.lord.dominatorworkbench.recipes.enhanced.DominatorRecipesEnhanced;
import com.lord.dominatorworkbench.tileentity.TileEntityDominatorWorkbenchEnhanced;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class ContainerDominatorWorkbenchEnhanced extends Container {

    private final TileEntityDominatorWorkbenchEnhanced tile;
    private final EntityPlayer player;

    public ContainerDominatorWorkbenchEnhanced(InventoryPlayer playerInventory, TileEntityDominatorWorkbenchEnhanced tile) {
        this.tile = tile;
        this.player = playerInventory.player;

        
        this.addSlotToContainer(new SlotCraftingDominatorEnhanced(player, tile, tile, 25, 142, 54));

        
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                final int slotX = x;
                final int slotY = y;
                this.addSlotToContainer(new Slot(tile, x + y * 5, 14 + x * 18, 18 + y * 18) {
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
                this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 124 + y * 18));
            }
        }

        
        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 182));
        }

        updateCraftingResult();
    }

    private void updateCraftingResult() {
        if (!tile.getWorld().isRemote) {
            InventoryCrafting craftMatrix = new InventoryCrafting(new Container() {
                @Override
                public boolean canInteractWith(EntityPlayer playerIn) { return false; }
            }, 5, 5);

            for (int i = 0; i < 25; i++) {
                craftMatrix.setInventorySlotContents(i, tile.getStackInSlot(i));
            }

            ItemStack result = DominatorRecipesEnhanced.findMatchingRecipe(craftMatrix, tile.getWorld());
            tile.setInventorySlotContents(25, result);
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

            if (index == 25) {
                if (!this.mergeItemStack(itemstack1, 26, 62, true)) {
                    return ItemStack.EMPTY;
                }
                updateCraftingResult();
            } else if (index >= 26 && index < 53) {
                if (!this.mergeItemStack(itemstack1, 0, 25, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 53 && index < 62) {
                if (!this.mergeItemStack(itemstack1, 0, 25, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 26, 62, false)) {
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