package moe.plushie.armourers_workshop.common.crafting.recipe;

import moe.plushie.armourers_workshop.common.items.ModItems;
import moe.plushie.armourers_workshop.utils.SkinNBTHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipeSkinClear extends RecipeItemSkinning {

    public RecipeSkinClear() {
        super(null);
    }

    @Override
    public boolean matches(IInventory inventory) {
        return !getCraftingResult(inventory).isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(IInventory inventory) {
        ItemStack skinItemStack = ItemStack.EMPTY;
        ItemStack soapStack = ItemStack.EMPTY;
        
        for (int slotId = 0; slotId < inventory.getSizeInventory(); slotId++) {
            ItemStack stack = inventory.getStackInSlot(slotId);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                
                
                if (item != ModItems.skin && SkinNBTHelper.stackHasSkinData(stack) && SkinNBTHelper.getSkinDescriptorFromStack(stack).lockSkin) {
                    if (!skinItemStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    skinItemStack = stack;
                } else if (item == ModItems.soap) {
                    if (!soapStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    soapStack = stack;
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }
        
        if (!skinItemStack.isEmpty() && !soapStack.isEmpty()) {
            ItemStack returnStack = skinItemStack.copy();
            SkinNBTHelper.removeSkinDataFromStack(returnStack, true);
            return returnStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void onCraft(IInventory inventory) {
        for (int slotId = 0; slotId < inventory.getSizeInventory(); slotId++) {
            ItemStack stack = inventory.getStackInSlot(slotId);
            if (stack.getItem() != ModItems.soap) {
                inventory.decrStackSize(slotId, 1);
            }
        }
    }
}
