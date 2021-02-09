package dev.octalide.mint.blocks;

import dev.octalide.mint.Mint;
import dev.octalide.mint.blockentities.DestroyerEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Destroyer extends BlockWithEntity {
    public static final String NAME = "destroyer";
    public static final Identifier ID = new Identifier(Mint.MOD_ID, NAME);

    public Destroyer() {
        super(FabricBlockSettings.of(Material.METAL)
            .nonOpaque()
            .strength(0.5f)
            .sounds(BlockSoundGroup.METAL));

        setDefaultState(Props.buildDefaultState(this.getDefaultState()));
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        world.setBlockState(pos, updatePowered(state, world, pos));
    }

    protected BlockState updatePowered(BlockState state, World world, BlockPos pos) {
        return state.with(Props.powered, world.isReceivingRedstonePower(pos));
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
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        Props.buildState(builder);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState state = this.getDefaultState();

        state = state.with(Props.facing, context.getPlayerLookDirection().getOpposite());

        return state;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new DestroyerEntity();
    }

    public static class Props {
        public static DirectionProperty facing = Properties.FACING;
        public static BooleanProperty powered = Properties.POWERED;

        public static void buildState(StateManager.Builder<Block, BlockState> builder) {
            builder.add(facing);
            builder.add(powered);
        }

        public static BlockState buildDefaultState(BlockState state) {
            state = state.with(facing, Direction.NORTH);
            state = state.with(powered, false);

            return state;
        }
    }
}
