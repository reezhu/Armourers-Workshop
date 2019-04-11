package riskyken.plushieWrapper.client;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBridge implements IRenderBuffer {

    public static IRenderBuffer INSTANCE;
    
    Tessellator tessellator;
    
    public static void init() {
        INSTANCE = new RenderBridge();
    }
    
    private RenderBridge() {
        tessellator = Tessellator.getInstance();
    }
    
    @Override
    public void draw() {
        tessellator.draw();
    }

    @Override
    public void startDrawingQuads(VertexFormat vertexFormat) {
        tessellator.getWorldRenderer().begin(GL11.GL_QUADS, vertexFormat);
    }

    @Override
    public void startDrawing(int glMode, VertexFormat vertexFormat) {
        tessellator.getWorldRenderer().begin(glMode, vertexFormat);
    }
    
    @Override
    public void setColourRGBA_F(float r, float g, float b, float a) {
        tessellator.getWorldRenderer().color(r, g, b, a);
    }
    
    @Override
    public void setColourRGBA_B(byte r, byte g, byte b, byte a) {
        tessellator.getWorldRenderer().color((r & 0xFF) / 255F, (g & 0xFF) / 255F, (b & 0xFF) / 255F, (a & 0xFF) / 255F);
    }

    @Override
    public void setNormal(float x, float y, float z) {
        tessellator.getWorldRenderer().normal(x, y, z);
    }

    @Override
    public void setTextureUV(double u, double v) {
        tessellator.getWorldRenderer().tex(u, v);
    }

    @Override
    public void addVertex(double x, double y, double z) {
        tessellator.getWorldRenderer().pos(x, y, z);
    }

    @Override
    public void addVertexWithUV(double x, double y, double z, double u, double v) {
        tessellator.getWorldRenderer().pos(x, y, z);
        tessellator.getWorldRenderer().tex(u, v);
    }
    
    @Override
    public void lightmap(int x, int y) {
        tessellator.getWorldRenderer().lightmap(x, y);
    }
    
    @Override
    public void endVertex() {
        tessellator.getWorldRenderer().endVertex();
    }
}
