package riskyken.armourersWorkshop.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import riskyken.armourersWorkshop.api.common.skin.IEntityEquipment;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinDye;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinPointer;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinType;
import riskyken.armourersWorkshop.client.model.ModelRendererAttachment;
import riskyken.armourersWorkshop.client.model.skin.*;
import riskyken.armourersWorkshop.client.skin.ClientSkinCache;
import riskyken.armourersWorkshop.common.capability.IWardrobeCapability;
import riskyken.armourersWorkshop.common.config.ConfigHandlerClient;
import riskyken.armourersWorkshop.common.data.PlayerPointer;
import riskyken.armourersWorkshop.common.skin.EntityEquipmentData;
import riskyken.armourersWorkshop.common.skin.EquipmentWardrobeData;
import riskyken.armourersWorkshop.common.skin.data.Skin;
import riskyken.armourersWorkshop.common.skin.data.SkinPointer;
import riskyken.armourersWorkshop.common.skin.type.SkinTypeRegistry;
import riskyken.armourersWorkshop.proxies.ClientProxy;
import riskyken.armourersWorkshop.proxies.ClientProxy.SkinRenderType;
import riskyken.armourersWorkshop.utils.ModLogger;
import riskyken.armourersWorkshop.utils.SkinNBTHelper;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Helps render custom equipment on the player and other entities.
 *
 * TODO Clean up this class it's a mess >:|
 *
 * @author RiskyKen
 *
 */
@SideOnly(Side.CLIENT)
public final class SkinModelRenderer {
    
    public static SkinModelRenderer INSTANCE;
    
    public static void init() {
        INSTANCE = new SkinModelRenderer();
    }
    
    private final HashMap<PlayerPointer, EntityEquipmentData> playerEquipmentMap;
    private final Set<ModelBiped> attachedBipedSet;
    
    public final ModelSkinChest customChest = new ModelSkinChest();
    public final ModelSkinHead customHead = new ModelSkinHead();
    public final ModelSkinLegs customLegs = new ModelSkinLegs();
    public final ModelSkinSkirt customSkirt = new ModelSkinSkirt();
    public final ModelSkinFeet customFeet = new ModelSkinFeet();
    public final ModelSkinSword customSword = new ModelSkinSword();
    public final ModelSkinBow customBow = new ModelSkinBow();
    public final ModelSkinWings customWings = new ModelSkinWings();
    
    public EntityPlayer targetPlayer = null;
    
    @CapabilityInject(IWardrobeCapability.class)
    private static final Capability<IWardrobeCapability> WARDROBE_CAP = null;
    
    private SkinModelRenderer() {
        MinecraftForge.EVENT_BUS.register(this);
        playerEquipmentMap = new HashMap<PlayerPointer, EntityEquipmentData>();
        attachedBipedSet = Collections.newSetFromMap(new WeakHashMap<ModelBiped, Boolean>());
    }
    
    public Skin getPlayerCustomArmour(Entity entity, ISkinType skinType, int slotIndex) {
        if (!(entity instanceof AbstractClientPlayer)) { return null; }
        AbstractClientPlayer player = (AbstractClientPlayer) entity;
        
        EntityEquipmentData equipmentData = playerEquipmentMap.get(new PlayerPointer(player));
        
        //Look for skinned armourer.
        if (skinType.getEntityEquipmentSlot() >= 0) {
            int slot = skinType.getEntityEquipmentSlot();
            ItemStack armourStack = player.inventory.armorItemInSlot(slot);
            if (SkinNBTHelper.stackHasSkinData(armourStack)) {
                SkinPointer sp = SkinNBTHelper.getSkinPointerFromStack(armourStack);
                return getCustomArmourItemData(sp);
            }
        }
        
        //No skinned armour found checking the equipment wardrobe.
        if (equipmentData == null) {
            return null;
        }
        
        if (!equipmentData.haveEquipment(skinType, slotIndex)) {
            return null;
        }
        
        ISkinPointer skinPointer = equipmentData.getSkinPointer(skinType, slotIndex);
        return getCustomArmourItemData(skinPointer);
    }
    
    public ISkinDye getPlayerDyeData(Entity entity, ISkinType skinType, int slotIndex) {
        if (!(entity instanceof AbstractClientPlayer)) {
            return null;
        }
        AbstractClientPlayer player = (AbstractClientPlayer) entity;
        
        EntityEquipmentData equipmentData = playerEquipmentMap.get(new PlayerPointer(player));
        
        //Look for skinned armourer.
        if (skinType.getEntityEquipmentSlot() >= 0) {
            int slot = skinType.getEntityEquipmentSlot();
            ItemStack armourStack = player.inventory.armorItemInSlot(slot);
            if (SkinNBTHelper.stackHasSkinData(armourStack)) {
                SkinPointer sp = SkinNBTHelper.getSkinPointerFromStack(armourStack);
                return sp.getSkinDye();
            }
        }
        
        //No skinned armour found checking the equipment wardrobe.
        if (equipmentData == null) {
            return null;
        }
        
        if (!equipmentData.haveEquipment(skinType, slotIndex)) {
            return null;
        }
        
        ISkinDye skinDye = equipmentData.getSkinDye(skinType, slotIndex);
        return skinDye;
    }
    
    public byte[] getPlayerExtraColours(Entity entity) {
        if (!(entity instanceof AbstractClientPlayer)) {
            return null;
        }
        AbstractClientPlayer player = (AbstractClientPlayer) entity;
        
        EntityEquipmentData equipmentData = playerEquipmentMap.get(new PlayerPointer(player));
        
        return null;
    }
    
    public IEntityEquipment getPlayerCustomEquipmentData(Entity entity) {
        if (!(entity instanceof AbstractClientPlayer)) { return null; }
        AbstractClientPlayer player = (AbstractClientPlayer) entity;
        
        EntityEquipmentData equipmentData = playerEquipmentMap.get(new PlayerPointer(player));
        
        return equipmentData;
    }
    
    public int getSkinDataMapSize() {
        return playerEquipmentMap.size();
    }
    
    public Skin getCustomArmourItemData(ISkinPointer skinPointer) {
        return ClientSkinCache.INSTANCE.getSkin(skinPointer);
    }
    
    public void addEquipmentData(PlayerPointer playerPointer, EntityEquipmentData equipmentData) {
        if (playerEquipmentMap.containsKey(playerPointer)) {
            playerEquipmentMap.remove(playerPointer);
        }
        playerEquipmentMap.put(playerPointer, equipmentData);
    }
    
    public void removeEquipmentData(PlayerPointer playerPointer) {
        if (playerEquipmentMap.containsKey(playerPointer)) {
            playerEquipmentMap.remove(playerPointer);
        }
    }
    
    private boolean isPlayerWearingSkirt(PlayerPointer playerPointer) {
        if (!playerEquipmentMap.containsKey(playerPointer)) {
            return false;
        }
        EntityEquipmentData equipmentData = playerEquipmentMap.get(playerPointer);
        for (int i = 0; i < equipmentData.getNumberOfSlots(); i++) {
            if (!equipmentData.haveEquipment(SkinTypeRegistry.skinLegs, i)) {
                return false;
            } else {
                ISkinPointer skinPointer = equipmentData.getSkinPointer(SkinTypeRegistry.skinLegs, i);
                Skin skin = ClientSkinCache.INSTANCE.getSkin(skinPointer);
                if (skin != null) {
                    for (int j = 0; j < skin.getPartCount(); j++) {
                        if (skin.getParts().get(j).getPartType().getPartName().equals("skirt")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    @SubscribeEvent
    public void onRender(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.entityPlayer;
        targetPlayer = player;
        
        if (ClientProxy.getSkinRenderType() == SkinRenderType.MODEL_ATTACHMENT) {
            attachModelsToBiped(event.renderer.getMainModel(), event.renderer);
        }
        
        if (player.getGameProfile() == null) {
            return;
        }
        PlayerPointer playerPointer = new PlayerPointer(player);
        
        //Limit the players limbs if they have a skirt equipped.
        //A proper lady should not swing her legs around!
        if (isPlayerWearingSkirt(playerPointer)) {
            EquipmentWardrobeData ewd = ClientProxy.equipmentWardrobeHandler.getEquipmentWardrobeData(playerPointer);
            if (ewd != null && ewd.limitLimbs) {
                if (player.limbSwingAmount > 0.25F) {
                    player.limbSwingAmount = 0.25F;
                    player.prevLimbSwingAmount = 0.25F;
                } 
            }
        }
    }
    
    private void attachModelsToBiped(ModelBiped modelBiped, RenderPlayer renderPlayer) {
        if (attachedBipedSet.contains(modelBiped)) {
            return;
        }
        attachedBipedSet.add(modelBiped);
        modelBiped.bipedHead.addChild(new ModelRendererAttachment(modelBiped, SkinTypeRegistry.skinHead, SkinTypeRegistry.INSTANCE.getSkinPartFromRegistryName("armourers:head.base")));
        modelBiped.bipedBody.addChild(new ModelRendererAttachment(modelBiped, SkinTypeRegistry.skinChest, SkinTypeRegistry.INSTANCE.getSkinPartFromRegistryName("armourers:chest.base")));
        modelBiped.bipedLeftArm.addChild(new ModelRendererAttachment(modelBiped, SkinTypeRegistry.skinChest, SkinTypeRegistry.INSTANCE.getSkinPartFromRegistryName("armourers:chest.leftArm")));
        modelBiped.bipedRightArm.addChild(new ModelRendererAttachment(modelBiped, SkinTypeRegistry.skinChest, SkinTypeRegistry.INSTANCE.getSkinPartFromRegistryName("armourers:chest.rightArm")));
        modelBiped.bipedLeftLeg.addChild(new ModelRendererAttachment(modelBiped, SkinTypeRegistry.skinLegs, SkinTypeRegistry.INSTANCE.getSkinPartFromRegistryName("armourers:legs.leftLeg")));
        modelBiped.bipedRightLeg.addChild(new ModelRendererAttachment(modelBiped, SkinTypeRegistry.skinLegs, SkinTypeRegistry.INSTANCE.getSkinPartFromRegistryName("armourers:legs.rightLeg")));
        modelBiped.bipedBody.addChild(new ModelRendererAttachment(modelBiped, SkinTypeRegistry.skinLegs, SkinTypeRegistry.INSTANCE.getSkinPartFromRegistryName("armourers:legs.skirt")));
        modelBiped.bipedLeftLeg.addChild(new ModelRendererAttachment(modelBiped, SkinTypeRegistry.skinFeet, SkinTypeRegistry.INSTANCE.getSkinPartFromRegistryName("armourers:feet.leftFoot")));
        modelBiped.bipedRightLeg.addChild(new ModelRendererAttachment(modelBiped, SkinTypeRegistry.skinFeet, SkinTypeRegistry.INSTANCE.getSkinPartFromRegistryName("armourers:feet.rightFoot")));  
        modelBiped.bipedBody.addChild(new ModelRendererAttachment(modelBiped, SkinTypeRegistry.skinWings, SkinTypeRegistry.INSTANCE.getSkinPartFromRegistryName("armourers:wings.leftWing")));
        modelBiped.bipedBody.addChild(new ModelRendererAttachment(modelBiped, SkinTypeRegistry.skinWings, SkinTypeRegistry.INSTANCE.getSkinPartFromRegistryName("armourers:wings.rightWing")));
        ModLogger.log(String.format("Added model render attachment to %s", modelBiped.toString()));
        ModLogger.log(String.format("Using player renderer %s", renderPlayer.toString()));
    }
    
    @SubscribeEvent
    public void onRender(RenderPlayerEvent.Post event) {
    	targetPlayer = null;
    }
    
    @SubscribeEvent
    public void onRenderSpecialsPost(RenderPlayerEvent.Post event) {
        if (ClientProxy.getSkinRenderType() != SkinRenderType.RENDER_EVENT) {
            return;
        }
        EntityPlayer player = event.entityPlayer;
        RenderPlayer render = event.renderer;
        if (player.getGameProfile() == null) {
            return;
        }
        PlayerPointer playerPointer = new PlayerPointer(player);
        if (!playerEquipmentMap.containsKey(playerPointer)) {
            return;
        }
        
        double distance = Minecraft.getMinecraft().thePlayer.getDistance(
                player.posX,
                player.posY,
                player.posZ);
        
        if (distance > ConfigHandlerClient.maxSkinRenderDistance) {
            return;
        }
        
        IWardrobeCapability wardrobe = player.getCapability(WARDROBE_CAP, null);
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
        
        GlStateManager.pushMatrix();
        GlStateManager.enableCull();
        GlStateManager.enableBlend();
//        GlStateManager.enableBlendProfile(Profile.TRANSPARENT_MODEL);
        GL11.glTranslated(0, 22 * 0.0625F, 0);
        GL11.glScalef(1F, -1F, -1F);
        GL11.glRotatef((player.renderYawOffset - player.prevRenderYawOffset) * event.partialRenderTick + player.renderYawOffset, 0, 1, 0);
        
        for (int slot = 0; slot < 4; slot++) {

            for (int skinIndex = 0; skinIndex < 5; skinIndex++) {
                if (slot == SkinTypeRegistry.skinHead.getEntityEquipmentSlot()) {
                    Skin data = getPlayerCustomArmour(player, SkinTypeRegistry.skinHead, skinIndex);
                    ISkinDye dye = getPlayerDyeData(player, SkinTypeRegistry.skinHead, skinIndex);
                    if (data != null) {
                        customHead.render(player, render.getMainModel(), data, false, dye, extraColours, false, distance, true);
                    }
                }
                if (slot == SkinTypeRegistry.skinChest.getEntityEquipmentSlot()) {
                    Skin data = getPlayerCustomArmour(player, SkinTypeRegistry.skinChest, skinIndex);
                    ISkinDye dye = getPlayerDyeData(player, SkinTypeRegistry.skinChest, skinIndex);
                    if (data != null) {
                        customChest.render(player, render.getMainModel(), data, false, dye, extraColours, false, distance, true);
                    }
                }
                if (slot == SkinTypeRegistry.skinLegs.getEntityEquipmentSlot()) {
                    Skin data = getPlayerCustomArmour(player, SkinTypeRegistry.skinLegs, skinIndex);
                    ISkinDye dye = getPlayerDyeData(player, SkinTypeRegistry.skinLegs, skinIndex);
                    if (data != null) {
                        customLegs.render(player, render.getMainModel(), data, false, dye, extraColours, false, distance, true);
                    }
                }
                if (slot == SkinTypeRegistry.skinSkirt.getEntityEquipmentSlot()) {
                    Skin data = getPlayerCustomArmour(player, SkinTypeRegistry.skinSkirt, skinIndex);
                    ISkinDye dye = getPlayerDyeData(player, SkinTypeRegistry.skinSkirt, skinIndex);
                    if (data != null) {
                        customSkirt.render(player, render.getMainModel(), data, false, dye, extraColours, false, distance, true);
                    }
                }
                if (slot == SkinTypeRegistry.skinFeet.getEntityEquipmentSlot()) {
                    Skin data = getPlayerCustomArmour(player, SkinTypeRegistry.skinFeet, skinIndex);
                    ISkinDye dye = getPlayerDyeData(player, SkinTypeRegistry.skinFeet, skinIndex);
                    if (data != null) {
                        customFeet.render(player, render.getMainModel(), data, false, dye, extraColours, false, distance, true);
                    }
                }
            }

        }
        
        Skin data = getPlayerCustomArmour(player, SkinTypeRegistry.skinWings, 0);
        ISkinDye dye = getPlayerDyeData(player, SkinTypeRegistry.skinWings, 0);
        if (data != null) {
            customWings.render(player, render.getMainModel(), data, false, dye, extraColours, false, distance, true);
        }
        
        GlStateManager.disableBlend();
        GlStateManager.disableCull();
        GlStateManager.popMatrix();
    }
    
    public AbstractModelSkin getModelForEquipmentType(ISkinType skinType) {
        if (skinType == SkinTypeRegistry.skinHead) {
            return customHead;
        } else if (skinType == SkinTypeRegistry.skinChest) {
            return customChest;
        } else if (skinType == SkinTypeRegistry.skinLegs) {
            return customLegs;
        } else if (skinType == SkinTypeRegistry.skinSkirt) {
            return customSkirt;
        } else if (skinType == SkinTypeRegistry.skinFeet) {
            return customFeet;
        } else if (skinType == SkinTypeRegistry.skinSword) {
            return customSword;
        } else if (skinType == SkinTypeRegistry.skinBow) {
            return customBow;
        } else if (skinType == SkinTypeRegistry.skinWings) {
            return customWings;
        }
        return null;
    }
    
    public boolean renderEquipmentPartFromStack(Entity entity, ItemStack stack, ModelBiped modelBiped, byte[] extraColours, double distance, boolean doLodLoading) {
        SkinPointer skinPointer = SkinNBTHelper.getSkinPointerFromStack(stack);
        if (skinPointer == null) {
            return false;
        }
        Skin data = getCustomArmourItemData(skinPointer);
        return renderEquipmentPart(entity, modelBiped, data, skinPointer.getSkinDye(), extraColours, distance, doLodLoading);
    }
    
    public boolean renderEquipmentPartFromStack(ItemStack stack, ModelBiped modelBiped, byte[] extraColours, double distance, boolean doLodLoading) {
        SkinPointer skinPointer = SkinNBTHelper.getSkinPointerFromStack(stack);
        if (skinPointer == null) {
            return false;
        }
        Skin data = getCustomArmourItemData(skinPointer);
        return renderEquipmentPart(null, modelBiped, data, skinPointer.getSkinDye(), extraColours, distance, doLodLoading);
    }
    
    public boolean renderEquipmentPartFromSkinPointer(ISkinPointer skinPointer, float limb1, float limb2, float limb3, float headY, float headX) {
        Skin data = getCustomArmourItemData(skinPointer);
        return renderEquipmentPartRotated(null, data, limb1, limb2, limb3, headY, headX);
    }
    
    public boolean renderEquipmentPart(Entity entity, ModelBiped modelBiped, Skin data, ISkinDye skinDye, byte[] extraColours, double distance, boolean doLodLoading) {
        if (data == null) {
            return false;
        }
        IEquipmentModel model = getModelForEquipmentType(data.getSkinType());
        if (model == null) {
            return false;
        }
        
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        model.render(entity, modelBiped, data, false, skinDye, extraColours, false, distance, doLodLoading);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        return true;
    }
    
    private boolean renderEquipmentPartRotated(Entity entity, Skin data, float limb1, float limb2, float limb3, float headY, float headX) {
        if (data == null) {
            return false;
        }
        IEquipmentModel model = getModelForEquipmentType(data.getSkinType());
        if (model == null) {
            return false;
        }
        model.render(entity, data, limb1, limb2, limb3, headY, headX);
        return true;
    }
}
