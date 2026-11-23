package com.lord.dominatorworkbench.automation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

 
public class ContainerAutoWorkbench extends Container {
    private final TileAutoWorkbench tile;
    private final int count;
    private final int inventoryStart;
    private final AutoWorkbenchLayout layout;
    private int lastEnergy = -1;
    private int lastRedstone = -1;

    public ContainerAutoWorkbench(InventoryPlayer playerInv, TileAutoWorkbench tile) {
        this.tile = tile;
        this.layout = new AutoWorkbenchLayout(tile.getTier());
        this.count = tile.size();
        int n = layout.side;
        
        
        
        for (int y = 0; y < n; y++) for (int x = 0; x < n; x++) {
            addSlotToContainer(new Slot(tile, count + y * n + x,
                    layout.patternX + x * 18, layout.gridY + y * 18) {
                @Override public boolean isItemValid(ItemStack stack) { return false; }
                @Override public boolean canTakeStack(EntityPlayer player) { return false; }
                @Override public int getSlotStackLimit() { return 1; }
            });
        }
        
        for (int y = 0; y < n; y++) for (int x = 0; x < n; x++) {
            addSlotToContainer(new Slot(tile, y * n + x,
                    layout.inputX + x * 18, layout.gridY + y * 18));
        }
        addSlotToContainer(new Slot(tile, count * 2, layout.outputX, layout.outputY) {
            @Override public boolean isItemValid(ItemStack stack) { return false; }
        });
        inventoryStart = inventorySlots.size();
        for (int y = 0; y < 3; y++) for (int x = 0; x < 9; x++) {
            addSlotToContainer(new Slot(playerInv, 9 + y * 9 + x,
                    layout.inventoryX + x * 18, layout.inventoryY + y * 18));
        }
        for (int x = 0; x < 9; x++) {
            addSlotToContainer(new Slot(playerInv, x, layout.inventoryX + x * 18, layout.hotbarY));
        }
    }

    public TileAutoWorkbench tile() { return tile; }
    public AutoWorkbenchLayout layout() { return layout; }
    @Override public boolean canInteractWith(EntityPlayer player) { return tile.isUsableByPlayer(player); }

    @Override public ItemStack slotClick(int slotId, int button, ClickType type, EntityPlayer player) {
        
        if (slotId >= 0 && slotId < count) {
            if (!tile.isUsableByPlayer(player)) return ItemStack.EMPTY;
            if (type == ClickType.PICKUP) {
                ItemStack cursor = player.inventory.getItemStack();
                ItemStack existing = tile.getStackInSlot(count + slotId);
                if (cursor.isEmpty() || !existing.isEmpty() && ItemStack.areItemsEqual(existing, cursor)
                        && ItemStack.areItemStackTagsEqual(existing, cursor)) {
                    tile.setInventorySlotContents(count + slotId, ItemStack.EMPTY);
                } else {
                    ItemStack ghost = cursor.copy(); ghost.setCount(1);
                    tile.setInventorySlotContents(count + slotId, ghost);
                }
            } else if (type == ClickType.QUICK_MOVE) {
                tile.setInventorySlotContents(count + slotId, ItemStack.EMPTY);
            }
            return ItemStack.EMPTY;
        }
        return super.slotClick(slotId, button, type, player);
    }

    @Override public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        if (index < 0 || index >= inventorySlots.size()) return ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot == null || !slot.getHasStack()) return ItemStack.EMPTY;
        if (index < count) return ItemStack.EMPTY;
        ItemStack original = slot.getStack().copy();
        ItemStack stack = slot.getStack();
        if (index >= inventoryStart) {
            if (!mergeItemStack(stack, count, count * 2, false)) return ItemStack.EMPTY;
        } else {
            if (!mergeItemStack(stack, inventoryStart, inventorySlots.size(), false)) return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) slot.putStack(ItemStack.EMPTY);
        else slot.onSlotChanged();
        if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, stack);
        return original;
    }

    @Override public boolean canMergeSlot(ItemStack stack, Slot target) {
        return target.slotNumber >= count && target.slotNumber < count * 2
                || target.slotNumber >= inventoryStart;
    }
    @Override public boolean enchantItem(EntityPlayer player, int button) {
        if (!tile.isUsableByPlayer(player)) return false;
        if (button == 1) { tile.cycleRedstone(); return true; }
        if (button == 2) { tile.clearPattern(); return true; }
        return false;
    }
    @Override public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (lastEnergy != tile.getEnergyStored() || lastRedstone != tile.getRedstoneMode()) {
            for (IContainerListener listener : listeners) {
                listener.sendWindowProperty(this, 0, tile.getEnergyStored() & 65535);
                listener.sendWindowProperty(this, 1, (tile.getEnergyStored() >>> 16) & 65535);
                listener.sendWindowProperty(this, 2, tile.getRedstoneMode());
            }
            lastEnergy = tile.getEnergyStored(); lastRedstone = tile.getRedstoneMode();
        }
    }
    private int clientEnergyLow, clientEnergyHigh;
    @Override public void updateProgressBar(int id, int data) {
        if (id == 0) clientEnergyLow = data & 65535;
        if (id == 1) clientEnergyHigh = data & 65535;
        if (id == 2) tile.setField(1, data);
        if (id <= 1) tile.setField(0, clientEnergyLow | (clientEnergyHigh << 16));
    }
}
