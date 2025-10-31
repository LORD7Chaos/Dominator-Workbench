package com.lord.dominatorworkbench;

import com.lord.dominatorworkbench.blocks.BlockDominatorWorkbench;
import com.lord.dominatorworkbench.gui.GuiHandler;
import com.lord.dominatorworkbench.recipes.DominatorRecipes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = DominatorWorkbenchMod.MODID, name = DominatorWorkbenchMod.NAME, version = DominatorWorkbenchMod.VERSION, dependencies = "required-after:crafttweaker;after:jei")
@Mod.EventBusSubscriber
public class DominatorWorkbenchMod {
    public static final String MODID = "dominatorworkbench";
    public static final String NAME = "Dominator Workbench";
    public static final String VERSION = "0.1.0";

    @Mod.Instance(MODID)
    public static DominatorWorkbenchMod instance;

    public static Block dominatorWorkbench;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        DominatorRecipes.registerRecipes();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        DominatorRecipes.getRecipeCount();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        dominatorWorkbench = new BlockDominatorWorkbench();
        event.getRegistry().register(dominatorWorkbench);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        if (dominatorWorkbench != null) {
            ItemBlock itemBlock = new ItemBlock(dominatorWorkbench);
            itemBlock.setRegistryName(dominatorWorkbench.getRegistryName());
            event.getRegistry().register(itemBlock);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        if (dominatorWorkbench != null) {
            ((BlockDominatorWorkbench) dominatorWorkbench).initModel();
        }
    }
}