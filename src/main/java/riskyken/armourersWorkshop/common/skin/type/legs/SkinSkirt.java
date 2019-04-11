package riskyken.armourersWorkshop.common.skin.type.legs;

import riskyken.armourersWorkshop.api.common.skin.type.ISkinPartType;
import riskyken.armourersWorkshop.common.skin.type.AbstractSkinTypeBase;

import java.util.ArrayList;

public class SkinSkirt extends AbstractSkinTypeBase {

    private ArrayList<ISkinPartType> skinParts;
    
    public SkinSkirt() {
        skinParts = new ArrayList<ISkinPartType>();
        skinParts.add(new SkinSkirtPartBase(this));
    }
    
    @Override
    public ArrayList<ISkinPartType> getSkinParts() {
        return this.skinParts;
    }

    @Override
    public String getRegistryName() {
        return "armourers:skirt";
    }
    
    @Override
    public String getName() {
        return "Skirt";
    }

    @Override
    public int getEntityEquipmentSlot() {
        return 2;
    }
}
