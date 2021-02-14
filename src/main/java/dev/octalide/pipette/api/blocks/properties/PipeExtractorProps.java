package dev.octalide.pipette.api.blocks.properties;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.Direction;

public class PipeExtractorProps extends PipeProps {
    public static DirectionProperty input = DirectionProperty.of("input", Direction.NORTH, Direction.EAST,
            Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);

    public static void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        PipeProps.appendProperties(builder);

        builder.add(input);
    }

    public static BlockState buildDefaultState(BlockState state) {
        state = PipeProps.buildDefaultState(state);
        state = state.with(input, Direction.NORTH);

        return state;
    }
}
