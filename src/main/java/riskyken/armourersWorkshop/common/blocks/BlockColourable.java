package riskyken.armourersWorkshop.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import riskyken.armourersWorkshop.api.common.painting.IPantable;
import riskyken.armourersWorkshop.api.common.painting.IPantableBlock;
import riskyken.armourersWorkshop.api.common.skin.cubes.ICubeColour;
import riskyken.armourersWorkshop.common.painting.PaintType;
import riskyken.armourersWorkshop.common.skin.cubes.CubeColour;
import riskyken.armourersWorkshop.common.tileentities.TileEntityColourable;

public class BlockColourable extends AbstractModBlockContainer implements IPantableBlock {
    
    public BlockColourable(String name, boolean glowing) {
        super(name);
        if (glowing) {
            setLightLevel(1.0F);
        }
        setHardness(1.0F);
        setLightOpacity(0);
    }
    
    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityColourable();
    }
    
    @Override
    public boolean setColour(IBlockAccess world, BlockPos pos, int colour, EnumFacing side) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null & te instanceof IPantable) {
            ((IPantable)te).setColour(colour, side);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean setColour(IBlockAccess world, BlockPos pos, byte[] rgb, EnumFacing side) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null & te instanceof IPantable) {
            ((IPantable)te).setColour(rgb, side);
            return true;
        }
        return false;
    }

    @Override
    public int getColour(IBlockAccess world, BlockPos pos, EnumFacing side) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null & te instanceof IPantable) {
            return ((IPantable)te).getColour(side);
        }
        return 0;
    }
    
    @Override
    public ICubeColour getColour(IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null & te instanceof IPantable) {
            return ((IPantable)te).getColour();
        }
        return new CubeColour();
    }
    
    @Override
    public void setPaintType(IBlockAccess world, BlockPos pos, PaintType paintType, EnumFacing side) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null & te instanceof IPantable) {
            ((IPantable)te).setPaintType(paintType, side);
        }
    }
    
    @Override
    public PaintType getPaintType(IBlockAccess world, BlockPos pos, EnumFacing side) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null & te instanceof IPantable) {
            return ((IPantable)te).getPaintType(side);
        }
        return PaintType.NORMAL;
    }
    
    @Override
    public boolean isRemoteOnly(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public int getRenderType() {
        return 3;
    }

//    @Override
//    public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
//        if (pos != null) {
//            TileEntity te = worldIn.getTileEntity(pos);
//            if (tintIndex >= 0 & tintIndex <= 5) {
//                if (te != null && te instanceof TileEntityColourable) {
//                    return ((TileEntityColourable)te).getColour(EnumFacing.values()[tintIndex]);
//                }
//            }
//
//        }
//        return 0xFFFFFFFF;
//    }


}
