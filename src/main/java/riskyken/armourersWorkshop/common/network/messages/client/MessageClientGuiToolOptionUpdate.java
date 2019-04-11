package riskyken.armourersWorkshop.common.network.messages.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import riskyken.armourersWorkshop.common.painting.tool.IConfigurableTool;

import java.util.Iterator;
import java.util.Set;

public class MessageClientGuiToolOptionUpdate implements IMessage, IMessageHandler<MessageClientGuiToolOptionUpdate, IMessage> {

    private NBTTagCompound toolOptions;
    
    public MessageClientGuiToolOptionUpdate() { }
    
    public MessageClientGuiToolOptionUpdate(NBTTagCompound toolOptions) {
        this.toolOptions = toolOptions;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        this.toolOptions = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.toolOptions);
    }
    
    @Override
    public IMessage onMessage(MessageClientGuiToolOptionUpdate message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        if (player != null) {
            ItemStack stack = player.getHeldItem();
            Item item = stack.getItem();
            
            if (item instanceof IConfigurableTool) {
                NBTTagCompound newOptions = message.toolOptions;
                if (!stack.hasTagCompound()) {
                    stack.setTagCompound(new NBTTagCompound());
                }
                NBTTagCompound stackCompound = stack.getTagCompound();
                Set keySet = newOptions.getKeySet();
                
                Iterator iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    if (stackCompound.hasKey(key)) {
                        stackCompound.removeTag(key);
                    }
                    stackCompound.setTag(key, newOptions.getTag(key));
                }
            }
        }
        return null;
    }
}
