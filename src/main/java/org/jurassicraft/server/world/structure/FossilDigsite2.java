package org.jurassicraft.server.world.structure;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.world.loot.Loot;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class FossilDigsite2 extends StructureVillagePieces.Village {
    public static final int WIDTH = 9;
    public static final int HEIGHT = 0;
    public static final int DEPTH = 0;

    public static int OFFSET_X = -4;
    public static int OFFSET_Y = 0;
    public static int OFFSET_Z = -13;

    private static final ResourceLocation STRUCTURE1 = new ResourceLocation(JurassiCraft.MODID, "fossildigsite_1");
    private static final ResourceLocation STRUCTURE2 = new ResourceLocation(JurassiCraft.MODID, "fossildigsite_2");
    private static final ResourceLocation STRUCTURE3 = new ResourceLocation(JurassiCraft.MODID, "fossildigsite_3");


    private static ResourceLocation STRUCTURES = new ResourceLocation(JurassiCraft.MODID, "");

    private int count;
    private EnumFacing coordBaseMode;
    private Mirror mirror;
    private Rotation rotation;

    public FossilDigsite2() {
    }

    public FossilDigsite2(StructureVillagePieces.Start start, int type, StructureBoundingBox bounds, EnumFacing facing) {
        super(start, type);
        this.setCoordBaseMode(facing);
        this.boundingBox = bounds;
    }

    @Override
    public void setCoordBaseMode(EnumFacing facing) {
        super.setCoordBaseMode(facing);
        this.coordBaseMode = facing;
        if (facing == null) {
            this.rotation = Rotation.NONE;
            this.mirror = Mirror.NONE;
        } else {
            switch (facing) {
                case SOUTH:
                    this.mirror = Mirror.LEFT_RIGHT;
                    this.rotation = Rotation.NONE;
                    break;
                case WEST:
                    this.mirror = Mirror.LEFT_RIGHT;
                    this.rotation = Rotation.CLOCKWISE_90;
                    break;
                case EAST:
                    this.mirror = Mirror.NONE;
                    this.rotation = Rotation.CLOCKWISE_90;
                    break;
                default:
                    this.mirror = Mirror.NONE;
                    this.rotation = Rotation.NONE;
            }
        }
    }

    @Override
    public boolean addComponentParts(World world, Random random, StructureBoundingBox bounds) {
        PlacementSettings settings = new PlacementSettings()
                .setRotation(this.rotation)
                .setMirror(this.mirror)
                .setIgnoreEntities(true);
        if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(world, bounds);
            if (this.averageGroundLvl < 0) {
                return true;
            }
            settings.setIgnoreEntities(false);
        }
        this.boundingBox.offset(0, ((this.averageGroundLvl - this.boundingBox.maxY) + HEIGHT) + 1, 0);
        int structureType = random.nextInt(3);
        switch (structureType) {
            case 0:
                STRUCTURES = STRUCTURE1;
                BlockPos offset1 = Template.transformedBlockPos(settings, new BlockPos(-5, -5, -5));
                this.boundingBox.offset(offset1.getX(), offset1.getY(), offset1.getZ());break;
            case 1:
                STRUCTURES = STRUCTURE2;
                BlockPos offset2 = Template.transformedBlockPos(settings, new BlockPos(5, -8, 5));
                this.boundingBox.offset(offset2.getX(), offset2.getY(), offset2.getZ());
                break;
            case 2:
                STRUCTURES = STRUCTURE3;
                BlockPos offset3 = Template.transformedBlockPos(settings, new BlockPos(-10, -11, -10));
                this.boundingBox.offset(offset3.getX(), offset3.getY(), offset3.getZ());
                break;
        }

        MinecraftServer server = world.getMinecraftServer();
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        Template template = templateManager.getTemplate(server, STRUCTURES);
        BlockPos lowerCorner = new BlockPos(this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ);
        settings.setBoundingBox(this.boundingBox);
        template.addBlocksToWorldChunk(world, lowerCorner, settings);
        this.count++;
        Map<BlockPos, String> dataBlocks = template.getDataBlocks(lowerCorner, settings);
        dataBlocks.forEach((pos, type) -> {
            switch (type) {
                case "GeneticistChest":
                    world.setBlockState(pos, Blocks.CHEST.getDefaultState().withRotation(this.rotation.add(Rotation.CLOCKWISE_90)).withMirror(this.mirror));
                    ((TileEntityChest) world.getTileEntity(pos)).setLootTable(Loot.GENETICIST_HOUSE_CHEST, random.nextLong());
                    break;
                case "Log":
                    world.setBlockState(pos, this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState()));
                    break;
                case "Fence":
                    world.setBlockState(pos, this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState()));
                    break;
            }
        });
        return true;
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setInteger("count", this.count);
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
        super.readStructureFromNBT(tagCompound, templateManager);
        this.count = tagCompound.getInteger("count");
    }

    public static class CreationHandler implements VillagerRegistry.IVillageCreationHandler {
        @Override
        public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int size) {
            return new StructureVillagePieces.PieceWeight(FossilDigsite2.class, 1, MathHelper.getInt(random, 0, 1));
        }

        @Override
        public Class<?> getComponentClass() {
            return FossilDigsite2.class;
        }

        @Override
        public StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int minX, int minY, int minZ, EnumFacing facing, int componentType) {
            StructureBoundingBox bounds = StructureBoundingBox.getComponentToAddBoundingBox(minX, minY, minZ, WIDTH, minY, WIDTH, WIDTH, StructureBoundingBox.getNewBoundingBox().getYSize(), WIDTH, facing);
            return StructureComponent.findIntersecting(pieces, bounds) == null ? new FossilDigsite2(startPiece, componentType, bounds, facing) : null;
        }
    }
}
