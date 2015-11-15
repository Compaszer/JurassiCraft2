package org.jurassicraft.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.common.entity.item.EntityJurassiCraftSign;

@SideOnly(Side.CLIENT)
public class RenderJurassiCraftSign extends Render
{
    private static final ResourceLocation texture = new ResourceLocation(JurassiCraft.MODID, "textures/entities/gentle_giants/gentle_giants.png");

    public RenderJurassiCraftSign()
    {
        super(Minecraft.getMinecraft().getRenderManager());
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1, double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(EntityJurassiCraftSign entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180.0F - p_76986_8_, 0.0F, 1.0F, 0.0F);
        GlStateManager.enableRescaleNormal();
        this.bindEntityTexture(entity);
        EntityJurassiCraftSign.EnumSignType enumart = entity.signType;
        float f2 = 0.03125F;
        GlStateManager.scale(f2, f2, f2);
        this.func_77010_a(entity, enumart.sizeX * 2, enumart.sizeY * 2, enumart.offsetX, enumart.offsetY);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, p_76986_8_, partialTicks);
    }

    protected ResourceLocation getSignTexture(EntityJurassiCraftSign entity)
    {
        return texture;
    }

    private void func_77010_a(EntityJurassiCraftSign entity, int p_77010_2_, int p_77010_3_, int p_77010_4_, int p_77010_5_)
    {
        float f = (float) (-p_77010_2_) / 2.0F;
        float f1 = (float) (-p_77010_3_) / 2.0F;
        float f2 = 0.5F;
        float f3 = 0.75F;
        float f4 = 0.8125F;
        float f5 = 0.0F;
        float f6 = 0.0625F;
        float f7 = 0.75F;
        float f8 = 0.8125F;
        float f9 = 0.001953125F;
        float f10 = 0.001953125F;
        float f11 = 0.7519531F;
        float f12 = 0.7519531F;
        float f13 = 0.0F;
        float f14 = 0.0625F;

        for (int i1 = 0; i1 < p_77010_2_ / 16; ++i1)
        {
            for (int j1 = 0; j1 < p_77010_3_ / 16; ++j1)
            {
                float f15 = f + (float) ((i1 + 1) * 16);
                float f16 = f + (float) (i1 * 16);
                float f17 = f1 + (float) ((j1 + 1) * 16);
                float f18 = f1 + (float) (j1 * 16);
                this.func_77008_a(entity, (f15 + f16) / 2.0F, (f17 + f18) / 2.0F);
                float f19 = (float) (p_77010_4_ + p_77010_2_ - i1 * 16) / 256.0F;
                float f20 = (float) (p_77010_4_ + p_77010_2_ - (i1 + 1) * 16) / 256.0F;
                float f21 = (float) (p_77010_5_ + p_77010_3_ - j1 * 16) / 256.0F;
                float f22 = (float) (p_77010_5_ + p_77010_3_ - (j1 + 1) * 16) / 256.0F;
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                worldrenderer.startDrawingQuads();
                worldrenderer.setNormal(0.0F, 0.0F, -1.0F);
                worldrenderer.addVertexWithUV((double) f15, (double) f18, (double) (-f2), (double) f20, (double) f21);
                worldrenderer.addVertexWithUV((double) f16, (double) f18, (double) (-f2), (double) f19, (double) f21);
                worldrenderer.addVertexWithUV((double) f16, (double) f17, (double) (-f2), (double) f19, (double) f22);
                worldrenderer.addVertexWithUV((double) f15, (double) f17, (double) (-f2), (double) f20, (double) f22);
                worldrenderer.setNormal(0.0F, 0.0F, 1.0F);
                worldrenderer.addVertexWithUV((double) f15, (double) f17, (double) f2, (double) f3, (double) f5);
                worldrenderer.addVertexWithUV((double) f16, (double) f17, (double) f2, (double) f4, (double) f5);
                worldrenderer.addVertexWithUV((double) f16, (double) f18, (double) f2, (double) f4, (double) f6);
                worldrenderer.addVertexWithUV((double) f15, (double) f18, (double) f2, (double) f3, (double) f6);
                worldrenderer.setNormal(0.0F, 1.0F, 0.0F);
                worldrenderer.addVertexWithUV((double) f15, (double) f17, (double) (-f2), (double) f7, (double) f9);
                worldrenderer.addVertexWithUV((double) f16, (double) f17, (double) (-f2), (double) f8, (double) f9);
                worldrenderer.addVertexWithUV((double) f16, (double) f17, (double) f2, (double) f8, (double) f10);
                worldrenderer.addVertexWithUV((double) f15, (double) f17, (double) f2, (double) f7, (double) f10);
                worldrenderer.setNormal(0.0F, -1.0F, 0.0F);
                worldrenderer.addVertexWithUV((double) f15, (double) f18, (double) f2, (double) f7, (double) f9);
                worldrenderer.addVertexWithUV((double) f16, (double) f18, (double) f2, (double) f8, (double) f9);
                worldrenderer.addVertexWithUV((double) f16, (double) f18, (double) (-f2), (double) f8, (double) f10);
                worldrenderer.addVertexWithUV((double) f15, (double) f18, (double) (-f2), (double) f7, (double) f10);
                worldrenderer.setNormal(-1.0F, 0.0F, 0.0F);
                worldrenderer.addVertexWithUV((double) f15, (double) f17, (double) f2, (double) f12, (double) f13);
                worldrenderer.addVertexWithUV((double) f15, (double) f18, (double) f2, (double) f12, (double) f14);
                worldrenderer.addVertexWithUV((double) f15, (double) f18, (double) (-f2), (double) f11, (double) f14);
                worldrenderer.addVertexWithUV((double) f15, (double) f17, (double) (-f2), (double) f11, (double) f13);
                worldrenderer.setNormal(1.0F, 0.0F, 0.0F);
                worldrenderer.addVertexWithUV((double) f16, (double) f17, (double) (-f2), (double) f12, (double) f13);
                worldrenderer.addVertexWithUV((double) f16, (double) f18, (double) (-f2), (double) f12, (double) f14);
                worldrenderer.addVertexWithUV((double) f16, (double) f18, (double) f2, (double) f11, (double) f14);
                worldrenderer.addVertexWithUV((double) f16, (double) f17, (double) f2, (double) f11, (double) f13);
                tessellator.draw();
            }
        }
    }

    private void func_77008_a(EntityJurassiCraftSign sign, float p_77008_2_, float p_77008_3_)
    {
        int i = MathHelper.floor_double(sign.posX);
        int j = MathHelper.floor_double(sign.posY + (double) (p_77008_3_ / 16.0F));
        int k = MathHelper.floor_double(sign.posZ);
        EnumFacing enumfacing = sign.field_174860_b;

        if (enumfacing == EnumFacing.NORTH)
        {
            i = MathHelper.floor_double(sign.posX + (double) (p_77008_2_ / 16.0F));
        }

        if (enumfacing == EnumFacing.WEST)
        {
            k = MathHelper.floor_double(sign.posZ - (double) (p_77008_2_ / 16.0F));
        }

        if (enumfacing == EnumFacing.SOUTH)
        {
            i = MathHelper.floor_double(sign.posX - (double) (p_77008_2_ / 16.0F));
        }

        if (enumfacing == EnumFacing.EAST)
        {
            k = MathHelper.floor_double(sign.posZ + (double) (p_77008_2_ / 16.0F));
        }

        int l = this.renderManager.worldObj.getCombinedLight(new BlockPos(i, j, k), 0);
        int i1 = l % 65536;
        int j1 = l / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) i1, (float) j1);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getSignTexture((org.jurassicraft.common.entity.item.EntityJurassiCraftSign) entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1, double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.doRender((EntityJurassiCraftSign) entity, x, y, z, p_76986_8_, partialTicks);
    }
}
