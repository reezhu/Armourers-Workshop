package riskyken.armourersWorkshop.common.blocks;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import riskyken.armourersWorkshop.ArmourersWorkshop;
import riskyken.armourersWorkshop.common.config.ConfigHandler;
import riskyken.armourersWorkshop.common.items.ModItems;
import riskyken.armourersWorkshop.common.lib.LibBlockNames;
import riskyken.armourersWorkshop.common.lib.LibGuiIds;
import riskyken.armourersWorkshop.common.tileentities.TileEntityMannequin;
import riskyken.armourersWorkshop.utils.HolidayHelper;

import java.util.Random;

public class BlockDoll extends AbstractModBlockContainer {
    
    private static final AxisAlignedBB DOLL_AABB = new AxisAlignedBB(0.2F, 0F, 0.2F, 0.8F, 0.95F, 0.8F);
    private static final String TAG_OWNER = "owner";
    private final boolean isValentins;
    
    public BlockDoll() {
        super(LibBlockNames.DOLL, Material.sand, soundTypeMetal, !ConfigHandler.hideDollFromCreativeTabs);
        setLightOpacity(0);
        isValentins = HolidayHelper.valentins.isHolidayActive();
    }

//    @Override
//    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
//        return DOLL_AABB;
//    }




    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te != null && te instanceof TileEntityMannequin) {
            int l = MathHelper.floor_double((double)(placer.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
            ((TileEntityMannequin)te).setRotation(l);
            
            if (stack.hasTagCompound()) {
                NBTTagCompound compound = stack.getTagCompound();
                GameProfile gameProfile = null;
                if (compound.hasKey(TAG_OWNER, 10)) {
                    gameProfile = NBTUtil.readGameProfileFromNBT(compound.getCompoundTag(TAG_OWNER));
                    ((TileEntityMannequin)te).setGameProfile(gameProfile);
                }
            }
            
        }
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState stateIn, Random rand) {
        if (isValentins) {
            if (rand.nextFloat() * 100 > 80) {
                //world.spawnParticle("heart", x + 0.2D + rand.nextFloat() * 0.6F, y + 1D, z + 0.2D + rand.nextFloat() * 0.6F, 0, 0, 0);
            }
        }
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityMannequin) {
            TileEntityMannequin te = (TileEntityMannequin) tileEntity;
            if (te.isRenderExtras()) {
                if (te.hasSpecialRender()) {
                    /*
                    EntityFX entityfx = new EntitySpellParticleFX(world,  x + random.nextFloat() * 1F, y, z + random.nextFloat() * 1F, 0, 0, 0);
                    ((EntitySpellParticleFX)entityfx).setBaseSpellTextureIndex(144);
                    float[] colour = te.getSpecialRenderColour();
                    entityfx.setRBGColorF(colour[0], colour[1], colour[2]);
                    Minecraft.getMinecraft().effectRenderer.addEffect(entityfx);
                    */
                }
            }
        }
    }
    
    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        if (world.isRemote) {
            return false;
        }
        
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof TileEntityMannequin) {
            int rotation = ((TileEntityMannequin)te).getRotation();
            rotation++;
            if (rotation > 15) {
                rotation = 0;
            }
            ((TileEntityMannequin)te).setRotation(rotation);
        }
        return true;
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
        ItemStack stack = new ItemStack(ModBlocks.doll, 1);
        TileEntity te = world.getTileEntity(pos);;
        if (te != null && te instanceof TileEntityMannequin) {
            TileEntityMannequin teMan = (TileEntityMannequin) te;
            if (teMan.getGameProfile() != null) {
                NBTTagCompound profileTag = new NBTTagCompound();
                NBTUtil.writeGameProfile(profileTag, teMan.getGameProfile());
                stack.setTagCompound(new NBTTagCompound());
                stack.getTagCompound().setTag(TAG_OWNER, profileTag);
            }
        }
        return stack;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer manager) {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer manager) {
        return true;
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = playerIn.getHeldItem();
        if (!playerIn.canPlayerEdit(pos, side, heldItem)) {
            return false;
        }
        if (!worldIn.isRemote) {
            if (heldItem != null && heldItem.getItem() == ModItems.mannequinTool) {
                return false;
            }
            if (heldItem != null && heldItem.getItem() == Items.name_tag) {
                TileEntity te = worldIn.getTileEntity(pos);
                if (te != null && te instanceof TileEntityMannequin) {
                    if (heldItem.getItem() == Items.name_tag) {
                        ((TileEntityMannequin)te).setOwner(heldItem);
                    }
                }
            } else {
                FMLNetworkHandler.openGui(playerIn, ArmourersWorkshop.instance, LibGuiIds.MANNEQUIN,
                        worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        if (heldItem != null && heldItem.getItem() == ModItems.mannequinTool) {
            return false;
        }
        return true;
    }
    
    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return 0;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityMannequin(true);
    }

    @Override
    public int getRenderType() {
        return -1;
    }

}
