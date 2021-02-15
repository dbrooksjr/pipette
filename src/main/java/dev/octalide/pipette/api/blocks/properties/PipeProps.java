package dev.octalide.pipette.api.blocks.properties;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

public class PipeProps extends PipePropsBase {
    public static DirectionProperty output = Properties.FACING;

    public static void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        PipePropsBase.appendProperties(builder);

        builder.add(output);
    }

    public static BlockState buildDefaultState(BlockState state) {
        state = PipePropsBase.buildDefaultState(state);
        state = state.with(output, Direction.NORTH);
        
        return state;
    }
}
