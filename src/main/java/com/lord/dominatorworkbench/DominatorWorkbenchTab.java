package com.lord.dominatorworkbench;

import com.lord.dominatorworkbench.items.ItemCraftCreator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DominatorWorkbenchTab extends CreativeTabs {

    public DominatorWorkbenchTab() {
        super("dominatorworkbench");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack createIcon() {
        if (DominatorWorkbenchMod.craftCreator != null) {
            return new ItemStack(DominatorWorkbenchMod.craftCreator);
        }
        return ItemStack.EMPTY;
    }
}