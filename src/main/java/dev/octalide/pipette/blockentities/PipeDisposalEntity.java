package dev.octalide.pipette.blockentities;

import dev.octalide.pipette.PBlocks;
import dev.octalide.pipette.api.blockentities.PipeEntityBase;
import dev.octalide.pipette.api.blocks.properties.PipeDisposalProps;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class PipeDisposalEntity extends PipeEntityBase {
    public PipeDisposalEntity() {
        super(PBlocks.PIPE_DISPOSAL_ENTITY);
    }

    @Override
    protected boolean attemptOutput() {
        if (getCachedState().get(PipeDisposalProps.powered))
            return false;
        if (this.isEmpty())
            return false;

        // NOTE: need document how this works
        // sorry for the mess of code
        this.clear();

        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return false;
    }
}
