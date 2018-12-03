package org.jurassicraft.client.render.block;

import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.OrientedBlock;
import org.jurassicraft.server.block.entity.AncientItemHoldingBlockEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class AncientItemHoldingBlockRenderer extends TileEntitySpecialRenderer<AncientItemHoldingBlockEntity> {
	private Minecraft mc = Minecraft.getMinecraft();
	ItemStack stack = new ItemStack(Items.IRON_SHOVEL, 1, 0);

	@Override
	public void render(AncientItemHoldingBlockEntity te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) {

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5F, y + 1.0F, z + 0.5F);
		GlStateManager.disableLighting();

		float scale = 0.7f;
		// GlStateManager.rotate(te.getShovelRotation(), 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.pushAttrib();

		RenderHelper.enableStandardItemLighting();
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
		RenderHelper.disableStandardItemLighting();

		GlStateManager.popAttrib();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
