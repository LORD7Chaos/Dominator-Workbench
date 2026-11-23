package com.lord.dominatorworkbench.automation;

import net.minecraft.entity.player.EntityPlayer;
import com.lord.dominatorworkbench.config.ConfigHandler;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

 




public class TileAutoWorkbench extends TileEntity implements IInventory, ITickable {
    private int tier;
    private NonNullList<ItemStack> items = NonNullList.withSize(19, ItemStack.EMPTY);
    private int redstoneMode; 
    private boolean previewDirty = true;
    private ItemStack preview = ItemStack.EMPTY;
    private int tickCounter;
    private final AutoEnergy energy = new AutoEnergy();
    private final AutoItemHandler itemHandler = new AutoItemHandler();

    public void setTier(int value) {
        int next = Math.max(0, Math.min(6, value));
        if (tier == next && items.size() == (size() * 2 + 1)) return;
        NonNullList<ItemStack> replacement = NonNullList.withSize((3 + 2 * next) * (3 + 2 * next) * 2 + 1, ItemStack.EMPTY);
        for (int i = 0; i < Math.min(replacement.size(), items.size()); ++i) replacement.set(i, items.get(i));
        this.tier = next;
        energy.resize(capacityForSize());
        this.items = replacement;
        previewDirty = true;
        markDirty();
    }

    public int getTier() { return tier; }
    public int side() { return 3 + tier * 2; }
    public int size() { int s = side(); return s * s; }
    public int outputSlot() { return size() * 2; }
    public int getEnergyStored() { return energy.getEnergyStored(); }
    public int getEnergyCapacity() { return energy.getMaxEnergyStored(); }
    public int energyPerOperation() { return size() * ConfigHandler.automation.powerMultiplier; }
    private int capacityForSize() {
        long cap = (long) energyPerOperation() * ConfigHandler.automation.capacityMultiplier;
        return (int) Math.min(Integer.MAX_VALUE, Math.max(cap, energyPerOperation()));
    }
    public int getRedstoneMode() { return redstoneMode; }
    public void cycleRedstone() { redstoneMode = (redstoneMode + 1) % 3; markDirty(); sync(); }
    private void sync() {
        if (world != null && !world.isRemote) {
            net.minecraft.block.state.IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    @Override public void onLoad() {
        super.onLoad();
        if (world != null && world.getBlockState(pos).getBlock() instanceof BlockAutoWorkbench) {
            setTier(world.getBlockState(pos).getValue(BlockAutoWorkbench.TIER));
        }
    }

    public void patternChanged() {
        previewDirty = true;
        refreshPreview();
        markDirty();
    }

    private void refreshPreview() {
        if (!previewDirty || world == null || world.isRemote) return;
        previewDirty = false;
        InventoryCrafting matrix = new InventoryCrafting(new Container() {
            @Override public boolean canInteractWith(EntityPlayer player) { return false; }
        }, side(), side());
        for (int i = 0; i < size(); i++) {
            ItemStack stack = items.get(size() + i);
            matrix.setInventorySlotContents(i, stack.isEmpty() ? ItemStack.EMPTY : stack.copy());
        }
        preview = AutoRecipeLookup.find(tier, matrix, world);
        sync();
    }

    public ItemStack getPreview() { return preview.copy(); }
    public void setPatternBatch(List<ItemStack> recipe) {
        if (recipe.size() != size()) return;
        for (int i = 0; i < size(); i++) {
            ItemStack stack = recipe.get(i);
            ItemStack copy = stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
            if (!copy.isEmpty()) copy.setCount(1);
            items.set(size() + i, copy);
        }
        patternChanged();
    }
    public void clearPattern() {
        for (int i = size(); i < size() * 2; i++) items.set(i, ItemStack.EMPTY);
        patternChanged();
    }

    @Override public void update() {
        if (world == null || world.isRemote) return;
        
        if (previewDirty || (++tickCounter % 20 == 0 && preview.isEmpty())) refreshPreview();
        boolean powered = world.isBlockPowered(pos);
        if (redstoneMode == 1 && !powered || redstoneMode == 2 && powered) return;
        if (preview.isEmpty() || energy.getEnergyStored() < energyPerOperation()) return;
        ItemStack existing = items.get(outputSlot());
        if (!existing.isEmpty() && (!ItemStack.areItemsEqual(existing, preview)
                || !ItemStack.areItemStackTagsEqual(existing, preview)
                || existing.getCount() + preview.getCount() > existing.getMaxStackSize())) return;
        if (existing.isEmpty() && preview.getCount() > preview.getMaxStackSize()) return;

        
        final int gridSize = size();
        int[] consumption = new int[gridSize];
        for (int patternPos = gridSize; patternPos < gridSize * 2; patternPos++) {
            ItemStack required = items.get(patternPos);
            if (required.isEmpty()) continue;
            boolean found = false;
            for (int input = 0; input < gridSize; input++) {
                ItemStack supplied = items.get(input);
                if (!supplied.isEmpty() && supplied.getCount() > consumption[input]
                        && ItemStack.areItemsEqual(required, supplied)
                        && ItemStack.areItemStackTagsEqual(required, supplied)) {
                    consumption[input]++;
                    found = true;
                    break;
                }
            }
            if (!found) return;
        }

        
        List<ItemStack> containerReturns = new ArrayList<>();
        for (int input = 0; input < gridSize; input++) {
            ItemStack supplied = items.get(input);
            if (consumption[input] > 0 && supplied.getItem().hasContainerItem(supplied)) {
                for (int n = 0; n < consumption[input]; ++n) {
                    ItemStack one = supplied.copy(); one.setCount(1);
                    ItemStack container = supplied.getItem().getContainerItem(one);
                    if (!container.isEmpty()) containerReturns.add(container);
                }
            }
        }
        
        List<ItemStack> staged = new ArrayList<>();
        for (int i = 0; i < gridSize; i++) {
            ItemStack stack = items.get(i).copy();
            if (!stack.isEmpty() && consumption[i] > 0) stack.shrink(consumption[i]);
            staged.add(stack);
        }
        for (ItemStack remainder : containerReturns) {
            ItemStack left = remainder.copy();
            for (int i = 0; i < gridSize && !left.isEmpty(); i++) {
                ItemStack held = staged.get(i);
                if (!held.isEmpty() && ItemStack.areItemsEqual(held, left) && ItemStack.areItemStackTagsEqual(held, left)) {
                    int move = Math.min(left.getCount(), Math.min(held.getMaxStackSize(), getInventoryStackLimit()) - held.getCount());
                    if (move > 0) { held.grow(move); left.shrink(move); }
                }
            }
            for (int i = 0; i < gridSize && !left.isEmpty(); i++) {
                if (staged.get(i).isEmpty()) {
                    int move = Math.min(left.getCount(), Math.min(left.getMaxStackSize(), getInventoryStackLimit()));
                    ItemStack placed = left.copy(); placed.setCount(move);
                    staged.set(i, placed); left.shrink(move);
                }
            }
            if (!left.isEmpty()) return;
        }
        if (!energy.consume(energyPerOperation())) return;
        for (int i = 0; i < gridSize; i++) items.set(i, staged.get(i));
        if (existing.isEmpty()) items.set(outputSlot(), preview.copy());
        else existing.grow(preview.getCount());
        markDirty();
    }

    @Override public int getSizeInventory() { return items.size(); }
    @Override public boolean isEmpty() {
        for (ItemStack stack : items) if (!stack.isEmpty()) return false;
        return true;
    }
    @Override public ItemStack getStackInSlot(int index) { return index < 0 || index >= items.size() ? ItemStack.EMPTY : items.get(index); }
    @Override public ItemStack decrStackSize(int index, int amount) {
        if (index < 0 || index >= items.size()) return ItemStack.EMPTY;
        ItemStack out = ItemStackHelper.getAndSplit(items, index, amount);
        if (!out.isEmpty()) { markDirty(); if (index >= size() && index < outputSlot()) patternChanged(); }
        return out;
    }
    @Override public ItemStack removeStackFromSlot(int index) {
        if (index < 0 || index >= items.size()) return ItemStack.EMPTY;
        ItemStack out = ItemStackHelper.getAndRemove(items, index);
        if (!out.isEmpty()) { markDirty(); if (index >= size() && index < outputSlot()) patternChanged(); }
        return out;
    }
    @Override public void setInventorySlotContents(int index, ItemStack stack) {
        if (index < 0 || index >= items.size()) return;
        ItemStack copy = stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
        if (!copy.isEmpty() && index >= size() && index < outputSlot()) copy.setCount(1);
        else if (!copy.isEmpty() && copy.getCount() > getInventoryStackLimit()) copy.setCount(getInventoryStackLimit());
        items.set(index, copy);
        markDirty();
        if (index >= size() && index < outputSlot()) patternChanged();
    }
    @Override public int getInventoryStackLimit() { return 64; }
    @Override public boolean isUsableByPlayer(EntityPlayer player) {
        return world != null && world.getTileEntity(pos) == this && player.getDistanceSq(pos.add(.5, .5, .5)) <= 64;
    }
    @Override public void openInventory(EntityPlayer player) { }
    @Override public void closeInventory(EntityPlayer player) { }
    @Override public boolean isItemValidForSlot(int index, ItemStack stack) { return index >= 0 && index < size(); }
    @Override public int getField(int id) { return id == 0 ? energy.getEnergyStored() : id == 1 ? redstoneMode : 0; }
    @Override public void setField(int id, int value) {
        if (id == 0) energy.setStored(value);
        else if (id == 1) redstoneMode = value;
    }
    @Override public int getFieldCount() { return 2; }
    @Override public void clear() {
        for (int i = 0; i < items.size(); i++) items.set(i, ItemStack.EMPTY);
        patternChanged();
    }
    @Override public String getName() { return "container.dominatorworkbench.auto"; }
    @Override public boolean hasCustomName() { return false; }
    @Override public ITextComponent getDisplayName() { return new TextComponentTranslation(getName()); }

    @Override public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("Tier", tier);
        tag.setInteger("RedstoneMode", redstoneMode);
        writeItems(tag);
        tag.setInteger("Energy", energy.getEnergyStored());
        if (!preview.isEmpty()) tag.setTag("Preview", preview.serializeNBT());
        return tag;
    }
    @Override public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.tier = Math.max(0, Math.min(6, tag.getInteger("Tier")));
        int slots = (3 + tier * 2) * (3 + tier * 2);
        this.items = NonNullList.withSize(slots * 2 + 1, ItemStack.EMPTY);
        energy.resize(capacityForSize());
        readItems(tag);
        redstoneMode = tag.getInteger("RedstoneMode") % 3;
        energy.setStored(tag.getInteger("Energy"));
        previewDirty = true;
        preview = tag.hasKey("Preview", 10) ? new ItemStack(tag.getCompoundTag("Preview")) : ItemStack.EMPTY;
    }


     




    private void writeItems(NBTTagCompound tag) {
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (stack.isEmpty()) continue;
            NBTTagCompound entry = new NBTTagCompound();
            entry.setInteger("SlotIndex", i);
            stack.writeToNBT(entry);
            list.appendTag(entry);
        }
        tag.setTag("AutoItems", list);
    }

    private void readItems(NBTTagCompound tag) {
        if (tag.hasKey("AutoItems", Constants.NBT.TAG_LIST)) {
            NBTTagList list = tag.getTagList("AutoItems", Constants.NBT.TAG_COMPOUND);
            for (int n = 0; n < list.tagCount(); n++) {
                NBTTagCompound entry = list.getCompoundTagAt(n);
                int slot = entry.getInteger("SlotIndex");
                if (slot >= 0 && slot < items.size()) items.set(slot, new ItemStack(entry));
            }
        } else {
            
            ItemStackHelper.loadAllItems(tag, items);
        }
    }

     
    public NBTTagCompound writePortableData() {
        NBTTagCompound tag = writeToNBT(new NBTTagCompound());
        tag.removeTag("x"); tag.removeTag("y"); tag.removeTag("z"); tag.removeTag("id");
        return tag;
    }

    @Override public NBTTagCompound getUpdateTag() { return writeToNBT(new NBTTagCompound()); }
    @Override public net.minecraft.network.play.server.SPacketUpdateTileEntity getUpdatePacket() {
        return new net.minecraft.network.play.server.SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }
    @Override public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }
    @Override public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }
    @SuppressWarnings("unchecked")
    @Override public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) return (T) energy;
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) itemHandler;
        return super.getCapability(capability, facing);
    }

    private final class AutoItemHandler extends InvWrapper {
        AutoItemHandler() { super(TileAutoWorkbench.this); }
        @Override public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return slot >= 0 && slot < size() ? super.insertItem(slot, stack, simulate) : stack;
        }
        @Override public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return slot == outputSlot() || slot >= 0 && slot < size() && getStackInSlot(slot).getItem() == Items.BUCKET
                    ? super.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
    }
    private final class AutoEnergy extends EnergyStorage {
        AutoEnergy() { super(9000, 10000, 0); }
        void resize(int amount) { capacity = amount; maxReceive = Math.max(10000, amount / 10); energy = Math.min(energy, capacity); }
        boolean consume(int amount) {
            if (energy < amount) return false;
            energy -= amount; markDirty(); return true;
        }
        void setStored(int value) { energy = Math.max(0, Math.min(capacity, value)); }
        @Override public int receiveEnergy(int maxReceive, boolean simulate) {
            int result = super.receiveEnergy(maxReceive, simulate);
            if (result > 0 && !simulate) markDirty();
            return result;
        }
    }
}