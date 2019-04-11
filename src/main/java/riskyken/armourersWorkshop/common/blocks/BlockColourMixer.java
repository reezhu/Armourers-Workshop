package riskyken.armourersWorkshop.common.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import riskyken.armourersWorkshop.ArmourersWorkshop;
import riskyken.armourersWorkshop.common.lib.LibBlockNames;
import riskyken.armourersWorkshop.common.lib.LibGuiIds;
import riskyken.armourersWorkshop.common.tileentities.TileEntityColourMixer;
import riskyken.armourersWorkshop.utils.UtilBlocks;

public class BlockColourMixer extends AbstractModBlockContainer /*implements IBlockColor*/ {

    public BlockColourMixer() {
        super(LibBlockNames.COLOUR_MIXER);
    }
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        UtilBlocks.dropInventoryBlocks(worldIn, pos);
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!playerIn.canPlayerEdit(pos, side, playerIn.getHeldItem())) {
            return false;
        }
        if (!worldIn.isRemote) {
            FMLNetworkHandler.openGui(playerIn, ArmourersWorkshop.instance, LibGuiIds.COLOUR_MIXER, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileEntityColourMixer();
    }
    
    @Override
    public int getRenderType() {
        return 3;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }

//    @SideOnly(Side.CLIENT)
//    @Override
//    public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
//        if (pos != null) {
//            TileEntity te = worldIn.getTileEntity(pos);
//            if (te != null && te instanceof TileEntityColourMixer) {
//                return ((TileEntityColourMixer)te).getColour(null);
//            }
//        }
//        return 0xFFFFFFFF;
//    }
}
