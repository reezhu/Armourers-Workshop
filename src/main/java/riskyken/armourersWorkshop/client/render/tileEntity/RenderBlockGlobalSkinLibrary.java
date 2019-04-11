package riskyken.armourersWorkshop.client.render.tileEntity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import riskyken.armourersWorkshop.client.model.block.ModelBlockGlobalSkinLibrary;
import riskyken.armourersWorkshop.common.tileentities.TileEntityGlobalSkinLibrary;

public class RenderBlockGlobalSkinLibrary extends TileEntitySpecialRenderer {

    private static final ModelBlockGlobalSkinLibrary GLOBE_MODEL = new ModelBlockGlobalSkinLibrary();
    private static final float SCALE = 0.0625F;
    
    public void renderTileEntityAt(TileEntityGlobalSkinLibrary tileEntity, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5D, y + 1.5D, z + 0.5D);
        GL11.glScalef(-1, -1, 1);
        GLOBE_MODEL.render(tileEntity, partialTicks, SCALE);
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        renderTileEntityAt((TileEntityGlobalSkinLibrary)te, x, y, z, partialTicks);
    }
}