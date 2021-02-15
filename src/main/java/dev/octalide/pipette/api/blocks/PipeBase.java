package dev.octalide.pipette.api.blocks;

import java.util.ArrayList;
import java.util.Map.Entry;

import dev.octalide.pipette.api.blockentities.PipeEntityBase;
import dev.octalide.pipette.api.blocks.properties.PipeProps;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class PipeBase extends BlockWithEntity {
    public PipeBase() {
        super(FabricBlockSettings.of(Material.METAL).nonOpaque().strength(0.5f).sounds(BlockSoundGroup.METAL));

        setDefaultState(buildDefaultState());
    }

    public abstract ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult);

    protected abstract boolean canExtend(BlockState state, BlockState other, Direction direction);

    /*
        Direction targetDirection = context.getSide().getOpposite();
        BlockState target = context.getWorld().getBlockState(context.getBlockPos().offset(targetDirection));

        Direction output = targetDirection;
        if (target.getBlock() instanceof PipeBase && !context.getPlayer().isSneaking()) {
            if (target.get(Props.output) == targetDirection.getOpposite()) {
                output = target.get(Props.output);
            }
        }

        state = state.with(Props.output, output);
        state = state.with(Props.input, targetDirection);
    */

    public abstract BlockEntity createBlockEntity(BlockView blockView);

    public abstract void appendProperties(StateManager.Builder<Block, BlockState> builder);

    public abstract BlockState buildDefaultState();

    protected BlockState updateExtensions(BlockState state, World world, BlockPos pos) {
        for (Entry<Direction, BooleanProperty> extension : PipeProps.extensions.entrySet()) {
            state = state.with(extension.getValue(),
                    canExtend(state, world.getBlockState(pos.offset(extension.getKey())), extension.getKey()));
        }

        return state;
    }

    protected BlockState updatePowered(BlockState state, World world, BlockPos pos) {
        return state.with(PipeProps.powered, world.isReceivingRedstonePower(pos));
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos,
            boolean notify) {
        state = updatePowered(state, world, pos);
        state = updateExtensions(state, world, pos);

        world.setBlockState(pos, state);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState state = buildDefaultState();

        state = state.with(PipeProps.powered, context.getWorld().isReceivingRedstonePower(context.getBlockPos()));
        state = updateExtensions(state, context.getWorld(), context.getBlockPos());

        return state;
    }
    
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof PipeEntityBase) {
                ItemScatterer.spawn(world, pos, ((PipeEntityBase) entity).getItems());
                world.updateComparators(pos, this);
            }
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return Shapes.fromState(state);
    }

    protected Direction getNextDirection(BlockState state, Direction current) {
        Direction next = incrimentDirection(current);

        return next;
    }

    protected static Direction incrimentDirection(Direction direction) {
        Direction next = direction;

        switch (direction) {
            case DOWN:
                next = Direction.UP;
                break;
            case UP:
                next = Direction.NORTH;
                break;
            case NORTH:
                next = Direction.SOUTH;
                break;
            case SOUTH:
                next = Direction.WEST;
                break;
            case WEST:
                next = Direction.EAST;
                break;
            case EAST:
                next = Direction.DOWN;
                break;
        }

        return next;
    }

    public static class Shapes {
        public static final VoxelShape CORE;
        public static final VoxelShape[] EXTENSIONS = new VoxelShape[6];

        static {

            // values for the center cube (not including inputs or outputs)
            int xMin = 4;
            int yMin = 4;
            int zMin = 4;
            int xMax = 12;
            int yMax = 12;
            int zMax = 12;

            CORE = createCuboidShape(xMin, yMin, zMin, xMax, yMax, zMax);

            // x y z xm ym zm
            EXTENSIONS[Direction.DOWN.getId()] = createCuboidShape(5, 0, 5, 11, 4, 11);
            EXTENSIONS[Direction.UP.getId()] = createCuboidShape(5, 12, 5, 11, 16, 11);
            EXTENSIONS[Direction.NORTH.getId()] = createCuboidShape(5, 5, 0, 11, 11, 4);
            EXTENSIONS[Direction.SOUTH.getId()] = createCuboidShape(5, 5, 12, 11, 11, 16);
            EXTENSIONS[Direction.EAST.getId()] = createCuboidShape(12, 5, 5, 16, 11, 11);
            EXTENSIONS[Direction.WEST.getId()] = createCuboidShape(0, 5, 5, 4, 11, 11);
        }

        public static VoxelShape fromState(BlockState state) {
            VoxelShape shape = CORE;

            // maximum of 6 possible extension shapes including an output and 5 inputs
            ArrayList<VoxelShape> extensionShapes = new ArrayList<>();

            // add each extension shape
            PipeProps.extensions.forEach((direction, prop) -> {
                if (state.get(prop)) {
                    extensionShapes.add(EXTENSIONS[direction.getId()]);
                }
            });

            for (VoxelShape voxelShape : extensionShapes) {
                shape = VoxelShapes.union(shape, voxelShape);
            }

            return shape;
        }
    }
}
