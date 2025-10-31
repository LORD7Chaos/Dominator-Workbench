package com.lord.dominatorworkbench.jei;

import net.minecraft.item.ItemStack;
import java.util.Collection;

public class JEIHelper {

    public static boolean isItemVisibleInJEI(ItemStack stack) {
        try {
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public static void reloadJEI() {
        try {
            System.out.println("JEI reload requested - would reload JEI recipes here");
        } catch (Exception e) {
            System.err.println("Failed to reload JEI: " + e.getMessage());
        }
    }

    public static void refreshRecipes() {

        try {
            Class<?> jeiPluginClass = Class.forName("com.lord.dominatorworkbench.jei.DominatorWorkbenchPlugin");
        } catch (ClassNotFoundException e) {
        }
    }
}