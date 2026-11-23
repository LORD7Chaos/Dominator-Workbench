package com.lord.dominatorworkbench;

import com.lord.dominatorworkbench.blocks.*;
import com.lord.dominatorworkbench.automation.*;
import com.lord.dominatorworkbench.config.ConfigHandler;
import com.lord.dominatorworkbench.gui.GuiHandler;
import com.lord.dominatorworkbench.items.ItemCraftCreator;
import com.lord.dominatorworkbench.recipes.basic.DominatorRecipesBasic;
import com.lord.dominatorworkbench.recipes.dominator.DominatorRecipes;
import com.lord.dominatorworkbench.recipes.enhanced.DominatorRecipesEnhanced;
import com.lord.dominatorworkbench.recipes.master.DominatorRecipesMaster;
import com.lord.dominatorworkbench.recipes.omega.DominatorRecipesOmega;
import com.lord.dominatorworkbench.recipes.titan.DominatorRecipesTitan;
import com.lord.dominatorworkbench.recipes.universe.DominatorRecipesUniverse;
import com.lord.dominatorworkbench.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

@Mod(modid = DominatorWorkbenchMod.MODID,
        name = DominatorWorkbenchMod.NAME,
        version = DominatorWorkbenchMod.VERSION,
        dependencies = "after:crafttweaker;after:jei",
        acceptedMinecraftVersions = "[1.12,1.12.2]")
@Mod.EventBusSubscriber
public class DominatorWorkbenchMod {
    public static final String MODID = "dominatorworkbench";
    public static final String NAME = "Dominator Workbench";
    public static final String VERSION = "4.0.0";

    @Mod.Instance(MODID)
    public static DominatorWorkbenchMod instance;

    public static Logger logger;

    
    public static CreativeTabs dominatorWorkbenchTab;

    

    public static Item craftCreator;
    public static Block unifiedWorkbench;
    public static BlockAutoWorkbench autoWorkbench;
    public static net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper autoNetwork;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        autoNetwork = NetworkRegistry.INSTANCE.newSimpleChannel("dwb_auto");
        autoNetwork.registerMessage(PatternPacket.Handler.class, PatternPacket.class, 0, Side.SERVER);

        
        dominatorWorkbenchTab = new DominatorWorkbenchTab();

        
        registerWorkbenchTileEntities();
        GameRegistry.registerTileEntity(TileAutoWorkbench.class, new ResourceLocation(MODID, "auto_workbench"));

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }

    private void registerWorkbenchTileEntities() {
        if (ConfigHandler.workbenches.basic) {
            GameRegistry.registerTileEntity(TileEntityDominatorWorkbenchBasic.class,
                    new ResourceLocation(MODID, "dominator_workbench_basic"));
        }
        if (ConfigHandler.workbenches.enhanced) {
            GameRegistry.registerTileEntity(TileEntityDominatorWorkbenchEnhanced.class,
                    new ResourceLocation(MODID, "dominator_workbench_enhanced"));
        }
        if (ConfigHandler.workbenches.master) {
            GameRegistry.registerTileEntity(TileEntityDominatorWorkbenchMaster.class,
                    new ResourceLocation(MODID, "dominator_workbench_master"));
        }
        if (ConfigHandler.workbenches.titan) {
            GameRegistry.registerTileEntity(TileEntityDominatorWorkbenchTitan.class,
                    new ResourceLocation(MODID, "dominator_workbench_titan"));
        }
        if (ConfigHandler.workbenches.dominator) {
            GameRegistry.registerTileEntity(TileEntityDominatorWorkbench.class,
                    new ResourceLocation(MODID, "dominator_workbench"));
        }
        if (ConfigHandler.workbenches.omega) {
            GameRegistry.registerTileEntity(TileEntityDominatorWorkbenchOmega.class,
                    new ResourceLocation(MODID, "dominator_workbench_omega"));
        }
        if (ConfigHandler.workbenches.universe) {
            GameRegistry.registerTileEntity(TileEntityDominatorWorkbenchUniverse.class,
                    new ResourceLocation(MODID, "dominator_workbench_universe"));
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        registerRecipes();
    }


    private void registerRecipes() {
        if (ConfigHandler.workbenches.basic) DominatorRecipesBasic.registerRecipes();
        if (ConfigHandler.workbenches.enhanced) DominatorRecipesEnhanced.registerRecipes();
        if (ConfigHandler.workbenches.master) DominatorRecipesMaster.registerRecipes();
        if (ConfigHandler.workbenches.titan) DominatorRecipesTitan.registerRecipes();
        if (ConfigHandler.workbenches.dominator) DominatorRecipes.registerRecipes();
        if (ConfigHandler.workbenches.omega) DominatorRecipesOmega.registerRecipes();
        if (ConfigHandler.workbenches.universe) DominatorRecipesUniverse.registerRecipes();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        unifiedWorkbench = new com.lord.dominatorworkbench.blocks.BlockUnifiedWorkbench();
        event.getRegistry().register(unifiedWorkbench);
        autoWorkbench = new BlockAutoWorkbench();
        event.getRegistry().register(autoWorkbench);

    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new com.lord.dominatorworkbench.items.ItemBlockUnifiedWorkbench(unifiedWorkbench));
        event.getRegistry().register(new ItemBlockAutoWorkbench(autoWorkbench));

        craftCreator = new ItemCraftCreator();
        craftCreator.setCreativeTab(dominatorWorkbenchTab);
        event.getRegistry().register(craftCreator);

    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {

        for (int tier = 0; tier < 7; tier++) {
            net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(unifiedWorkbench), tier,
                new net.minecraft.client.renderer.block.model.ModelResourceLocation(
                    MODID + ":workbench_" + tier, "inventory"));
        }

        for (int tier = 0; tier < 7; tier++) {
            net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(autoWorkbench), tier,
                new net.minecraft.client.renderer.block.model.ModelResourceLocation(
                    MODID + ":auto_workbench_" + tier, "inventory"));
        }
        if (craftCreator != null) {
            net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(
                    craftCreator, 0,
                    new net.minecraft.client.renderer.block.model.ModelResourceLocation(
                            craftCreator.getRegistryName(), "inventory"
                    )
            );
        }
    }


}