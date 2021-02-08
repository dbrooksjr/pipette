package dev.octalide.mint.blockentities;

import dev.octalide.mint.blocks.MBlocks;
import dev.octalide.mint.blocks.PipeBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;

public class PipeSplitterEntity extends PipeEntityBase {
    private int currentOutput = 0;

    public PipeSplitterEntity() {
        super(MBlocks.PIPE_EXTRACTOR_ENTITY);
    }

    @Override
    protected boolean attemptOutput() {
        if (world == null) return false;
        if (getCachedState().get(PipeBase.Props.powered)) return false;

        ItemStack stack = getStack(0);

        if (stack == null) return false;
        if (stack.isEmpty()) return false;

        Direction inputDirection = getCachedState().get(PipeBase.Props.facing);
        Direction outputDirection = Direction.byId(currentOutput);

        boolean skip = false;
        boolean result = false;

        // skip if this direction does not have an extension
        if (!getCachedState().get(PipeBase.Props.extensions.get(outputDirection))) skip = true;

        // skip if this direction has already been tried this cycle
        if (outputDirection.getId() < currentOutput) skip = true;

        // skip if this direction is the same as the input
        if (inputDirection == outputDirection) skip = true;

        if (!skip) {
            Inventory outputInventory = HopperBlockEntity.getInventoryAt(world, pos.offset(outputDirection));

            // output block has an inventory
            if (outputInventory != null) {
                // copy the stack
                ItemStack stackCopy = stack.copy();

                // transfer to the output block's inventory
                ItemStack res = HopperBlockEntity.transfer(this, outputInventory, this.removeStack(0, 1), outputDirection);

                // mark as dirty
                if (res.isEmpty()) {
                    outputInventory.markDirty();
                    result = true;
                } else {
                    // put the stack back
                    this.setStack(0, stackCopy);
                }
            }
        }

        // cycle output direction
        currentOutput++;
        if (currentOutput > 5) currentOutput = 0;

        /*
        TODO:
        Move this code to a new "splitter" pipe.
        Only allow extractors and filters to connect to one output at a time.
        Override extractor/filter wrench behaviour from direction flip to output swap between valid inventories.
        Fix behaviour for sided inventories (e.g furnace...)
        Smoothen input/output methods...
        */

        return result;
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
