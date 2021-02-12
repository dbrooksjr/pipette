package dev.octalide.mint.blocks;

import dev.octalide.mint.blockentities.PipeEntityBase;
import dev.octalide.mint.items.MItems;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class PipeBase extends BlockWithEntity {
    public PipeBase() {
        super(FabricBlockSettings.of(Material.METAL).nonOpaque().strength(0.5f).sounds(BlockSoundGroup.METAL));

        setDefaultState(Props.buildDefaultState(this.getDefaultState()));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult blockHitResult) {
        ActionResult result = ActionResult.PASS;

        if (player.isHolding(MItems.PIPE_WRENCH)) {
            // cycle output
            Direction output = state.get(Props.output);
            Direction next = getNextDirection(state, output);

            state = state.with(Props.output, next);
            state = updateExtensions(state, world, pos);

            world.setBlockState(pos, state);

            world.playSound(null, pos, SoundEvents.BLOCK_CHAIN_STEP, SoundCategory.BLOCKS, 0.5f, 1.5f);
            result = ActionResult.SUCCESS;
        }

        return result;
    }

    protected Direction getNextDirection(BlockState state, Direction current) {
        Direction next = incrimentDirection(current);

        return next;
    }

    protected Direction incrimentDirection(Direction direction) {
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

    protected BlockState updateExtensions(BlockState state, World world, BlockPos pos) {
        for (Entry<Direction, BooleanProperty> extension : Props.extensions.entrySet()) {
            state = state.with(extension.getValue(),
                    canExtend(state, world.getBlockState(pos.offset(extension.getKey())), extension.getKey()));
        }

        return state;
    }

    protected BlockState updatePowered(BlockState state, World world, BlockPos pos) {
        return state.with(Props.powered, world.isReceivingRedstonePower(pos));
    }

    protected boolean canExtend(BlockState state, BlockState other, Direction direction) {
        boolean can = false;

        DirectionProperty[] props = { Properties.FACING, Properties.HORIZONTAL_FACING, Properties.HOPPER_FACING };

        if (other.getBlock() == Blocks.HOPPER)
            can = Arrays.stream(props).anyMatch(directionProperty -> other.contains(directionProperty)
                    && other.get(directionProperty) == direction.getOpposite());

        else if (other.getBlock() == MBlocks.PIPE)
            can = other.get(Props.output) == direction.getOpposite();
        else if (other.getBlock() == MBlocks.PIPE_EXTRACTOR)
            can = other.get(Props.output) == direction.getOpposite()
                    || other.get(Props.input) == direction.getOpposite();
        else if (other.getBlock() == MBlocks.PIPE_FILTER)
            can = other.get(Props.output) == direction.getOpposite()
                    || other.get(Props.input) == direction.getOpposite();
        else if (other.getBlock() == MBlocks.PIPE_SPLITTER)
            can = true;

        if (state.get(Props.output) == direction)
            can = true;

        return can;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState state = this.getDefaultState();

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
        state = state.with(Props.powered, context.getWorld().isReceivingRedstonePower(context.getBlockPos()));
        state = updateExtensions(state, context.getWorld(), context.getBlockPos());

        return state;
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos,
            boolean notify) {
        state = updatePowered(state, world, pos);
        state = updateExtensions(state, world, pos);

        world.setBlockState(pos, state);
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
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(Props.output, rotation.rotate(state.get(Props.output)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.with(Props.output, mirror.apply(state.get(Props.output)));
    }

    public abstract BlockEntity createBlockEntity(BlockView blockView);

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        Props.buildState(builder);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return Shapes.fromState(state);
    }

    public static class Props {
        public static Map<Direction, BooleanProperty> extensions;
        public static BooleanProperty powered = Properties.POWERED;
        public static DirectionProperty output = Properties.FACING;
        // input is used by extractor-style pipes. while other pipes *do* have this
        // state var, it's not used.
        public static DirectionProperty input = DirectionProperty.of("input", Direction.NORTH, Direction.EAST,
                Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);

        static {
            extensions = new HashMap<>();
            extensions.put(Direction.DOWN, Properties.DOWN);
            extensions.put(Direction.UP, Properties.UP);
            extensions.put(Direction.NORTH, Properties.NORTH);
            extensions.put(Direction.SOUTH, Properties.SOUTH);
            extensions.put(Direction.EAST, Properties.EAST);
            extensions.put(Direction.WEST, Properties.WEST);
        }

        public static void buildState(StateManager.Builder<Block, BlockState> builder) {
            builder.add(output);
            builder.add(input);
            builder.add(powered);

            extensions.values().forEach(builder::add);
        }

        public static BlockState buildDefaultState(BlockState state) {
            state = state.with(output, Direction.NORTH);
            state = state.with(powered, false);

            for (BooleanProperty extension : extensions.values()) {
                state = state.with(extension, false);
            }

            return state;
        }
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
            // TODO: build a shape cache

            VoxelShape shape = CORE;

            // maximum of 6 possible extension shapes including an output and 5 inputs
            ArrayList<VoxelShape> extensionShapes = new ArrayList<>();

            // add each extension shape
            Props.extensions.forEach((direction, prop) -> {
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
