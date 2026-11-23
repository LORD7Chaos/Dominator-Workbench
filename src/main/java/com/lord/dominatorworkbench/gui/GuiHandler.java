package com.lord.dominatorworkbench.gui;

import com.lord.dominatorworkbench.container.basic.ContainerDominatorWorkbenchBasic;
import com.lord.dominatorworkbench.container.dominator.ContainerDominatorWorkbench;
import com.lord.dominatorworkbench.container.enhanced.ContainerDominatorWorkbenchEnhanced;
import com.lord.dominatorworkbench.container.master.ContainerDominatorWorkbenchMaster;
import com.lord.dominatorworkbench.container.omega.ContainerDominatorWorkbenchOmega;
import com.lord.dominatorworkbench.container.titan.ContainerDominatorWorkbenchTitan;
import com.lord.dominatorworkbench.container.universe.ContainerDominatorWorkbenchUniverse;
import com.lord.dominatorworkbench.tileentity.*;
import com.lord.dominatorworkbench.automation.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int DOMINATOR_WORKBENCH_BASIC_GUI = 0;
    public static final int DOMINATOR_WORKBENCH_ENHANCED_GUI = 1;
    public static final int DOMINATOR_WORKBENCH_MASTER_GUI = 2;
    public static final int DOMINATOR_WORKBENCH_TITAN_GUI = 3;
    public static final int DOMINATOR_WORKBENCH_GUI = 4;
    public static final int DOMINATOR_WORKBENCH_OMEGA_GUI = 5;
    public static final int DOMINATOR_WORKBENCH_UNIVERSE_GUI = 6;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);

        if (ID >= 100 && ID <= 106 && te instanceof TileAutoWorkbench
                && ((TileAutoWorkbench) te).getTier() == ID - 100) {
            return new ContainerAutoWorkbench(player.inventory, (TileAutoWorkbench) te);
        }
        switch (ID) {
            case DOMINATOR_WORKBENCH_BASIC_GUI:
                if (te instanceof TileEntityDominatorWorkbenchBasic) {
                    return new ContainerDominatorWorkbenchBasic(player.inventory, (TileEntityDominatorWorkbenchBasic) te);
                }
                break;
            case DOMINATOR_WORKBENCH_ENHANCED_GUI:
                if (te instanceof TileEntityDominatorWorkbenchEnhanced) {
                    return new ContainerDominatorWorkbenchEnhanced(player.inventory, (TileEntityDominatorWorkbenchEnhanced) te);
                }
                break;
            case DOMINATOR_WORKBENCH_MASTER_GUI:
                if (te instanceof TileEntityDominatorWorkbenchMaster) {
                    return new ContainerDominatorWorkbenchMaster(player.inventory, (TileEntityDominatorWorkbenchMaster) te);
                }
                break;
            case DOMINATOR_WORKBENCH_TITAN_GUI:
                if (te instanceof TileEntityDominatorWorkbenchTitan) {
                    return new ContainerDominatorWorkbenchTitan(player.inventory, (TileEntityDominatorWorkbenchTitan) te);
                }
                break;
            case DOMINATOR_WORKBENCH_GUI:
                if (te instanceof TileEntityDominatorWorkbench) {
                    return new ContainerDominatorWorkbench(player.inventory, (TileEntityDominatorWorkbench) te);
                }
                break;
            case DOMINATOR_WORKBENCH_OMEGA_GUI:
                if (te instanceof TileEntityDominatorWorkbenchOmega) {
                    return new ContainerDominatorWorkbenchOmega(player.inventory, (TileEntityDominatorWorkbenchOmega) te);
                }
                break;
            case DOMINATOR_WORKBENCH_UNIVERSE_GUI:
                if (te instanceof TileEntityDominatorWorkbenchUniverse) {
                    return new ContainerDominatorWorkbenchUniverse(player.inventory, (TileEntityDominatorWorkbenchUniverse) te);
                }
                break;
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);

        if (ID >= 100 && ID <= 106 && te instanceof TileAutoWorkbench
                && ((TileAutoWorkbench) te).getTier() == ID - 100) {
            return new GuiAutoWorkbench(player.inventory, (TileAutoWorkbench) te);
        }
        switch (ID) {
            case DOMINATOR_WORKBENCH_BASIC_GUI:
                if (te instanceof TileEntityDominatorWorkbenchBasic) {
                    return new GuiDominatorWorkbenchBasic(player.inventory, (TileEntityDominatorWorkbenchBasic) te);
                }
                break;
            case DOMINATOR_WORKBENCH_ENHANCED_GUI:
                if (te instanceof TileEntityDominatorWorkbenchEnhanced) {
                    return new GuiDominatorWorkbenchEnhanced(player.inventory, (TileEntityDominatorWorkbenchEnhanced) te);
                }
                break;
            case DOMINATOR_WORKBENCH_MASTER_GUI:
                if (te instanceof TileEntityDominatorWorkbenchMaster) {
                    return new GuiDominatorWorkbenchMaster(player.inventory, (TileEntityDominatorWorkbenchMaster) te);
                }
                break;
            case DOMINATOR_WORKBENCH_TITAN_GUI:
                if (te instanceof TileEntityDominatorWorkbenchTitan) {
                    return new GuiDominatorWorkbenchTitan(player.inventory, (TileEntityDominatorWorkbenchTitan) te);
                }
                break;
            case DOMINATOR_WORKBENCH_GUI:
                if (te instanceof TileEntityDominatorWorkbench) {
                    return new GuiDominatorWorkbench(player.inventory, (TileEntityDominatorWorkbench) te);
                }
                break;
            case DOMINATOR_WORKBENCH_OMEGA_GUI:
                if (te instanceof TileEntityDominatorWorkbenchOmega) {
                    return new GuiDominatorWorkbenchOmega(player.inventory, (TileEntityDominatorWorkbenchOmega) te);
                }
                break;
            case DOMINATOR_WORKBENCH_UNIVERSE_GUI:
                if (te instanceof TileEntityDominatorWorkbenchUniverse) {
                    return new GuiDominatorWorkbenchUniverse(player.inventory, (TileEntityDominatorWorkbenchUniverse) te);
                }
                break;
        }
        return null;
    }
}