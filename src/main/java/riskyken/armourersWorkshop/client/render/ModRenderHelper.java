package riskyken.armourersWorkshop.client.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public final class ModRenderHelper {
    
    private static float lightX;
    private static float lightY;
    
    public static void disableLighting() {
        lightX = OpenGlHelper.lastBrightnessX;
        lightY = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
    }
    
    public static void enableLighting() {
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightX, lightY);
    }
    
    public static void setLightingForBlock(World world, int x, int y, int z) {
        int i = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
    }
    
    public static void enableAlphaBlend() {
        enableAlphaBlend(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
    
    public static void enableAlphaBlend(int sfactor, int dfactor) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(sfactor, dfactor);
    }
    
    public static void disableAlphaBlend() {
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    public static void renderItemStack(ItemStack stack) {
        IIcon icon = stack.getItem().getIcon(stack, 0);
        ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
    }
    
    public static void enableScissorScaled(int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        double scaledWidth = (double)mc.displayWidth / sr.getScaledWidth_double();
        double scaledHeight = (double)mc.displayHeight / sr.getScaledHeight_double();
        enableScissor(
                MathHelper.floor_double(x * scaledWidth),
                mc.displayHeight - MathHelper.floor_double((double)(y + height) * scaledHeight),
                MathHelper.floor_double(width * scaledWidth),
                MathHelper.floor_double(height * scaledHeight));
    }
    
    public static void enableScissor(int x, int y, int width, int height) {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x, y, width, height);
    }
    
    public static void disableScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
