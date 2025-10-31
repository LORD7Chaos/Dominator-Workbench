package com.lord.dominatorworkbench.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.lord.dominatorworkbench.DominatorWorkbenchMod;
import com.lord.dominatorworkbench.gui.GuiHandler;
import net.minecraft.util.BlockRenderLayer;

public class BlockDominatorWorkbench extends Block {

    public BlockDominatorWorkbench() {
        super(Material.WOOD);
        this.setRegistryName("dominator_workbench");
        this.setTranslationKey("dominator_workbench");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setHardness(2.5F);
        this.setResistance(5.0F);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(DominatorWorkbenchMod.instance, GuiHandler.DOMINATOR_WORKBENCH_GUI, world,
                    pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }


    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}