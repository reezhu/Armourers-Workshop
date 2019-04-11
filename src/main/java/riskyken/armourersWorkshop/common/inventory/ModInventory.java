package riskyken.armourersWorkshop.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants.NBT;

public class ModInventory implements IInventory {

    private static final String TAG_ITEMS = "items";
    private static final String TAG_SLOT = "slot";
    
    private final String name;
    private final ItemStack[] slots;
    private final IInventorySlotUpdate callback;
    private final TileEntity parent;
    
    public ModInventory(String name, int slotCount) {
        this(name, slotCount, null, null);
    }
    
    public ModInventory(String name, int slotCount, TileEntity parent) {
        this(name, slotCount, parent, null);
    }
    
    public ModInventory(String name, int slotCount, IInventorySlotUpdate callback) {
        this(name, slotCount, null, callback);
    }
    
    public ModInventory(String name, int slotCount, TileEntity parent, IInventorySlotUpdate callback) {
        this.name = name;
        this.slots = new ItemStack[slotCount];
        this.parent = parent;
        this.callback = callback;
    }
    
    @Override
    public int getSizeInventory() {
        return this.slots.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotId) {
        return this.slots[slotId];
    }

    @Override
    public ItemStack decrStackSize(int slotId, int count) {
        ItemStack itemstack = getStackInSlot(slotId);
        if (itemstack != null) {
            if (itemstack.stackSize <= count){
                setInventorySlotContents(slotId, null);
            }else{
                itemstack = itemstack.splitStack(count);
                setInventorySlotContents(slotId, getStackInSlot(slotId));
                markDirty();
            }
        }
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = getStackInSlot(index);
        setInventorySlotContents(index, stack);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack stack) {
        this.slots[slotId] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
        markDirty();
        if (callback != null) {
            callback.setInventorySlotContents(this, slotId, stack);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentText(getName());
    }
    
    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        if (parent != null) {
            parent.markDirty();
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return !player.isDead;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int slotId, ItemStack stack) {
        return true;
    }
    
    public void saveItemsToNBT(NBTTagCompound compound) {
        NBTTagList items = new NBTTagList();
        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack stack = getStackInSlot(i);
            if (stack != null) {
                NBTTagCompound item = new NBTTagCompound();
                item.setByte(TAG_SLOT, (byte)i);
                stack.writeToNBT(item);
                items.appendTag(item);
            }
        }
        compound.setTag(this.name + ":" + TAG_ITEMS, items);
    }
    
    public void loadItemsFromNBT(NBTTagCompound compound) {
        NBTTagList items = compound.getTagList(this.name + ":" + TAG_ITEMS, NBT.TAG_COMPOUND);
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound item = (NBTTagCompound)items.getCompoundTagAt(i);
            int slot = item.getByte(TAG_SLOT);
            if (slot >= 0 && slot < getSizeInventory()) {
                slots[slot] = ItemStack.loadItemStackFromNBT(item);
            }
        }
    }
    
    @Override
    public int getField(int id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getFieldCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        
    }
}
