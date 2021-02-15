package dev.octalide.pipette.api.blocks.properties;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;

public class PipeSplitterProps extends PipePropsBase {
    public static void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        PipePropsBase.appendProperties(builder);
    }

    public static BlockState buildDefaultState(BlockState state) {
        state = PipePropsBase.buildDefaultState(state);
        
        return state;
    }
}
