package riskyken.armourersWorkshop.common.items;

import net.minecraft.item.Item;
import org.apache.logging.log4j.Level;
import riskyken.armourersWorkshop.common.addons.AddonBuildCraft;
import riskyken.armourersWorkshop.common.items.paintingtool.*;
import riskyken.armourersWorkshop.common.lib.LibItemNames;
import riskyken.armourersWorkshop.utils.ModLogger;

public class ModItems {
    
    public static Item equipmentSkinTemplate;
    public static Item equipmentSkin;
    
    public static Item paintbrush;
    public static Item paintRoller;
    public static Item colourPicker;
    public static Item burnTool;
    public static Item dodgeTool;
    public static Item colourNoiseTool;
    public static Item shadeNoiseTool;
    public static Item hueTool;
    public static Item blendingTool;
    public static Item blockMarker;
    
    public static Item mannequinTool;
    public static Item wandOfStyle;
    public static Item soap;
    public static Item dyeBottle;
    public static Item guideBook;
    public static Item armourersHammer;
    public static Item debugTool;
    
    public static Item armourContainerItem;
    public static Item[] armourContainer;
    
    
    public ModItems() {
        equipmentSkinTemplate = new ItemSkinTemplate();
        setEquipmentSkinType();
        
        //Tools
        paintbrush = new ItemPaintbrush();
        paintRoller = new ItemPaintRoller();
        colourPicker = new ItemColourPicker();
        burnTool = new ItemBurnTool();
        dodgeTool = new ItemDodgeTool();
        colourNoiseTool = new ItemColourNoiseTool();
        shadeNoiseTool = new ItemShadeNoiseTool();
        hueTool = new ItemHueTool();
        blockMarker = new ItemBlockMarker();
        //blendingTool = new ItemBlendingTool();
        
        mannequinTool = new ItemMannequinTool();
        wandOfStyle = new ItemWandOfStyle();
        soap = new ItemSoap();
        dyeBottle = new ItemDyeBottle();
        guideBook = new ItemGuideBook();
        armourersHammer = new ItemArmourersHammer();
        debugTool = new ItemDebugTool();
        
        armourContainerItem = new ItemArmourContainerItem();
        armourContainer = new Item[4];
        armourContainer[0] = new ItemArmourContainer(LibItemNames.ARMOUR_CONTAINER_HEAD, 0);
        armourContainer[1] = new ItemArmourContainer(LibItemNames.ARMOUR_CONTAINER_CHEST, 1);
        armourContainer[2] = new ItemArmourContainer(LibItemNames.ARMOUR_CONTAINER_LEGS, 2);
        armourContainer[3] = new ItemArmourContainer(LibItemNames.ARMOUR_CONTAINER_FEET, 3);
    }
    
    private void setEquipmentSkinType() {
        boolean skinTypeSet = true;
        
        if (AddonBuildCraft.isSkinCompatibleVersion()) {
            try {
                Class<?> c = Class.forName("riskyken.armourersWorkshop.common.items.ItemSkinRobotOverlay");
                Object classObject = c.newInstance();
                
                if (classObject instanceof ItemSkin) {
                    equipmentSkin = (ItemSkin)classObject;
                } else {
                    skinTypeSet = false;
                }
                
            } catch (Exception e) {
                ModLogger.log(Level.WARN, "Failed to load BuildCraft skinned item.");
                e.printStackTrace();
                skinTypeSet = false;
            }
        } else {
            skinTypeSet = false;
        }
        
        if (!skinTypeSet) {
            equipmentSkin = new ItemSkin();
        }
    }
}
