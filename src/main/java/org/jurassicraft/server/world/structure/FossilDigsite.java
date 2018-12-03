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
import org.jurassicraft.server.world.loot.Loot;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class FossilDigsite extends StructureVillagePieces.Village {

    private Mirror mirror;
    private Rotation rotation;
    private Random random;
    public static final int WIDTH = 8;
    public static int HEIGHT = 0;
    public static int DEPTH = 0;
    public static final int OFFSET_X = 0;
    public static final int OFFSET_Y = 0;
    public static final int OFFSET_Z = 0;

    private int count;
    private EnumFacing coordBaseMode;

    private static final ResourceLocation STRUCTURE1 = new ResourceLocation(JurassiCraft.MODID, "fossildigsite_1");
    private static final ResourceLocation STRUCTURE2 = new ResourceLocation(JurassiCraft.MODID, "fossildigsite_2");
    private static final ResourceLocation STRUCTURE3 = new ResourceLocation(JurassiCraft.MODID, "fossildigsite_3");


    private static ResourceLocation STRUCTURES = new ResourceLocation(JurassiCraft.MODID, "fossildigsite");

    public FossilDigsite() {
    }

    public FossilDigsite(StructureVillagePieces.Start start, int type, StructureBoundingBox bounds, EnumFacing facing) {
        super(start, type);
        this.setCoordBaseMode(facing);
        this.boundingBox = bounds;
    }



    @Override
    public boolean addComponentParts(World world, Random random, StructureBoundingBox bounds) {
        if(random.nextInt(3) == 1 ) {
            STRUCTURES = STRUCTURE1;
        } if (random.nextInt(3) == 2) {
            STRUCTURES = STRUCTURE2;
        } if (random.nextInt(3) == 3) {
            STRUCTURES = STRUCTURE3;
        }if(random.nextInt(3) == 0 ) {
            STRUCTURES = STRUCTURE1;
        }
            MinecraftServer server = world.getMinecraftServer();
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings()
                .setRotation(this.rotation)
                .setMirror(this.mirror)
                .setIgnoreEntities(false);
        Template template = templateManager.getTemplate(server, STRUCTURES);
        if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(world, bounds);
            if (this.averageGroundLvl < 0) {
                return true;
            }
            settings.setIgnoreEntities(false);
            {
                this.boundingBox.offset(0, ((this.averageGroundLvl - this.boundingBox.maxY) - DEPTH) - 1, 0);
            }
            boolean invert = this.mirror == Mirror.LEFT_RIGHT;
            switch (this.coordBaseMode) {
                case SOUTH:
                    if(STRUCTURES == STRUCTURE1) {
                        this.boundingBox.offset( -OFFSET_Z, OFFSET_Y -5, OFFSET_X);
                    } if (STRUCTURES == STRUCTURE2) {
                    this.boundingBox.offset( -OFFSET_Z, OFFSET_Y -8, OFFSET_X);
                } if (STRUCTURES == STRUCTURE3) {
                    this.boundingBox.offset( -OFFSET_Z, OFFSET_Y -11, OFFSET_X);
                } break;
                case WEST:
                    break;
                case EAST:
                    if(STRUCTURES == STRUCTURE1) {
                        this.boundingBox.offset(invert ? 0 : -OFFSET_Z, OFFSET_Y -5, OFFSET_X);
                    } if (STRUCTURES == STRUCTURE2) {
                    this.boundingBox.offset(invert ? 0 : -OFFSET_Z, OFFSET_Y -8, OFFSET_X);
                } if (STRUCTURES == STRUCTURE3) {
                    this.boundingBox.offset(invert ? 0 : -OFFSET_Z, OFFSET_Y -11, OFFSET_X);
                } break;
            }
        }
        BlockPos lowerCorner = new BlockPos(this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ);
        settings.setBoundingBox(this.boundingBox);
        template.addBlocksToWorldChunk(world, lowerCorner, settings);
        this.count++;
        Map<BlockPos, String> dataBlocks = template.getDataBlocks(lowerCorner, settings);
        dataBlocks.forEach((pos, type) -> {
            switch (type) {
                case "DigsiteChest":
                    world.setBlockState(pos, Blocks.CHEST.getDefaultState().withRotation(this.rotation.add(Rotation.CLOCKWISE_90)).withMirror(this.mirror));
                    ((TileEntityChest) world.getTileEntity(pos)).setLootTable(Loot.FOSSIL_DIGSITE_LOOT, random.nextLong());
                    break;
                case "Log":
                    world.setBlockState(pos, this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState()));
                    break;
                case "Base":
                    world.setBlockState(pos, this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState()));
                    break;
                case "OakStairs":
                    world.setBlockState(pos, this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withRotation(this.rotation).withMirror(this.mirror)));
                    break;
                case "Ladder":
                    world.setBlockState(pos, this.getBiomeSpecificBlockState(Blocks.LADDER.getDefaultState().withRotation(this.rotation).withMirror(this.mirror)));
                case "Fence":
                    world.setBlockState(pos, this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState()));
                    break;
                //case "Air":
                //    this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 12, 2, bounds);

            }
        });
        return true;
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
    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setInteger("count", this.count);
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
        super.readStructureFromNBT(tagCompound, p_143011_2_);
        this.count = tagCompound.getInteger("count");
    }

    public static class CreationHandler implements VillagerRegistry.IVillageCreationHandler {
        @Override
        public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int size) {
            return new StructureVillagePieces.PieceWeight(FossilDigsite.class, 1, MathHelper.getInt(random, 0, 50));
        }

        @Override
        public Class<?> getComponentClass() {
            return FossilDigsite.class;
        }

        @Override
        public StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int minX, int minY, int minZ, EnumFacing facing, int componentType) {
            StructureBoundingBox bounds = StructureBoundingBox.getComponentToAddBoundingBox(minX, minY, minZ, 0, 0, 0, WIDTH, HEIGHT, DEPTH, facing);
            return StructureComponent.findIntersecting(pieces, bounds) == null ? new FossilDigsite(startPiece, componentType, bounds, facing) : null;
        }
    }
}