package riskyken.armourersWorkshop.common.skin.type.wings;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinProperty;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinPartType;
import riskyken.armourersWorkshop.client.lib.LibItemResources;
import riskyken.armourersWorkshop.common.skin.data.SkinProperties;
import riskyken.armourersWorkshop.common.skin.type.AbstractSkinTypeBase;

public class SkinWings extends AbstractSkinTypeBase {
    
    private ArrayList<ISkinPartType> skinParts;
    
    public SkinWings() {
        skinParts = new ArrayList<ISkinPartType>();
        skinParts.add(new SkinWingsPartLeftWing(this));
        skinParts.add(new SkinWingsPartRightWing(this));
    }
    
    @Override
    public ArrayList<ISkinPartType> getSkinParts() {
        return this.skinParts;
    }

    @Override
    public String getRegistryName() {
        return "armourers:wings";
    }
    
    @Override
    public String getName() {
        return "wings";
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcon(IIconRegister register) {
        this.icon = register.registerIcon(LibItemResources.TEMPLATE_WINGS);
        this.emptySlotIcon = register.registerIcon(LibItemResources.SLOT_SKIN_WINGS);
    }
    
    @Override
    public ArrayList<ISkinProperty<?>> getProperties() {
        ArrayList<ISkinProperty<?>> properties = super.getProperties();
        properties.add(SkinProperties.PROP_WINGS_FLYING_SPEED);
        properties.add(SkinProperties.PROP_WINGS_IDLE_SPEED);
        properties.add(SkinProperties.PROP_WINGS_MAX_ANGLE);
        properties.add(SkinProperties.PROP_WINGS_MIN_ANGLE);
        properties.add(SkinProperties.PROP_WINGS_MOVMENT_TYPE);
        return properties;
    }
    
    public static enum MovementType {
        EASE,
        LINEAR
    }
}
