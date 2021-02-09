package dev.octalide.mint.blockentities;

import dev.octalide.mint.blocks.MBlocks;
import dev.octalide.mint.blocks.PipeBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;


public class PipeSplitterEntity extends PipeEntityBase {
    private int currentOutput = 0;

    public PipeSplitterEntity() {
        super(MBlocks.PIPE_SPLITTER_ENTITY);
    }

    protected boolean attemptOutput() {
        if (world == null || world.isClient()) return false;
        if (this.isEmpty()) return false;
        if (getCachedState().get(PipeBase.Props.powered)) return false;

        Direction facing = getCachedState().get(PipeBase.Props.facing);
        Direction target = selectNextOutput(facing);
        // no valid output directions
        if(target == null) return false;

        Inventory output = HopperBlockEntity.getInventoryAt(world, pos.offset(target));

        if (output == null) return false;

        currentOutput = target.getId();

        return transfer(this, output, facing.getOpposite());
    }

    private Direction selectNextOutput(Direction facing) {
        Direction next = Direction.byId(currentOutput);

        for (int tries = 0; tries <= 5; tries++) {
            switch(next) {
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

            boolean invalid = false;

            // skip if this direction is the same as the input
            if (next == facing) invalid = true;

            // skip if this direction does not have an extension
            if (!getCachedState().get(PipeBase.Props.extensions.get(next))) invalid = true;

            // this output is valid
            if (!invalid) return next;
        }

        return null;
    }

    // Serialize the BlockEntity
    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putInt("current_output", currentOutput);

        return super.toTag(tag);
    }

    // Deserialize the BlockEntity
    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        currentOutput = tag.getInt("current_output");
    }
}
