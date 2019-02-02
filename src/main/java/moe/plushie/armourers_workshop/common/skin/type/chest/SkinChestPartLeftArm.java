package moe.plushie.armourers_workshop.common.skin.type.chest;

import java.awt.Point;

import org.lwjgl.opengl.GL11;

import moe.plushie.armourers_workshop.api.common.IPoint3D;
import moe.plushie.armourers_workshop.api.common.IRectangle3D;
import moe.plushie.armourers_workshop.api.common.skin.Point3D;
import moe.plushie.armourers_workshop.api.common.skin.Rectangle3D;
import moe.plushie.armourers_workshop.api.common.skin.type.ISkinPartTypeTextured;
import moe.plushie.armourers_workshop.api.common.skin.type.ISkinType;
import moe.plushie.armourers_workshop.client.model.armourer.ModelChest;
import moe.plushie.armourers_workshop.common.skin.data.SkinProperties;
import moe.plushie.armourers_workshop.common.skin.type.AbstractSkinPartTypeBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SkinChestPartLeftArm extends AbstractSkinPartTypeBase implements ISkinPartTypeTextured {
    
    public SkinChestPartLeftArm(ISkinType baseType) {
        super(baseType);
        this.buildingSpace = new Rectangle3D(-11, -16, -14, 14, 32, 28);
        this.guideSpace = new Rectangle3D(-3, -10, -2, 4, 12, 4);
        this.offset = new Point3D(10, -7, 0);
    }
    
    @Override
    public String getPartName() {
        return "leftArm";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderBuildingGuide(float scale, SkinProperties skinProps, boolean showHelper) {
        GL11.glTranslated(0, this.buildingSpace.getY() * scale, 0);
        GL11.glTranslated(0, -this.guideSpace.getY() * scale, 0);
        ModelChest.MODEL.renderLeftArm(scale);
        GL11.glTranslated(0, this.guideSpace.getY() * scale, 0);
        GL11.glTranslated(0, -this.buildingSpace.getY() * scale, 0);
    }

    @Override
    public Point getTextureSkinPos() {
        return new Point(40, 16);
    }

    @Override
    public boolean isTextureMirrored() {
        return true;
    }
    
    @Override
    public Point getTextureBasePos() {
        return new Point(32, 48);
    }
    
    @Override
    public Point getTextureOverlayPos() {
        return new Point(48, 48);
    }
    
    @Override
    public IPoint3D getTextureModelSize() {
        return new Point3D(4, 12, 4);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IPoint3D getItemRenderOffset() {
        return new Point3D(5, 2, 0);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IRectangle3D getItemRenderTextureBounds() {
        return new Rectangle3D(-8, 0, -2, 4, 12, 4);
    }
    
    @Override
    public boolean isModelOverridden(SkinProperties skinProps) {
        return SkinProperties.PROP_MODEL_OVERRIDE_ARM_LEFT.getValue(skinProps);
    }
    
    @Override
    public boolean isOverlayOverridden(SkinProperties skinProps) {
        return SkinProperties.PROP_MODEL_HIDE_OVERLAY_ARM_LEFT.getValue(skinProps);
    }
}
