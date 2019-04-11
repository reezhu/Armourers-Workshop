package riskyken.armourersWorkshop.client.render.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinDye;
import riskyken.armourersWorkshop.client.model.skin.ModelSkinWings;
import riskyken.armourersWorkshop.client.render.SkinModelRenderer;
import riskyken.armourersWorkshop.common.capability.IWardrobeCapability;
import riskyken.armourersWorkshop.common.skin.EquipmentWardrobeData;
import riskyken.armourersWorkshop.common.skin.data.Skin;
import riskyken.armourersWorkshop.common.skin.type.SkinTypeRegistry;
import riskyken.armourersWorkshop.proxies.ClientProxy;
import riskyken.armourersWorkshop.proxies.ClientProxy.SkinRenderType;

import java.awt.*;

public class LayerSkin implements LayerRenderer<EntityLivingBase>{

    protected final RendererLivingEntity<?> livingEntityRenderer;
    public final ModelSkinWings customWings = new ModelSkinWings();
    
    @CapabilityInject(IWardrobeCapability.class)
    private static final Capability<IWardrobeCapability> WARDROBE_CAP = null;

    public LayerSkin(RendererLivingEntity<?> livingEntityRendererIn) {
        this.livingEntityRenderer = livingEntityRendererIn;
    }
    
    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount,
            float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        
        if (ClientProxy.getSkinRenderType() != SkinRenderType.RENDER_LAYER) {
            return;
        }
        
        IWardrobeCapability wardrobe = entitylivingbaseIn.getCapability(WARDROBE_CAP, null);
        if (wardrobe == null) {
            return;
        }
        EquipmentWardrobeData ewd = wardrobe.getEquipmentWardrobeData();
        byte[] extraColours = null;
        if (ewd != null) {
            Color skinColour = new Color(ewd.skinColour);
            Color hairColour = new Color(ewd.hairColour);
            extraColours = new byte[] {
                    (byte)skinColour.getRed(), (byte)skinColour.getGreen(), (byte)skinColour.getBlue(),
                    (byte)hairColour.getRed(), (byte)hairColour.getGreen(), (byte)hairColour.getBlue()};
        }
        
        double distance = Minecraft.getMinecraft().thePlayer.getDistance(
                entitylivingbaseIn.posX,
                entitylivingbaseIn.posY,
                entitylivingbaseIn.posZ);
        
        
        
        for (int slot = 0; slot < 4; slot++) {

            for (int skinIndex = 0; skinIndex < 5; skinIndex++) {
                if (slot == SkinTypeRegistry.skinHead.getEntityEquipmentSlot()) {
                    Skin data = SkinModelRenderer.INSTANCE.getPlayerCustomArmour(entitylivingbaseIn, SkinTypeRegistry.skinHead, skinIndex);
                    ISkinDye dye = SkinModelRenderer.INSTANCE.getPlayerDyeData(entitylivingbaseIn, SkinTypeRegistry.skinHead, skinIndex);
                    if (data != null) {
                        SkinModelRenderer.INSTANCE.customHead.render(entitylivingbaseIn, (ModelBiped)livingEntityRenderer.getMainModel(), data, false, dye, extraColours, false, distance, true);
                    }
                }
                if (slot == SkinTypeRegistry.skinChest.getEntityEquipmentSlot()) {
                    Skin data = SkinModelRenderer.INSTANCE.getPlayerCustomArmour(entitylivingbaseIn, SkinTypeRegistry.skinChest, skinIndex);
                    ISkinDye dye = SkinModelRenderer.INSTANCE.getPlayerDyeData(entitylivingbaseIn, SkinTypeRegistry.skinChest, skinIndex);
                    if (data != null) {
                        SkinModelRenderer.INSTANCE.customChest.render(entitylivingbaseIn, (ModelBiped)livingEntityRenderer.getMainModel(), data, false, dye, extraColours, false, distance, true);
                    }
                }
                if (slot == SkinTypeRegistry.skinLegs.getEntityEquipmentSlot()) {
                    Skin data = SkinModelRenderer.INSTANCE.getPlayerCustomArmour(entitylivingbaseIn, SkinTypeRegistry.skinLegs, skinIndex);
                    ISkinDye dye = SkinModelRenderer.INSTANCE.getPlayerDyeData(entitylivingbaseIn, SkinTypeRegistry.skinLegs, skinIndex);
                    if (data != null) {
                        SkinModelRenderer.INSTANCE.customLegs.render(entitylivingbaseIn, (ModelBiped)livingEntityRenderer.getMainModel(), data, false, dye, extraColours, false, distance, true);
                    }
                }
                if (slot == SkinTypeRegistry.skinSkirt.getEntityEquipmentSlot()) {
                    Skin data = SkinModelRenderer.INSTANCE.getPlayerCustomArmour(entitylivingbaseIn, SkinTypeRegistry.skinSkirt, skinIndex);
                    ISkinDye dye = SkinModelRenderer.INSTANCE.getPlayerDyeData(entitylivingbaseIn, SkinTypeRegistry.skinSkirt, skinIndex);
                    if (data != null) {
                        SkinModelRenderer.INSTANCE.customSkirt.render(entitylivingbaseIn, (ModelBiped)livingEntityRenderer.getMainModel(), data, false, dye, extraColours, false, distance, true);
                    }
                }
                if (slot == SkinTypeRegistry.skinFeet.getEntityEquipmentSlot()) {
                    Skin data = SkinModelRenderer.INSTANCE.getPlayerCustomArmour(entitylivingbaseIn, SkinTypeRegistry.skinFeet, skinIndex);
                    ISkinDye dye = SkinModelRenderer.INSTANCE.getPlayerDyeData(entitylivingbaseIn, SkinTypeRegistry.skinFeet, skinIndex);
                    if (data != null) {
                        SkinModelRenderer.INSTANCE.customFeet.render(entitylivingbaseIn, (ModelBiped)livingEntityRenderer.getMainModel(), data, false, dye, extraColours, false, distance, true);
                    }
                }
            }

        }
        
        
        
        
        Skin data = SkinModelRenderer.INSTANCE.getPlayerCustomArmour(entitylivingbaseIn, SkinTypeRegistry.skinWings, 0);
        ISkinDye dye = SkinModelRenderer.INSTANCE.getPlayerDyeData(entitylivingbaseIn, SkinTypeRegistry.skinWings, 0);
        if (data != null) {
            customWings.render(entitylivingbaseIn, (ModelBiped) livingEntityRenderer.getMainModel(), data, false, dye, extraColours, false, distance, true);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
