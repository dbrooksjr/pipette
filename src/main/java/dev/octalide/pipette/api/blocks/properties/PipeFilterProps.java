package dev.octalide.pipette.api.blocks.properties;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;

public class PipeFilterProps extends PipeExtractorProps {
    public static void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        PipeExtractorProps.appendProperties(builder);
    }

    public static BlockState buildDefaultState(BlockState state) {
        state = PipeExtractorProps.buildDefaultState(state);

        return state;
    }
}
