package riskyken.armourersWorkshop.common.undo;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import riskyken.armourersWorkshop.common.lib.LibModInfo;

import java.util.HashMap;

/**
 *
 * @author RiskyKen
 *
 */
public final class UndoManager {

    public static int maxUndos = 50;
    
    private static HashMap<String, PlayerUndoData> playerUndoData = new HashMap<String, PlayerUndoData>();

    public static void begin(EntityPlayer player) {
        if (!playerUndoData.containsKey(player.getName())) {
            playerUndoData.put(player.getName(), new PlayerUndoData(player));
        }
        PlayerUndoData playerData = playerUndoData.get(player.getName());
        playerData.begin();
    }
    
    public static void end(EntityPlayer player) {
        if (!playerUndoData.containsKey(player.getName())) {
            playerUndoData.put(player.getName(), new PlayerUndoData(player));
        }
        PlayerUndoData playerData = playerUndoData.get(player.getName());
        playerData.end();
    }
    
    @Deprecated()
    public static void blockPainted(EntityPlayer player, World world, BlockPos pos, int oldColour, byte oldPaintType, EnumFacing side) {
        byte[] oldrgb = new byte[3];
        oldrgb[0] = (byte) ((oldColour >> 16) & 0xFF);
        oldrgb[1] = (byte) ((oldColour >> 8) & 0xFF);
        oldrgb[2] = (byte) ((oldColour) & 0xFF);
        blockPainted(player, world, pos, oldrgb, oldPaintType, side);
    }
    
    public static void blockPainted(EntityPlayer player, World world, BlockPos pos, byte[] oldrgb, byte oldPaintType, EnumFacing side) {
        UndoData undoData = new UndoData(pos, world.provider.getDimensionId(), oldrgb, oldPaintType, side);
        if (!playerUndoData.containsKey(player.getName())) {
            playerUndoData.put(player.getName(), new PlayerUndoData(player));
        }
        
        PlayerUndoData playerData = playerUndoData.get(player.getName());
        playerData.addUndoData(undoData);
    }
    
    public static void undoPressed(EntityPlayer player) {
        String key = player.getName();
        if (!playerUndoData.containsKey(key)) {
            String outOfUndosText = I18n.format("chat." + LibModInfo.ID.toLowerCase() + ":undo.outOfUndos");
            player.addChatMessage(new ChatComponentText(outOfUndosText));
            return;
        }
        PlayerUndoData playerData = playerUndoData.get(key);
        World world = player.worldObj;
        String undoText = I18n.format("chat." + LibModInfo.ID.toLowerCase() + ":undo.undoing");
        player.addChatMessage(new ChatComponentText(undoText));
        playerData.playerPressedUndo(world);
        if (playerData.getAvalableUndos() < 1) {
            playerUndoData.remove(key);
        }
    }
}
