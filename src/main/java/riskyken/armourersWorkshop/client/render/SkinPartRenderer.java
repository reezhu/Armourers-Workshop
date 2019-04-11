package riskyken.armourersWorkshop.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinDye;
import riskyken.armourersWorkshop.client.handler.ModClientFMLEventHandler;
import riskyken.armourersWorkshop.client.model.SkinModel;
import riskyken.armourersWorkshop.client.model.bake.ColouredFace;
import riskyken.armourersWorkshop.client.skin.ClientSkinPartData;
import riskyken.armourersWorkshop.common.config.ConfigHandlerClient;
import riskyken.armourersWorkshop.common.lib.LibModInfo;
import riskyken.armourersWorkshop.common.skin.data.SkinPart;
import riskyken.armourersWorkshop.proxies.ClientProxy;
import riskyken.plushieWrapper.client.IRenderBuffer;
import riskyken.plushieWrapper.client.RenderBridge;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class SkinPartRenderer extends ModelBase {

    private static final ResourceLocation texture = new ResourceLocation(LibModInfo.ID, "textures/armour/cube.png");
    public static final SkinPartRenderer INSTANCE = new SkinPartRenderer();
    private final Minecraft mc;
    
    public SkinPartRenderer() {
        mc = Minecraft.getMinecraft();
    }
    
    public void renderPart(SkinPart skinPart, float scale, ISkinDye skinDye, byte[] extraColour, boolean doLodLoading) {
        renderPart(skinPart, scale, skinDye, extraColour, 0, doLodLoading);
    }
    
    public void renderPart(SkinPart skinPart, float scale, ISkinDye skinDye, byte[] extraColour, double distance, boolean doLodLoading) {
        int lod = MathHelper.floor_double(distance / ConfigHandlerClient.lodDistance);
        lod = MathHelper.clamp_int(lod, 0, ConfigHandlerClient.maxLodLevels);
        renderPart(skinPart, scale, skinDye, extraColour, lod, doLodLoading);
    }
    
    private void renderPart(SkinPart skinPart, float scale, ISkinDye skinDye, byte[] extraColour, int lod, boolean doLodLoading) {
        //mc.mcProfiler.startSection(skinPart.getPartType().getPartName());
        ModClientFMLEventHandler.skinRendersThisTick++;
        //GL11.glColor3f(1F, 1F, 1F);
        
        ClientSkinPartData cspd = skinPart.getClientSkinPartData();
        SkinModel skinModel = cspd.getModelForDye(skinDye, extraColour);
        boolean multipassSkinRendering = ClientProxy.useMultipassSkinRendering();
        
        for (int i = 0; i < skinModel.displayListCompiled.length; i++) {
            if (!skinModel.displayListCompiled[i]) {
                if (skinModel.hasList[i]) {
                    skinModel.displayList[i] = GLAllocation.generateDisplayLists(1);
                    GL11.glNewList(skinModel.displayList[i], GL11.GL_COMPILE);
                    renderVertexList(cspd.vertexLists[i], scale, skinDye, extraColour, cspd);
                    GL11.glEndList();
                }
                skinModel.displayListCompiled[i] = true;
                skinModel.setLoaded();
            }
        }
        //mc.renderEngine.bindTexture(texture);
        if (ClientProxy.useSafeTextureRender()) {
            mc.renderEngine.bindTexture(texture);
        } else {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        }
        
        int startIndex = 0;;
        int endIndex = 0;;
        
        int loadingLod = skinModel.getLoadingLod();
        if (!doLodLoading) {
            loadingLod = 0;
        }
        if (loadingLod > lod) {
            lod = loadingLod;
        }
        
        if (lod != 0) {
            if (multipassSkinRendering) {
                startIndex = lod * 4;
            } else {
                startIndex = lod * 2;
            }
        }
        
        if (multipassSkinRendering) {
            endIndex = startIndex + 4;
        } else {
            endIndex = startIndex + 2;
        }

        
        int listCount = skinModel.displayList.length;
        for (int i = startIndex; i < endIndex; i++) {
            if (i >= startIndex & i < endIndex) {
                boolean glowing = false;
                if (i % 2 == 1) {
                    glowing = true;
                }
                if (i >= 0 & i < skinModel.displayList.length) {
                    if (skinModel.hasList[i]) {
                        if (skinModel.displayListCompiled[i]) {
                            if (glowing) {
                                //GL11.glDisable(GL11.GL_LIGHTING);
                                //ModRenderHelper.disableLighting();
                            }
                            if (ConfigHandlerClient.wireframeRender) {
                                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                            }
                            GL11.glCallList(skinModel.displayList[i]);
                            if (ConfigHandlerClient.wireframeRender) {
                                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                            }
                            if (glowing) {
                                //ModRenderHelper.enableLighting();
                                //GL11.glEnable(GL11.GL_LIGHTING);
                            }
                        }
                    }
                }
            }
        }
        
        if (!ClientProxy.useSafeTextureRender()) {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
        GL11.glColor4f(1, 1, 1, 1);
        //GlStateManager.color(1F, 1F, 1F, 1F);
        //mc.mcProfiler.endSection();
    }
    
    private void renderVertexList(ArrayList<ColouredFace> vertexList, float scale, ISkinDye skinDye, byte[] extraColour, ClientSkinPartData cspd) {
        IRenderBuffer renderBuffer = RenderBridge.INSTANCE;
        renderBuffer.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        for (int i = 0; i < vertexList.size(); i++) {
            ColouredFace cVert = vertexList.get(i);
            cVert.renderVertex(renderBuffer, skinDye, extraColour, cspd, ClientProxy.useSafeTextureRender());
        }
        renderBuffer.draw();
    }
}
