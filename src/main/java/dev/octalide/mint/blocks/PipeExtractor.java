package dev.octalide.mint.blocks;

import dev.octalide.mint.Mint;
import dev.octalide.mint.blockentities.PipeExtractorEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;


public class PipeExtractor extends PipeBase {
    public static final String NAME = "pipe_extractor";
    public static final Identifier ID = new Identifier(Mint.MOD_ID, NAME);

    @Override
    protected boolean canExtend(BlockState state, BlockState other, Direction direction) {
        boolean can = super.canExtend(state, other, direction);

        if (direction == state.get(Props.input)) can = true;

        return can;
    }

    @Override
    protected Direction getNextDirection(BlockState state, Direction current) {
        Direction next = incrimentDirection(current);

        if (next == state.get(Props.input)) next = incrimentDirection(next);

        return next;
    }

    protected Direction incrimentDirection(Direction direction) {
        Direction next = direction;

        switch(direction) {
            case DOWN:
                next = Direction.UP;
                break;
            case UP:
                next = Direction.NORTH;
                break;
            case NORTH:
                next = Direction.SOUTH;
                break;
            case SOUTH:
                next = Direction.WEST;
                break;
            case WEST:
                next = Direction.EAST;
                break;
            case EAST:
                next = Direction.DOWN;
                break;
        }

        return next;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new PipeExtractorEntity();
    }
}
