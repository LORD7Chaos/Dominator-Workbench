package com.lord.dominatorworkbench.container.dominator;

import com.lord.dominatorworkbench.recipes.dominator.DominatorRecipes;
import com.lord.dominatorworkbench.tileentity.TileEntityDominatorWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class ContainerDominatorWorkbench extends Container {

    private final TileEntityDominatorWorkbench tile;
    private final EntityPlayer player;

    public ContainerDominatorWorkbench(InventoryPlayer playerInventory, TileEntityDominatorWorkbench tile) {
        this.tile = tile;
        this.player = playerInventory.player;

        this.addSlotToContainer(new SlotCraftingDominator(player, tile, tile, 121, 246, 97) {
            @Override
            public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
                ItemStack result = super.onTake(thePlayer, stack);
                updateCraftingResult();
                return result;
            }
        });

        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                this.addSlotToContainer(new Slot(tile, x + y * 11, 12 + x * 18, 8 + y * 18) {
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
                this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 56 + x * 18, 210 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(playerInventory, x, 56 + x * 18, 268));
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
            }, 11, 11);

            for (int i = 0; i < 121; i++) {
                craftMatrix.setInventorySlotContents(i, tile.getStackInSlot(i));
            }

            ItemStack result = DominatorRecipes.findMatchingRecipe(craftMatrix, tile.getWorld());
            tile.setInventorySlotContents(121, result);
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

            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 122, 158, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack1, itemstack);
                updateCraftingResult(); 
            }
            else if (index >= 122 && index < 149) {
                if (!this.mergeItemStack(itemstack1, 1, 122, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 149 && index < 158) {
                if (!this.mergeItemStack(itemstack1, 1, 122, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 1 && index < 122) {
                if (!this.mergeItemStack(itemstack1, 122, 158, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 122, 158, false)) {
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

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.slotNumber != 0 && super.canMergeSlot(stack, slotIn);
    }
}