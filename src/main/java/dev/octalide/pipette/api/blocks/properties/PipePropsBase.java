package dev.octalide.pipette.api.blocks.properties;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import java.util.HashMap;
import java.util.Map;

public abstract class PipePropsBase {
    public static Map<Direction, BooleanProperty> extensions;
    public static BooleanProperty powered = Properties.POWERED;

    static {
        extensions = new HashMap<>();
        extensions.put(Direction.DOWN, Properties.DOWN);
        extensions.put(Direction.UP, Properties.UP);
        extensions.put(Direction.NORTH, Properties.NORTH);
        extensions.put(Direction.SOUTH, Properties.SOUTH);
        extensions.put(Direction.EAST, Properties.EAST);
        extensions.put(Direction.WEST, Properties.WEST);
    }

    public static void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(powered);

        extensions.values().forEach(builder::add);
    }

    public static BlockState buildDefaultState(BlockState state) {
        state = state.with(powered, false);

        for (BooleanProperty extension : extensions.values()) {
            state = state.with(extension, false);
        }

        return state;
    }
}
