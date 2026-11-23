package com.lord.dominatorworkbench.items;

import com.lord.dominatorworkbench.blocks.*;
import com.lord.dominatorworkbench.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

public class ItemCraftCreator extends Item {

    public ItemCraftCreator() {
        setRegistryName("craft_creator");
        setTranslationKey("craft_creator");
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos,
                                      EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (world.isRemote) return EnumActionResult.SUCCESS;

        Block block = world.getBlockState(pos).getBlock();
        TileEntity te = world.getTileEntity(pos);

        int size = getWorkbenchSize(block, te);
        if (size == 0) {
            player.sendMessage(new TextComponentTranslation("message.dominatorworkbench.craft_creator.invalid_workbench"));
            return EnumActionResult.FAIL;
        }

        if (player.isSneaking()) {
            copyRecipeToClipboard(player, te, size);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    private int getWorkbenchSize(Block block, TileEntity te) {
        if (block instanceof BlockDominatorWorkbenchBasic && te instanceof TileEntityDominatorWorkbenchBasic) {
            return 3;
        } else if (block instanceof BlockDominatorWorkbenchEnhanced && te instanceof TileEntityDominatorWorkbenchEnhanced) {
            return 5;
        } else if (block instanceof BlockDominatorWorkbenchMaster && te instanceof TileEntityDominatorWorkbenchMaster) {
            return 7;
        } else if (block instanceof BlockDominatorWorkbenchTitan && te instanceof TileEntityDominatorWorkbenchTitan) {
            return 9;
        } else if (block instanceof BlockDominatorWorkbench && te instanceof TileEntityDominatorWorkbench) {
            return 11;
        } else if (block instanceof BlockDominatorWorkbenchOmega && te instanceof TileEntityDominatorWorkbenchOmega) {
            return 13;
        } else if (block instanceof BlockDominatorWorkbenchUniverse && te instanceof TileEntityDominatorWorkbenchUniverse) {
            return 15;
        }
        return 0;
    }

    private String getWorkbenchName(int size) {
        switch (size) {
            case 3: return "DominatorWorkbenchBasic";
            case 5: return "DominatorWorkbenchEnhanced";
            case 7: return "DominatorWorkbenchMaster";
            case 9: return "DominatorWorkbenchTitan";
            case 11: return "DominatorWorkbench";
            case 13: return "DominatorWorkbenchOmega";
            case 15: return "DominatorWorkbenchUniverse";
            default: return "DominatorWorkbench";
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (!world.isRemote) {
            if (player.isSneaking()) {
                toggleMode(player, stack);
            }
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    private void copyRecipeToClipboard(EntityPlayer player, TileEntity te, int size) {
        ItemStack itemInHand = player.getHeldItemMainhand();
        String mode = getMode(itemInHand);

        boolean hasItems = false;
        int craftSlots = size * size;
        for (int i = 0; i < craftSlots; i++) {
            ItemStack stack = getStackFromTile(te, i);
            if (stack != null && !stack.isEmpty()) {
                hasItems = true;
                break;
            }
        }

        if (!hasItems) {
            player.sendMessage(new TextComponentTranslation("message.dominatorworkbench.craft_creator.empty"));
            return;
        }

        String recipeString = generateRecipeString(te, mode, size);

        try {
            StringSelection selection = new StringSelection(recipeString);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);

            player.sendMessage(new TextComponentTranslation("message.dominatorworkbench.craft_creator.copied", mode));
        } catch (Exception e) {
            player.sendMessage(new TextComponentTranslation("message.dominatorworkbench.craft_creator.copy_error"));
            e.printStackTrace();
        }
    }

    private void toggleMode(EntityPlayer player, ItemStack stack) {
        String currentMode = getMode(stack);
        String newMode = currentMode.equals("Shaped") ? "Shapeless" : "Shaped";
        setMode(stack, newMode);

        String modeKey = newMode.equals("Shaped") ? "shaped" : "shapeless";
        player.sendMessage(new TextComponentTranslation("message.dominatorworkbench.craft_creator.mode_changed",
                new TextComponentTranslation("mode.dominatorworkbench." + modeKey).getFormattedText()));
    }

    private String generateRecipeString(TileEntity te, String mode, int size) {
        String workbenchName = getWorkbenchName(size);
        StringBuilder sb = new StringBuilder();
        sb.append(workbenchName).append(".add").append(mode).append("Recipe(null, ");

        if (mode.equals("Shaped")) {
            sb.append(generateShapedRecipe(te, size));
        } else {
            sb.append(generateShapelessRecipe(te, size));
        }

        sb.append(");\n");
        return sb.toString();
    }

    private String generateShapedRecipe(TileEntity te, int size) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n    [\n");

        int minX = size, maxX = -1, minY = size, maxY = -1;

        
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                ItemStack stack = getStackFromTile(te, x + y * size);
                if (stack != null && !stack.isEmpty()) {
                    minX = Math.min(minX, x);
                    maxX = Math.max(maxX, x);
                    minY = Math.min(minY, y);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        
        int recipeHeight = maxY - minY + 1;
        int recipeWidth = maxX - minX + 1;

        for (int y = minY; y <= maxY; y++) {
            sb.append("        [");
            for (int x = minX; x <= maxX; x++) {
                ItemStack stack = getStackFromTile(te, x + y * size);
                if (stack != null && !stack.isEmpty()) {
                    sb.append(formatItemStack(stack));
                } else {
                    sb.append("null");
                }
                if (x < maxX) sb.append(", ");
            }
            sb.append("]");
            if (y < maxY) sb.append(",");
            sb.append("\n");
        }

        sb.append("    ]");
        return sb.toString();
    }

    private String generateShapelessRecipe(TileEntity te, int size) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n    [\n");

        List<String> items = new ArrayList<>();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                ItemStack stack = getStackFromTile(te, x + y * size);
                if (stack != null && !stack.isEmpty()) {
                    items.add(formatItemStack(stack));
                }
            }
        }

        sb.append("        ");
        for (int i = 0; i < items.size(); i++) {
            sb.append(items.get(i));
            if (i < items.size() - 1) sb.append(", ");
        }

        sb.append("\n    ]");
        return sb.toString();
    }

    private ItemStack getStackFromTile(TileEntity te, int slot) {
        if (te instanceof TileEntityDominatorWorkbenchBasic) {
            return ((TileEntityDominatorWorkbenchBasic) te).getStackInSlot(slot);
        } else if (te instanceof TileEntityDominatorWorkbenchEnhanced) {
            return ((TileEntityDominatorWorkbenchEnhanced) te).getStackInSlot(slot);
        } else if (te instanceof TileEntityDominatorWorkbenchMaster) {
            return ((TileEntityDominatorWorkbenchMaster) te).getStackInSlot(slot);
        } else if (te instanceof TileEntityDominatorWorkbenchTitan) {
            return ((TileEntityDominatorWorkbenchTitan) te).getStackInSlot(slot);
        } else if (te instanceof TileEntityDominatorWorkbench) {
            return ((TileEntityDominatorWorkbench) te).getStackInSlot(slot);
        } else if (te instanceof TileEntityDominatorWorkbenchOmega) {
            return ((TileEntityDominatorWorkbenchOmega) te).getStackInSlot(slot);
        } else if (te instanceof TileEntityDominatorWorkbenchUniverse) {
            return ((TileEntityDominatorWorkbenchUniverse) te).getStackInSlot(slot);
        }
        return ItemStack.EMPTY;
    }

    private String formatItemStack(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return "null";

        StringBuilder sb = new StringBuilder();
        sb.append("<").append(stack.getItem().getRegistryName());

        if (stack.getMetadata() != 0) {
            sb.append(":").append(stack.getMetadata());
        }

        sb.append(">");

        if (stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();
            String nbtString = convertNBTToZenScript(tag);
            if (!nbtString.isEmpty()) {
                sb.append(".withTag(").append(nbtString).append(")");
            }
        }

        if (stack.getCount() > 1) {
            sb.append(" * ").append(stack.getCount());
        }

        return sb.toString();
    }

    private String convertNBTToZenScript(NBTTagCompound tag) {
        if (tag == null || tag.getKeySet().isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        boolean first = true;
        for (String key : tag.getKeySet()) {
            if (!first) sb.append(", ");
            first = false;

            sb.append(escapeZenScriptString(key)).append(": ");

            switch (tag.getTagId(key)) {
                case 1:
                    sb.append(tag.getByte(key)).append(" as byte");
                    break;
                case 2:
                    sb.append(tag.getShort(key)).append(" as short");
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
                    sb.append(tag.getTag(key));
                    break;
                case 7:
                    byte[] byteArray = tag.getByteArray(key);
                    sb.append("[");
                    for (int i = 0; i < byteArray.length; i++) {
                        if (i > 0) sb.append(", ");
                        sb.append(byteArray[i]).append(" as byte");
                    }
                    sb.append("]");
                    break;
                case 8:
                    sb.append("\"").append(escapeZenScriptString(tag.getString(key))).append("\"");
                    break;
                case 9:
                    net.minecraft.nbt.NBTTagList list = tag.getTagList(key, 10);
                    sb.append("[");
                    for (int i = 0; i < list.tagCount(); i++) {
                        if (i > 0) sb.append(", ");
                        sb.append(convertNBTToZenScript(list.getCompoundTagAt(i)));
                    }
                    sb.append("]");
                    break;
                case 10:
                    sb.append(convertNBTToZenScript(tag.getCompoundTag(key)));
                    break;
                case 11:
                    int[] intArray = tag.getIntArray(key);
                    sb.append("[");
                    for (int i = 0; i < intArray.length; i++) {
                        if (i > 0) sb.append(", ");
                        sb.append(intArray[i]);
                    }
                    sb.append("]");
                    break;
                default:
                    sb.append("null");
                    break;
            }
        }

        sb.append("}");
        return sb.toString();
    }

    private String escapeZenScriptString(String str) {
        return str.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    private String getMode(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setString("Mode", "Shaped");
        }
        return stack.getTagCompound().getString("Mode");
    }

    private void setMode(ItemStack stack, String mode) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setString("Mode", mode);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String mode = getMode(stack);
        String modeKey = mode.equals("Shaped") ? "shaped" : "shapeless";

        tooltip.add(I18n.format("tooltip.dominatorworkbench.craft_creator.mode",
                I18n.format("mode.dominatorworkbench." + modeKey)));
        tooltip.add("");
        tooltip.add(I18n.format("tooltip.dominatorworkbench.craft_creator.copy"));
        tooltip.add(I18n.format("tooltip.dominatorworkbench.craft_creator.switch"));
        tooltip.add("");
        tooltip.add(I18n.format("tooltip.dominatorworkbench.craft_creator.instruction1"));
        tooltip.add(I18n.format("tooltip.dominatorworkbench.craft_creator.instruction2"));
    }
}