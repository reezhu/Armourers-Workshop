package riskyken.armourersWorkshop.common.items.paintingtool;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import riskyken.armourersWorkshop.ArmourersWorkshop;
import riskyken.armourersWorkshop.api.common.painting.IPantableBlock;
import riskyken.armourersWorkshop.common.blocks.ModBlocks;
import riskyken.armourersWorkshop.common.items.AbstractModItem;
import riskyken.armourersWorkshop.common.lib.LibGuiIds;
import riskyken.armourersWorkshop.common.lib.LibItemNames;
import riskyken.armourersWorkshop.common.painting.IBlockPainter;
import riskyken.armourersWorkshop.common.painting.tool.AbstractToolOption;
import riskyken.armourersWorkshop.common.painting.tool.IConfigurableTool;
import riskyken.armourersWorkshop.common.painting.tool.ToolOptions;
import riskyken.armourersWorkshop.common.tileentities.TileEntityArmourer;
import riskyken.armourersWorkshop.common.undo.UndoManager;
import riskyken.armourersWorkshop.utils.TranslateUtils;
import riskyken.armourersWorkshop.utils.UtilItems;

import java.util.ArrayList;
import java.util.List;

public class ItemBlendingTool extends AbstractModItem implements IConfigurableTool, IBlockPainter {

    public ItemBlendingTool() {
        super(LibItemNames.BLENDING_TOOL);
    }
    
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn,
                             BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
        
        IBlockState blockState = worldIn.getBlockState(pos);

        if (blockState.getBlock() instanceof IPantableBlock) {
            if (!worldIn.isRemote) {
                UndoManager.begin(playerIn);
                usedOnBlockSide(stack, playerIn, worldIn, pos, blockState.getBlock(), facing);
                UndoManager.end(playerIn);
                //worldIn.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, LibSounds.BURN, 1.0F, 1.0F);
            }
            return true;
        }
        
        if (blockState.getBlock() == ModBlocks.armourerBrain & playerIn.isSneaking()) {
            if (!worldIn.isRemote) {
                TileEntity te = worldIn.getTileEntity(pos);
                if (te != null && te instanceof TileEntityArmourer) {
                    ((TileEntityArmourer)te).toolUsedOnArmourer(this, worldIn, stack, playerIn);
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public void usedOnBlockSide(ItemStack stack, EntityPlayer player, World world, BlockPos pos, Block block, EnumFacing side) {
        int radius = (Integer) ToolOptions.RADIUS.readFromNBT(stack.getTagCompound());
        // TODO Auto-generated method stub
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
        super.addInformation(stack, player, list, p_77624_4_);
        int intensity = UtilItems.getIntensityFromStack(stack, 16);
        int radius = (Integer) ToolOptions.RADIUS.readFromNBT(stack.getTagCompound());
        
        list.add(TranslateUtils.translate("item.armourersworkshop:rollover.intensity", intensity));
        list.add(TranslateUtils.translate("item.armourersworkshop:rollover.radius", radius * 2 - 1 , radius * 2 - 1, 1));
        list.add(TranslateUtils.translate("item.armourersworkshop:rollover.openSettings"));
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (worldIn.isRemote & playerIn.isSneaking()) {
            playerIn.openGui(ArmourersWorkshop.instance, LibGuiIds.TOOL_OPTIONS, worldIn, 0, 0, 0);
        }
        return super.onItemRightClick(itemStackIn, worldIn, playerIn);
    }
    
    @Override
    public void getToolOptions(ArrayList<AbstractToolOption> toolOptionList) {
        toolOptionList.add(ToolOptions.INTENSITY);
        toolOptionList.add(ToolOptions.RADIUS);
    }
}
