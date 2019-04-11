package riskyken.armourersWorkshop.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import riskyken.armourersWorkshop.ArmourersWorkshop;
import riskyken.armourersWorkshop.common.lib.LibGuiIds;
import riskyken.armourersWorkshop.common.lib.LibItemNames;

import java.util.ArrayList;

public class ItemDebugTool extends AbstractModItem {

    public ItemDebugTool() {
        super(LibItemNames.DEBUG_TOOL, false);
    }


    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (worldIn.isRemote) {
            playerIn.openGui(ArmourersWorkshop.instance, LibGuiIds.DEBUG_TOOL, worldIn, 0, 0, 0);
        }
        return itemStackIn;
    }
    
    public static interface IDebug {
        public void getDebugHoverText(World world, BlockPos pos, ArrayList<String> textLines);
    }
}
