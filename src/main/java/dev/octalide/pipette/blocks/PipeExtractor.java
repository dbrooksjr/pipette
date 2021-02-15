package dev.octalide.pipette.blocks;

import dev.octalide.pipette.Pipette;
import dev.octalide.pipette.api.blocks.properties.PipeExtractorProps;
import dev.octalide.pipette.api.blocks.properties.PipeProps;
import dev.octalide.pipette.blockentities.PipeExtractorEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class PipeExtractor extends Pipe {
    public static final String NAME = "pipe_extractor";
    public static final Identifier ID = new Identifier(Pipette.MOD_ID, NAME);

    @Override
    protected boolean canExtend(BlockState state, BlockState other, Direction direction) {
        boolean can = false;

        if (state.get(PipeExtractorProps.output) == direction) can = true;
        if (state.get(PipeExtractorProps.input) == direction) can = true;

        return can;
    }

    @Override
    protected Direction getNextDirection(BlockState state, Direction current) {
        Direction next = incrimentDirection(current);

        if (next == state.get(PipeExtractorProps.input))
            next = incrimentDirection(next);

        return next;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState state = super.getPlacementState(context);

        state = state.with(PipeProps.output, context.getSide());
        state = state.with(PipeExtractorProps.input, context.getSide().getOpposite());
        state = updateExtensions(state, context.getWorld(), context.getBlockPos());

        return state;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new PipeExtractorEntity();
    }

    @Override
    public void appendProperties(Builder<Block, BlockState> builder) {
        PipeExtractorProps.appendProperties(builder);
    }

    @Override
    public BlockState buildDefaultState() {
        return PipeExtractorProps.buildDefaultState(this.getDefaultState());
    }
}
