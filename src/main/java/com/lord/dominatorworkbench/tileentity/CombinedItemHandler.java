
package com.lord.dominatorworkbench.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class CombinedItemHandler implements IItemHandler {

    private final IItemHandler input;
    private final IItemHandler output;

    public CombinedItemHandler(IItemHandler input, IItemHandler output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public int getSlots() {
        return input.getSlots() + output.getSlots();
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        if (slot < input.getSlots()) {
            return input.getStackInSlot(slot);
        } else {
            return output.getStackInSlot(slot - input.getSlots());
        }
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot < input.getSlots()) {
            
            return input.insertItem(slot, stack, simulate);
        } else {
            
            return stack;
        }
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot < input.getSlots()) {
            
            return ItemStack.EMPTY;
        } else {
            
            return output.extractItem(slot - input.getSlots(), amount, simulate);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        if (slot < input.getSlots()) {
            return input.getSlotLimit(slot);
        } else {
            return output.getSlotLimit(slot - input.getSlots());
        }
    }
}