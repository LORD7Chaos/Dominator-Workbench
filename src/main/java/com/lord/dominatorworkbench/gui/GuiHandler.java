package com.lord.dominatorworkbench.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import com.lord.dominatorworkbench.container.ContainerDominatorWorkbench;

public class GuiHandler implements IGuiHandler {

    public static final int DOMINATOR_WORKBENCH_GUI = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == DOMINATOR_WORKBENCH_GUI) {
            return new ContainerDominatorWorkbench(player.inventory, world);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == DOMINATOR_WORKBENCH_GUI) {
            return new GuiDominatorWorkbench(player);
        }
        return null;
    }
}