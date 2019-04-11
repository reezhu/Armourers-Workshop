package riskyken.armourersWorkshop.common.blocks;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;

public class BlockColourableGlass extends BlockColourable {

    public BlockColourableGlass(String name, boolean glowing) {
        super(name, glowing);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }


    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (blockAccess.getBlockState(pos.offset(side)).getBlock() == this) {
            return false;
        }
        return super.shouldSideBeRendered(blockAccess, pos, side);
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }


}
