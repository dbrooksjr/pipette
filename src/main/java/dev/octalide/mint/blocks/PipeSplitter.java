package dev.octalide.mint.blocks;

import java.util.Arrays;

import dev.octalide.mint.Mint;
import dev.octalide.mint.blockentities.PipeSplitterEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class PipeSplitter extends PipeBase {
    public static final String NAME = "pipe_splitter";
    public static final Identifier ID = new Identifier(Mint.MOD_ID, NAME);

    @Override
    protected boolean canExtend(BlockState state, BlockState other, Direction direction) {
        boolean can = false;

        Block[] linkable = {
            Blocks.CHEST,
            Blocks.FURNACE,
            Blocks.DISPENSER,
            Blocks.DROPPER,
            Blocks.TRAPPED_CHEST,
            Blocks.BARREL,
            Blocks.HOPPER,
            Blocks.SHULKER_BOX,
        };

        if (Arrays.stream(linkable).anyMatch(block -> other.getBlock() == block)) {
            can = true;

        } else if (other.getBlock() == MBlocks.PIPE) {
            can = true;

        } else if (other.getBlock() == MBlocks.PIPE_EXTRACTOR) {
            can = true;

        } else if (other.getBlock() == MBlocks.PIPE_FILTER) {
            can = true;
        }

        if (state.get(Props.facing) == direction) {
            can = true;
        }

        return can;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new PipeSplitterEntity();
    }
}
