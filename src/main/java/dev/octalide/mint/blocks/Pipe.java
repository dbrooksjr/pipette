package dev.octalide.mint.blocks;

import dev.octalide.mint.Mint;
import dev.octalide.mint.blockentities.PipeEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.*;
import java.util.Map.Entry;


public class Pipe extends Block implements BlockEntityProvider {
    public static final String NAME = "pipe";
    public static final Identifier ID = new Identifier(Mint.MOD_ID, NAME);

    public static final int INVENTORY_SLOTS = 2;

    public final Block[] LINKABLE_BLOCKS = {
        Blocks.CHEST,
        Blocks.FURNACE,
        Blocks.DISPENSER,
        Blocks.DROPPER,
        Blocks.TRAPPED_CHEST,
        Blocks.BARREL,
        Blocks.HOPPER,
        Blocks.SHULKER_BOX,
        this
    };

    public Pipe() {
        super(Block.Settings
            .of(Material.METAL)
            .nonOpaque()
            .requiresTool()
            .strength(1.0f)
            .sounds(BlockSoundGroup.METAL)
        );

        setDefaultState(Props.setDefaultState(this.stateManager.getDefaultState()));
    }

    @Override
    public ActionResult onUse(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        if (world.isClient) return ActionResult.SUCCESS;

        Inventory blockEntity = (Inventory) world.getBlockEntity(pos);

        BlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.with(Props.pull, !state.get(Props.pull)));

        /*
        if (!player.getStackInHand(hand).isEmpty()) {
            boolean transferred = false;

            for (int slot = 0; slot <= INVENTORY_SLOTS - 1; slot++) {
                if (blockEntity.getStack(slot).isEmpty()) {
                    blockEntity.setStack(slot, player.getStackInHand(hand).copy());
                    player.getStackInHand(hand).setCount(0);

                    transferred = true;
                    break;
                }
            }

            if (!transferred) {
                player.sendMessage(new LiteralText("The first slot holds " + blockEntity.getStack(0) + " and the second slot holds " + blockEntity.getStack(1)), false);
            }
        } else {
            // Find the first slot that has an item and give it to the player
            for (int slot = INVENTORY_SLOTS - 1; slot >= 0; slot--) {
                if (!blockEntity.getStack(slot).isEmpty()) {
                    player.inventory.offerOrDrop(world, blockEntity.getStack(slot));
                    blockEntity.removeStack(slot);

                    break;
                }
            }
        }
        */

        return ActionResult.SUCCESS;
    }

    private boolean canConnect(BlockState other, Direction direction) {
        boolean can = false;

        DirectionProperty[] props = {
            Properties.FACING,
            Properties.HORIZONTAL_FACING,
            Properties.HOPPER_FACING
        };

        // check if the other block is in the linkable blocks whitelist
        for (Block block : LINKABLE_BLOCKS) {
            if (other.getBlock() == block) {
                for (DirectionProperty directionProp : props) {
                    // check if the other block has a direction property AND that the two are opposites
                    if (other.contains(directionProp) && other.get(directionProp) == direction.getOpposite()) {
                        can = true;
                        break;
                    }
                }
            }
        }

        return can;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState state = this.getDefaultState();

        state = state.with(Props.facing, context.getSide().getOpposite());
        state = state.with(Props.powered, context.getWorld().isReceivingRedstonePower(context.getBlockPos()));

        for (Entry<Direction, BooleanProperty> extension : Props.extensions.entrySet()) {
            BlockState facing = context.getWorld().getBlockState(context.getBlockPos().offset(extension.getKey()));

            state = state.with(
                extension.getValue(),
                canConnect(facing, extension.getKey())
            );
        }

        return state;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return state
            .with(Props.extensions.get(direction), canConnect(newState, direction))
            .with(Props.powered, isPowered(world, pos));
    }

    private boolean isPowered(WorldAccess world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        if (state.getWeakRedstonePower(world, pos.down(), Direction.DOWN) > 0) {
            return true;
        } else if (state.getWeakRedstonePower(world, pos.up(), Direction.UP) > 0) {
            return true;
        } else if (state.getWeakRedstonePower(world, pos.north(), Direction.NORTH) > 0) {
            return true;
        } else if (state.getWeakRedstonePower(world, pos.south(), Direction.SOUTH) > 0) {
            return true;
        } else if (state.getWeakRedstonePower(world, pos.west(), Direction.WEST) > 0) {
            return true;
        } else {
            return state.getWeakRedstonePower(world, pos.east(), Direction.EAST) > 0;
        }
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(Props.facing, rotation.rotate(state.get(Props.facing)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.with(Props.facing, mirror.apply(state.get(Props.facing)));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new PipeEntity();
    }

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
        // decide which VoxelShape combination to supply

        return Shapes.fromState(state);
    }

    public static class Props {
        public static Map<Direction, BooleanProperty> extensions;
        public static DirectionProperty facing = Properties.FACING;
        public static BooleanProperty powered = Properties.POWERED;
        public static BooleanProperty pull = BooleanProperty.of("pull"); // "push" vs "pull" pipe

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
            builder.add(facing);
            builder.add(powered);
            builder.add(pull);

            extensions.values().forEach(builder::add);
        }

        public static BlockState setDefaultState(BlockState state) {
            state = state.with(facing, Direction.NORTH);
            state = state.with(powered, false);
            state = state.with(pull, false);

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

            //                                                      x   y   z  xm  ym  zm
            EXTENSIONS[Direction.DOWN.getId()] =  createCuboidShape( 5,  0,  5, 11,  4, 11);
            EXTENSIONS[Direction.UP.getId()] =    createCuboidShape( 5, 12,  5, 11, 16, 11);
            EXTENSIONS[Direction.NORTH.getId()] = createCuboidShape( 5,  5,  0, 11, 11,  4);
            EXTENSIONS[Direction.SOUTH.getId()] = createCuboidShape( 5,  5, 12, 11, 11, 16);
            EXTENSIONS[Direction.EAST.getId()] =  createCuboidShape(12,  5,  5, 16, 11, 11);
            EXTENSIONS[Direction.WEST.getId()] =  createCuboidShape( 0,  5,  5,  4, 11, 11);
        }

        public static VoxelShape fromState(BlockState state) {
            // TODO: build a shape cache

            VoxelShape shape = CORE;

            Direction facing = state.get(Props.facing);

            // maximum of 6 possible extension shapes including an output and 5 inputs
            ArrayList<VoxelShape> extensionShapes = new ArrayList<>();

            // add extension for facing direction (always present)
            extensionShapes.add(EXTENSIONS[facing.getId()]);

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
