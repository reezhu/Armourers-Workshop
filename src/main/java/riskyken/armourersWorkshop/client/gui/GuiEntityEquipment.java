package riskyken.armourersWorkshop.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinType;
import riskyken.armourersWorkshop.common.inventory.ContainerEntityEquipment;
import riskyken.armourersWorkshop.common.inventory.InventoryEntitySkin;
import riskyken.armourersWorkshop.common.lib.LibModInfo;

import java.util.ArrayList;

public class GuiEntityEquipment extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(LibModInfo.ID, "textures/gui/entitySkinInventory.png");
    
    private final InventoryEntitySkin skinInventory;
    
    public GuiEntityEquipment(InventoryPlayer playerInventory, InventoryEntitySkin skinInventory) {
        super(new ContainerEntityEquipment(playerInventory, skinInventory));
        this.skinInventory = skinInventory;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.xSize = 176;
        this.ySize = 148;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GuiHelper.renderLocalizedGuiName(this.fontRendererObj, this.xSize, "entityEquipment");
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        
        ArrayList<ISkinType> skinTypes = skinInventory.getSkinTypes();
        for (int i = 0; i < skinTypes.size(); i++) {
            drawTexturedModalRect(this.guiLeft + 7 + i * 18, this.guiTop + 20, 0, 148, 18, 18);
        }
    }
}
