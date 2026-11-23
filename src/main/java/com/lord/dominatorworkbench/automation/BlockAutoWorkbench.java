package com.lord.dominatorworkbench.automation;

import com.lord.dominatorworkbench.DominatorWorkbenchMod;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Random;

 
public class BlockAutoWorkbench extends BlockContainer {
    public static final PropertyInteger TIER = PropertyInteger.create("tier", 0, 6);
    public BlockAutoWorkbench() {
        super(Material.IRON);
        setRegistryName(DominatorWorkbenchMod.MODID, "auto_workbench");
        setTranslationKey("auto_workbench");
        setDefaultState(blockState.getBaseState().withProperty(TIER, 0));
        setHardness(50f);
        setResistance(2000f);
        setHarvestLevel("pickaxe", 3);
        setSoundType(SoundType.GLASS);
        setCreativeTab(DominatorWorkbenchMod.dominatorWorkbenchTab);
    }
    @Override protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, TIER); }
    @Override public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TIER, Math.max(0, Math.min(6, meta)));
    }
    @Override public int getMetaFromState(IBlockState state) { return state.getValue(TIER); }
    @Override public int damageDropped(IBlockState state) { return getMetaFromState(state); }
    @Override public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == DominatorWorkbenchMod.dominatorWorkbenchTab || tab == CreativeTabs.SEARCH)
            for (int tier = 0; tier < 7; tier++) items.add(new ItemStack(this, 1, tier));
    }
    @Override public EnumBlockRenderType getRenderType(IBlockState state) { return EnumBlockRenderType.MODEL; }
    
    @Override public boolean isOpaqueCube(IBlockState state) { return false; }
    @Override public boolean isFullCube(IBlockState state) { return false; }
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    @Override public TileEntity createNewTileEntity(World world, int metadata) {
        TileAutoWorkbench tile = new TileAutoWorkbench();
        tile.setTier(metadata);
        return tile;
    }
    @Override public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) player.openGui(DominatorWorkbenchMod.instance, 100 + state.getValue(TIER), world,
                pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
    @Override public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack item) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileAutoWorkbench) {
            TileAutoWorkbench auto = (TileAutoWorkbench) tile;
            if (item.hasTagCompound() && item.getTagCompound().hasKey("AutoData", 10)) {
                NBTTagCompound tag = item.getTagCompound().getCompoundTag("AutoData").copy();
                tag.setInteger("Tier", state.getValue(TIER));
                tag.setInteger("x", pos.getX());
                tag.setInteger("y", pos.getY());
                tag.setInteger("z", pos.getZ());
                tag.setString("id", DominatorWorkbenchMod.MODID + ":auto_workbench");
                auto.readFromNBT(tag);
            } else auto.setTier(state.getValue(TIER));
            auto.patternChanged();
        }
    }
    @Override public Item getItemDropped(IBlockState state, Random random, int fortune) { return Items.AIR; }
    @Override public int quantityDropped(Random random) { return 0; }
    @Override public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            ItemStack dropped = new ItemStack(this, 1, getMetaFromState(state));
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileAutoWorkbench) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setTag("AutoData", ((TileAutoWorkbench) tile).writePortableData());
                dropped.setTagCompound(tag);
            }
            world.spawnEntity(new EntityItem(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, dropped));
        }
        super.breakBlock(world, pos, state);
    }
}
