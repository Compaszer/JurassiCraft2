package org.jurassicraft.server.world.structure;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
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

import java.util.List;
import java.util.Map;
import java.util.Random;

public class GeneticistVillagerHouse extends StructureVillagePieces.Village {
    public static final int WIDTH = 8;
    public static final int HEIGHT = 6;
    public static final int DEPTH = 14;

    public static final int OFFSET_X = -4;
    public static final int OFFSET_Y = 0;
    public static final int OFFSET_Z = -13;

    public static final IBlockState[] MACHINES = new IBlockState[] { BlockHandler.DNA_EXTRACTOR.getDefaultState(), BlockHandler.DNA_SEQUENCER.getDefaultState(), BlockHandler.DNA_COMBINATOR_HYBRIDIZER.getDefaultState(), BlockHandler.DNA_SYNTHESIZER.getDefaultState(), BlockHandler.EMBRYONIC_MACHINE.getDefaultState() };

    private static final ResourceLocation STRUCTURE = new ResourceLocation(JurassiCraft.MODID, "geneticist_house");

    private int count;
    private EnumFacing coordBaseMode;
    private Mirror mirror;
    private Rotation rotation;

    public GeneticistVillagerHouse() {
    }

    public GeneticistVillagerHouse(StructureVillagePieces.Start start, int type, StructureBoundingBox bounds, EnumFacing facing) {
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
        MinecraftServer server = world.getMinecraftServer();
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings()
                .setRotation(this.rotation)
                .setMirror(this.mirror)
                .setIgnoreEntities(true);
        Template template = templateManager.getTemplate(server, STRUCTURE);//east.
        if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(world, bounds);
            if (this.averageGroundLvl < 0) {
                return false;
            }
            settings.setIgnoreEntities(false);
            this.boundingBox.offset(0, ((this.averageGroundLvl - this.boundingBox.maxY) + HEIGHT) - 1, 0);
            boolean invert = this.mirror == Mirror.LEFT_RIGHT;
            switch (this.coordBaseMode) {
                case SOUTH:
                    this.boundingBox.offset(OFFSET_X, 0, -OFFSET_Z);
                    break;
                case WEST:
                case EAST:
                    this.boundingBox.offset(invert ? 0 : -OFFSET_Z, 0, OFFSET_X);
                    break;
            }
        }
        BlockPos lowerCorner = new BlockPos(this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ);
        settings.setBoundingBox(this.boundingBox);
        template.addBlocksToWorldChunk(world, lowerCorner, settings);
        this.count++;
        Map<BlockPos, String> dataBlocks = template.getDataBlocks(lowerCorner, settings);
        for (Map.Entry<BlockPos, String> block : dataBlocks.entrySet()) {
            BlockPos pos = block.getKey();
            String type = block.getValue();
            switch (type) {
                case "GeneticistChest":
                    world.setBlockState(pos, Blocks.CHEST.getDefaultState().withRotation(this.rotation.add(Rotation.CLOCKWISE_90)));
                    break;
                case "GeneticistMachine":
                    if (random.nextInt(4) == 0) {
                        world.setBlockState(pos, MACHINES[random.nextInt(MACHINES.length)].withRotation(this.rotation.add(Rotation.CLOCKWISE_90)));
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setInteger("count", this.count);
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        super.readStructureFromNBT(tagCompound);
        this.count = tagCompound.getInteger("count");
    }

    public static class CreationHandler implements VillagerRegistry.IVillageCreationHandler {
        @Override
        public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int size) {
            return new StructureVillagePieces.PieceWeight(GeneticistVillagerHouse.class, 1, MathHelper.getRandomIntegerInRange(random, 0, 1));
        }

        @Override
        public Class<?> getComponentClass() {
            return GeneticistVillagerHouse.class;
        }

        @Override
        public StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int minX, int minY, int minZ, EnumFacing facing, int componentType) {
            StructureBoundingBox bounds = StructureBoundingBox.getComponentToAddBoundingBox(minX, minY, minZ, 0, 0, 0, WIDTH, HEIGHT, DEPTH, facing);
            return StructureComponent.findIntersecting(pieces, bounds) == null ? new GeneticistVillagerHouse(startPiece, componentType, bounds, facing) : null;
        }
    }
}