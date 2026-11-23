package com.lord.dominatorworkbench.blocks;

import com.lord.dominatorworkbench.tileentity.TileEntityDominatorWorkbenchTitan;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.lord.dominatorworkbench.DominatorWorkbenchMod;
import com.lord.dominatorworkbench.gui.GuiHandler;

public class BlockDominatorWorkbenchTitan extends Block {

    public BlockDominatorWorkbenchTitan() {
        super(Material.WOOD);
        this.setRegistryName("dominator_workbench_titan");
        this.setTranslationKey("dominator_workbench_titan");
        this.setCreativeTab(DominatorWorkbenchMod.dominatorWorkbenchTab);
        this.setHardness(2.8F);
        this.setResistance(4.5F);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(DominatorWorkbenchMod.instance, GuiHandler.DOMINATOR_WORKBENCH_TITAN_GUI, world,
                    pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityDominatorWorkbenchTitan();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityDominatorWorkbenchTitan) {
            ((TileEntityDominatorWorkbenchTitan) te).dropItems();
        }
        super.breakBlock(worldIn, pos, state);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}