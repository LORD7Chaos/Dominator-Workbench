package com.lord.dominatorworkbench.automation;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import java.util.ArrayList;
import java.util.List;

 
public class PatternPacket implements IMessage {
    private int windowId, index;
    private List<ItemStack> stacks = new ArrayList<>();
    public PatternPacket() { }
    public static PatternPacket single(int window, int target, ItemStack stack) {
        PatternPacket packet = new PatternPacket();
        packet.windowId = window; packet.index = target;
        packet.stacks.add(stack.copy());
        return packet;
    }
    public static PatternPacket full(int window, List<ItemStack> pattern) {
        PatternPacket packet = new PatternPacket();
        packet.windowId = window; packet.index = -1;
        for (ItemStack stack : pattern) packet.stacks.add(stack.copy());
        return packet;
    }
    @Override public void toBytes(ByteBuf buf) {
        buf.writeInt(windowId); buf.writeInt(index);
        buf.writeShort(stacks.size());
        for (ItemStack stack : stacks) ByteBufUtils.writeItemStack(buf, stack);
    }
    @Override public void fromBytes(ByteBuf buf) {
        windowId = buf.readInt(); index = buf.readInt();
        int count = buf.readUnsignedShort();
        if (count > 225) throw new IllegalArgumentException("Too many pattern slots");
        for (int i = 0; i < count; i++) stacks.add(ByteBufUtils.readItemStack(buf));
    }
    public static final class Handler implements IMessageHandler<PatternPacket, IMessage> {
        @Override public IMessage onMessage(PatternPacket message, MessageContext context) {
            EntityPlayerMP player = context.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                if (!(player.openContainer instanceof ContainerAutoWorkbench) || player.openContainer.windowId != message.windowId) return;
                TileAutoWorkbench tile = ((ContainerAutoWorkbench) player.openContainer).tile();
                if (!tile.isUsableByPlayer(player)) return;
                int size = tile.size();
                if (message.index == -1 && message.stacks.size() == size) {
                    tile.setPatternBatch(message.stacks);
                } else if (message.index >= 0 && message.index < size && message.stacks.size() == 1) {
                    ItemStack ingredient = message.stacks.get(0);
                    if (!ingredient.isEmpty()) { ingredient = ingredient.copy(); ingredient.setCount(1); }
                    tile.setInventorySlotContents(size + message.index, ingredient);
                }
                
                
                
                player.openContainer.detectAndSendChanges();
                player.connection.sendPacket(new net.minecraft.network.play.server.SPacketWindowItems(
                        player.openContainer.windowId, player.openContainer.getInventory()));
            });
            return null;
        }
    }
}
