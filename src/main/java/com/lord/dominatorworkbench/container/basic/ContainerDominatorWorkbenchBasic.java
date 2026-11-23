package com.lord.dominatorworkbench.container.basic;

import com.lord.dominatorworkbench.recipes.basic.DominatorRecipesBasic;
import com.lord.dominatorworkbench.tileentity.TileEntityDominatorWorkbenchBasic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class ContainerDominatorWorkbenchBasic extends Container {

    private final TileEntityDominatorWorkbenchBasic tile;
    private final EntityPlayer player;

    public ContainerDominatorWorkbenchBasic(InventoryPlayer playerInventory, TileEntityDominatorWorkbenchBasic tile) {
        this.tile = tile;
        this.player = playerInventory.player;

        
        this.addSlotToContainer(new SlotCraftingDominatorBasic(player, tile, tile, 9, 124, 36));

        
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                final int slotX = x;
                final int slotY = y;
                this.addSlotToContainer(new Slot(tile, x + y * 3, 32 + x * 18, 18 + y * 18) {
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
                this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 88 + y * 18));
            }
        }

        
        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 146));
        }

        updateCraftingResult();
    }

    private void updateCraftingResult() {
        if (!tile.getWorld().isRemote) {
            InventoryCrafting craftMatrix = new InventoryCrafting(new Container() {
                @Override
                public boolean canInteractWith(EntityPlayer playerIn) { return false; }
            }, 3, 3);

            for (int i = 0; i < 9; i++) {
                craftMatrix.setInventorySlotContents(i, tile.getStackInSlot(i));
            }

            ItemStack result = DominatorRecipesBasic.findMatchingRecipe(craftMatrix, tile.getWorld());
            tile.setInventorySlotContents(9, result);
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

            if (index == 9) {
                if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                updateCraftingResult();
            } else if (index >= 10 && index < 37) {
                if (!this.mergeItemStack(itemstack1, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 37 && index < 46) {
                if (!this.mergeItemStack(itemstack1, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
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