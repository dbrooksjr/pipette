package dev.octalide.pipette.blocks;

import java.util.Arrays;

import dev.octalide.pipette.PItems;
import dev.octalide.pipette.Pipette;
import dev.octalide.pipette.api.blocks.PipeBase;
import dev.octalide.pipette.api.blocks.properties.PipeExtractorProps;
import dev.octalide.pipette.api.blocks.properties.PipeProps;
import dev.octalide.pipette.blockentities.PipeEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Pipe extends PipeBase {
    public static final String NAME = "pipe";
    public static final Identifier ID = new Identifier(Pipette.MOD_ID, NAME);

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult blockHitResult) {
        ActionResult result = ActionResult.PASS;

        if (player.isHolding(PItems.PIPE_WRENCH)) {
            // cycle output
            Direction output = state.get(PipeProps.output);
            Direction next = getNextDirection(state, output);

            state = state.with(PipeProps.output, next);
            state = updateExtensions(state, world, pos);

            world.setBlockState(pos, state);

            world.playSound(null, pos, SoundEvents.BLOCK_CHAIN_STEP, SoundCategory.BLOCKS, 0.5f, 1.5f);
            result = ActionResult.SUCCESS;
        }

        return result;
    }

    @Override
    protected boolean canExtend(BlockState state, BlockState other, Direction direction) {
        boolean can = false;

        DirectionProperty[] props = { Properties.FACING, Properties.HORIZONTAL_FACING, Properties.HOPPER_FACING };

        if (other.getBlock() == Blocks.HOPPER)
            can = Arrays.stream(props).anyMatch(directionProperty -> other.contains(directionProperty)
                    && other.get(directionProperty) == direction.getOpposite());

        else if (other.getBlock() instanceof PipeExtractor)
            can = other.get(PipeExtractorProps.output) == direction.getOpposite()
                    || other.get(PipeExtractorProps.input) == direction.getOpposite();
        else if (other.getBlock() instanceof Pipe)
            can = other.get(PipeProps.output) == direction.getOpposite();
        else if (other.getBlock() instanceof PipeSplitter)
            can = true;

        if (state.get(PipeProps.output) == direction)
            can = true;

        return can;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new PipeEntity();
    }

    @Override
    public void appendProperties(Builder<Block, BlockState> builder) {
        PipeProps.appendProperties(builder);
    }

    @Override
    public BlockState buildDefaultState() {
        return PipeProps.buildDefaultState(this.getDefaultState());
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState state = super.getPlacementState(context);

        Direction targetDirection = context.getSide().getOpposite();
        BlockState target = context.getWorld().getBlockState(context.getBlockPos().offset(targetDirection));

        Direction output = targetDirection.getOpposite();
        if (target.getBlock() instanceof Pipe) {
            if (target.get(PipeProps.output) == output) {
                output = target.get(PipeProps.output).getOpposite();
            }
        }

        state = state.with(PipeProps.output, output.getOpposite());
        state = updateExtensions(state, context.getWorld(), context.getBlockPos());

        return state;
    }
}
