package riskyken.armourersWorkshop.common.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ModTileEntity extends TileEntity {
    
    public void syncWithClients() {
        if (!worldObj.isRemote) {
            syncWithNearbyPlayers(this);
        }
    }
    
    public static void syncWithNearbyPlayers(TileEntity tileEntity) {
        World world = tileEntity.getWorld();
        List<EntityPlayer> players = world.playerEntities;
        for (EntityPlayer player : players) {
            if (player instanceof EntityPlayerMP) {
                EntityPlayerMP mp = (EntityPlayerMP)player;
                if (tileEntity.getDistanceSq(mp.posX, mp.posY, mp.posZ) < 64) {
                    if (tileEntity instanceof ModTileEntity)
                        mp.playerNetServerHandler.sendPacket(((ModTileEntity) tileEntity).getUpdatePacket());
                }
            }
        }
    }

    @Nullable
    public S35PacketUpdateTileEntity getUpdatePacket() {
        return null;
    }

    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = new NBTTagCompound();
        this.writeToNBT(compound);
        return compound;
    }
}
