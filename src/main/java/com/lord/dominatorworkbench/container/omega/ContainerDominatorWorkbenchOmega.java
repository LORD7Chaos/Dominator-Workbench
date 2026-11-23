
package com.lord.dominatorworkbench.container.omega;

import com.lord.dominatorworkbench.recipes.omega.DominatorRecipesOmega;
import com.lord.dominatorworkbench.tileentity.TileEntityDominatorWorkbenchOmega;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class ContainerDominatorWorkbenchOmega extends Container {

    private final TileEntityDominatorWorkbenchOmega tile;
    private final EntityPlayer player;

    public ContainerDominatorWorkbenchOmega(InventoryPlayer playerInventory, TileEntityDominatorWorkbenchOmega tile) {
        this.tile = tile;
        this.player = playerInventory.player;

        this.addSlotToContainer(new SlotCraftingDominatorOmega(player, tile, tile, 169, 282, 115));

        for (int y = 0; y < 13; y++) {
            for (int x = 0; x < 13; x++) {
                final int slotX = x;
                final int slotY = y;
                this.addSlotToContainer(new Slot(tile, x + y * 13, 12 + x * 18, 8 + y * 18) {
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
                this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 74 + x * 18, 246 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(playerInventory, x, 74 + x * 18, 304));
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
            }, 13, 13);

            for (int i = 0; i < 169; i++) {
                craftMatrix.setInventorySlotContents(i, tile.getStackInSlot(i));
            }

            ItemStack result = DominatorRecipesOmega.findMatchingRecipe(craftMatrix, tile.getWorld());
            tile.setInventorySlotContents(169, result);
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

            if (index == 169) {
                if (!this.mergeItemStack(itemstack1, 170, 206, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack1, itemstack);
                updateCraftingResult();
            }
            else if (index >= 170 && index < 197) {
                if (!this.mergeItemStack(itemstack1, 0, 169, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 197 && index < 206) {
                if (!this.mergeItemStack(itemstack1, 0, 169, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 0 && index < 169) {
                if (!this.mergeItemStack(itemstack1, 170, 206, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 170, 206, false)) {
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