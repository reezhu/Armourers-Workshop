package riskyken.armourers_workshop.common.network.messages.server;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import riskyken.armourers_workshop.common.data.PlayerPointer;
import riskyken.armourers_workshop.common.skin.EquipmentWardrobeData;
import riskyken.armourers_workshop.proxies.ClientProxy;

/**
 * Sent from the server to a client when a player walks
 * into tracking range or updates their equipment wardrobe.
 * @author RiskyKen
 *
 */
public class MessageServerSkinWardrobeUpdate implements IMessage, IMessageHandler<MessageServerSkinWardrobeUpdate, IMessage> {

    PlayerPointer playerPointer;
    EquipmentWardrobeData equipmentWardrobeData;
    
    public MessageServerSkinWardrobeUpdate() {
        equipmentWardrobeData = new EquipmentWardrobeData();
    }

    public MessageServerSkinWardrobeUpdate(PlayerPointer playerPointer, EquipmentWardrobeData equipmentWardrobeData) {
        this.playerPointer = playerPointer;
        this.equipmentWardrobeData = equipmentWardrobeData;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.playerPointer = new PlayerPointer(buf);
        this.equipmentWardrobeData.fromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.playerPointer.writeToByteBuffer(buf);
        this.equipmentWardrobeData.toBytes(buf);
    }

    @Override
    public IMessage onMessage(MessageServerSkinWardrobeUpdate message, MessageContext ctx) {
        setEquipmentWardrobeData(message.playerPointer, message.equipmentWardrobeData);
        return null;
    }
    
    private void setEquipmentWardrobeData(PlayerPointer playerPointer, EquipmentWardrobeData ewd) {
        ClientProxy.equipmentWardrobeHandler.setEquipmentWardrobeData(playerPointer, ewd);
    }
}