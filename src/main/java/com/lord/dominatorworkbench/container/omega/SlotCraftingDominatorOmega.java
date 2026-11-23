package com.lord.dominatorworkbench.container.omega;

import com.lord.dominatorworkbench.recipes.omega.DominatorRecipesOmega;
import com.lord.dominatorworkbench.recipes.omega.IDominatorRecipeOmega;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class SlotCraftingDominatorOmega extends Slot {

    private final EntityPlayer player;
    private final IInventory craftMatrix;
    private int amountCrafted;

    public SlotCraftingDominatorOmega(EntityPlayer player, IInventory craftMatrix, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
        this.craftMatrix = craftMatrix;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(amount, this.getStack().getCount());
        }
        return super.decrStackSize(amount);
    }

    @Override
    protected void onCrafting(ItemStack stack, int amount) {
        this.amountCrafted += amount;
        this.onCrafting(stack);
    }

    @Override
    protected void onCrafting(ItemStack stack) {
        if (this.amountCrafted > 0) {
            stack.onCrafting(this.player.world, this.player, this.amountCrafted);
            net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerCraftingEvent(this.player, stack, this.craftMatrix);
        }
        this.amountCrafted = 0;
    }

    @Override
    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
        this.onCrafting(stack);

        InventoryCrafting tempMatrix = new InventoryCrafting(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer playerIn) {
                return false;
            }
        }, 13, 13);

        for (int i = 0; i < 169; i++) {
            tempMatrix.setInventorySlotContents(i, craftMatrix.getStackInSlot(i));
        }

        IDominatorRecipeOmega recipe = DominatorRecipesOmega.findMatchingDominatorRecipe(tempMatrix, thePlayer.world);

        if (recipe != null) {
            ItemStack[] remaining = recipe.getRemainingItems(tempMatrix);

            for (int i = 0; i < 169; i++) {
                ItemStack slotStack = craftMatrix.getStackInSlot(i);
                ItemStack remainingStack = remaining[i];

                if (!slotStack.isEmpty()) {
                    craftMatrix.decrStackSize(i, 1);
                    slotStack = craftMatrix.getStackInSlot(i);
                }

                if (!remainingStack.isEmpty()) {
                    if (slotStack.isEmpty()) {
                        craftMatrix.setInventorySlotContents(i, remainingStack);
                    } else if (ItemStack.areItemsEqual(slotStack, remainingStack) &&
                            ItemStack.areItemStackTagsEqual(slotStack, remainingStack)) {
                        remainingStack.grow(slotStack.getCount());
                        craftMatrix.setInventorySlotContents(i, remainingStack);
                    } else if (!this.player.inventory.addItemStackToInventory(remainingStack)) {
                        this.player.dropItem(remainingStack, false);
                    }
                }
            }

            craftMatrix.markDirty();
        }

        return stack;
    }
}