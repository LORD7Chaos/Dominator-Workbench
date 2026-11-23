package com.lord.dominatorworkbench.blocks;

import com.lord.dominatorworkbench.DominatorWorkbenchMod;
import com.lord.dominatorworkbench.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockUnifiedWorkbench extends Block {
    public static final PropertyInteger TIER = PropertyInteger.create("tier", 0, 6);

    public BlockUnifiedWorkbench() {
        super(Material.WOOD);
        setRegistryName(DominatorWorkbenchMod.MODID, "workbench");
        setTranslationKey("workbench");
        setDefaultState(blockState.getBaseState().withProperty(TIER, 0));
        setCreativeTab(DominatorWorkbenchMod.dominatorWorkbenchTab);
        setHardness(2.0F);
        setResistance(3.0F);
    }

    @Override protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TIER);
    }

    @Override public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TIER, Math.max(0, Math.min(6, meta)));
    }

    @Override public int getMetaFromState(IBlockState state) {
        return state.getValue(TIER);
    }

    @Override public int damageDropped(IBlockState state) { return getMetaFromState(state); }

    @Override public void getSubBlocks(net.minecraft.creativetab.CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == DominatorWorkbenchMod.dominatorWorkbenchTab || tab == net.minecraft.creativetab.CreativeTabs.SEARCH) {
            for (int tier = 0; tier < 7; tier++) items.add(new ItemStack(this, 1, tier));
        }
    }

    @Override public boolean hasTileEntity(IBlockState state) { return true; }

    @Override public TileEntity createTileEntity(World world, IBlockState state) {
        switch (state.getValue(TIER)) {
            case 0: return new TileEntityDominatorWorkbenchBasic();
            case 1: return new TileEntityDominatorWorkbenchEnhanced();
            case 2: return new TileEntityDominatorWorkbenchMaster();
            case 3: return new TileEntityDominatorWorkbenchTitan();
            case 4: return new TileEntityDominatorWorkbench();
            case 5: return new TileEntityDominatorWorkbenchOmega();
            default: return new TileEntityDominatorWorkbenchUniverse();
        }
    }

    @Override public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
                                                EnumHand hand, EnumFacing facing, float x, float y, float z) {
        if (!world.isRemote) player.openGui(DominatorWorkbenchMod.instance, state.getValue(TIER), world,
                pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityDominatorWorkbenchBasic) ((TileEntityDominatorWorkbenchBasic) te).dropItems();
        else if (te instanceof TileEntityDominatorWorkbenchEnhanced) ((TileEntityDominatorWorkbenchEnhanced) te).dropItems();
        else if (te instanceof TileEntityDominatorWorkbenchMaster) ((TileEntityDominatorWorkbenchMaster) te).dropItems();
        else if (te instanceof TileEntityDominatorWorkbenchTitan) ((TileEntityDominatorWorkbenchTitan) te).dropItems();
        else if (te instanceof TileEntityDominatorWorkbench) ((TileEntityDominatorWorkbench) te).dropItems();
        else if (te instanceof TileEntityDominatorWorkbenchOmega) ((TileEntityDominatorWorkbenchOmega) te).dropItems();
        else if (te instanceof TileEntityDominatorWorkbenchUniverse) ((TileEntityDominatorWorkbenchUniverse) te).dropItems();
        super.breakBlock(world, pos, state);
    }
}
