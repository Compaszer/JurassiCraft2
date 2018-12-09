package org.jurassicraft.server.block;

import java.util.List;
import java.util.Random;

import org.jurassicraft.server.block.entity.AncientItemHoldingBlockEntity;
import org.jurassicraft.server.tab.TabHandler;

import com.ibm.icu.lang.UCharacter.SentenceBreak;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AncientItemHoldingBlock extends Block implements ITileEntityProvider {
	public AncientItemHoldingBlock() {
		super(Material.SAND);
		setUnlocalizedName("ancient_item_holding_block");
		setSoundType(SoundType.SAND);
		// setCreativeTab(TabHandler.BLOCKS);
		this.hasTileEntity = true;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		super.onBlockHarvested(worldIn, pos, state, player);
		spawnAsEntity(worldIn, pos, new ItemStack(Items.IRON_SHOVEL));
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(Blocks.SAND);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		Random rand = new Random();
		return new AncientItemHoldingBlockEntity(rand.nextInt(360));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
	}

	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}

	// @Override
	// public EnumBlockRenderType getRenderType(IBlockState state) {
	// return EnumBlockRenderType.MODEL;
	// }
}
