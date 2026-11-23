package com.lord.dominatorworkbench.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.lord.dominatorworkbench.DominatorWorkbenchMod;

@Config(modid = DominatorWorkbenchMod.MODID, name = "dominatorworkbench/config")
public class ConfigHandler {

    @Config.LangKey("config.dominatorworkbench.workbenches")
    @Config.Comment("Enable/Disable specific workbenches")
    public static Workbenches workbenches = new Workbenches();

    @Config.Comment("Automatic workbench settings (based on Avaritiaddons defaults)")
    public static Automation automation = new Automation();

    public static class Automation {
        @Config.RangeInt(min = 1, max = 32767)
        @Config.Comment("FE consumed per crafting slot per operation; Avaritiaddons default: 10")
        public int powerMultiplier = 10;
        @Config.RangeInt(min = 1, max = 32767)
        @Config.Comment("Energy capacity = slots * powerMultiplier * capacityMultiplier; default: 100")
        public int capacityMultiplier = 100;
    }


    public static class Workbenches {

        @Config.LangKey("config.dominatorworkbench.workbenches.basic")
        @Config.Comment("Enable Basic Workbench (3x3)")
        @Config.RequiresMcRestart
        public boolean basic = true;

        @Config.LangKey("config.dominatorworkbench.workbenches.enhanced")
        @Config.Comment("Enable Enhanced Workbench (5x5)")
        @Config.RequiresMcRestart
        public boolean enhanced = true;

        @Config.LangKey("config.dominatorworkbench.workbenches.master")
        @Config.Comment("Enable Master Workbench (7x7)")
        @Config.RequiresMcRestart
        public boolean master = true;

        @Config.LangKey("config.dominatorworkbench.workbenches.titan")
        @Config.Comment("Enable Titan Workbench (9x9)")
        @Config.RequiresMcRestart
        public boolean titan = true;

        @Config.LangKey("config.dominatorworkbench.workbenches.dominator")
        @Config.Comment("Enable Dominator Workbench (11x11)")
        @Config.RequiresMcRestart
        public boolean dominator = true;

        @Config.LangKey("config.dominatorworkbench.workbenches.omega")
        @Config.Comment("Enable Omega Workbench (13x13)")
        @Config.RequiresMcRestart
        public boolean omega = true;

        @Config.LangKey("config.dominatorworkbench.workbenches.universe")
        @Config.Comment("Enable Universe Workbench (15x15)")
        @Config.RequiresMcRestart
        public boolean universe = true;
    }

    @Mod.EventBusSubscriber(modid = DominatorWorkbenchMod.MODID)
    public static class ConfigSyncHandler {

        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(DominatorWorkbenchMod.MODID)) {
                ConfigManager.sync(DominatorWorkbenchMod.MODID, Config.Type.INSTANCE);
                DominatorWorkbenchMod.logger.info("Configuration has been refreshed!");
            }
        }
    }
}