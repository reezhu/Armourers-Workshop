package riskyken.armourersWorkshop.common.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import riskyken.armourersWorkshop.common.lib.LibItemNames;
import riskyken.armourersWorkshop.common.skin.cubes.CubeRegistry;

public class ItemBlockMarker extends AbstractModItem {

    public ItemBlockMarker() {
        super(LibItemNames.BLOCK_MARKER);
    }
    
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn,
                             BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState blockState = worldIn.getBlockState(pos);
        if (CubeRegistry.INSTANCE.isBuildingBlock(blockState.getBlock())) {
            if (!worldIn.isRemote) {
                /*
                int meta = world.getBlockMetadata(blockLocation);
                int newMeta = side + 1;
                if (newMeta == meta) {
                    //This side is already marked.
                    world.setBlockMetaData(blockLocation, 0, 2);
                } else {
                    world.setBlockMetaData(blockLocation, newMeta, 2);
                }
                */
            }
            return true;
        }
        return false;
    }
}
