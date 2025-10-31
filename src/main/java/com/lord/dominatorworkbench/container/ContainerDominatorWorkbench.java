package com.lord.dominatorworkbench.container;

import com.lord.dominatorworkbench.recipes.DominatorRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerDominatorWorkbench extends Container {

    private final InventoryCrafting craftMatrix;
    private final IInventory craftResult;
    private final World world;
    private final EntityPlayer player;

    public ContainerDominatorWorkbench(InventoryPlayer playerInventory, World world) {
        this.craftMatrix = new InventoryCrafting(this, 11, 11);
        this.craftResult = new InventoryCraftResult();
        this.world = world;
        this.player = playerInventory.player;

        this.addSlotToContainer(new SlotCraftingDominator(player, craftMatrix, craftResult, 0, 245, 80));

        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                this.addSlotToContainer(new Slot(craftMatrix, x + y * 11, 12 + x * 18, 8 + y * 18));
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

        this.onCraftMatrixChanged(craftMatrix);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        if (!world.isRemote) {
            ItemStack result = DominatorRecipes.findMatchingRecipe(craftMatrix, world);
            craftResult.setInventorySlotContents(0, result);

            this.detectAndSendChanges();
        } else {
            ItemStack result = DominatorRecipes.findMatchingRecipe(craftMatrix, world);
            craftResult.setInventorySlotContents(0, result);
        }
    }

    protected void returnItemsToPlayer() {
        for (int i = 0; i < this.craftMatrix.getSizeInventory(); i++) {
            ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
            if (!itemstack.isEmpty()) {

                if (!this.player.inventory.addItemStackToInventory(itemstack.copy())) {
                    this.player.dropItem(itemstack, false);
                }
                this.craftMatrix.setInventorySlotContents(i, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);

        if (!this.world.isRemote) {
            this.returnItemsToPlayer();
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();


            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 122, 157, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack1, itemstack);
            } else if (index >= 122 && index < 149) {
                if (!this.mergeItemStack(itemstack1, 1, 122, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 149 && index < 157) {
                if (!this.mergeItemStack(itemstack1, 1, 122, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 1 && index < 122) {
                if (!this.mergeItemStack(itemstack1, 122, 157, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 122, 157, false)) {
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
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }
}