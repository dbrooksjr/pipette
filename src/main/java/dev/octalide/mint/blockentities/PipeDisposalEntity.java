package dev.octalide.mint.blockentities;

import dev.octalide.mint.blocks.MBlocks;
import dev.octalide.mint.blocks.PipeBase;

public class PipeDisposalEntity extends PipeEntityBase {
    public PipeDisposalEntity() {
        super(MBlocks.PIPE_DISPOSAL_ENTITY);
    }

    @Override
    protected boolean attemptOutput() {
        if (getCachedState().get(PipeBase.Props.powered)) return false;
        if (this.isEmpty()) return false;

        // NOTE: need document how this works
        // sorry for the mess of code
        this.clear();

        return true;
    }
}
