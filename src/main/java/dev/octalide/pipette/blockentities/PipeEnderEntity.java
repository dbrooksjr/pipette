package dev.octalide.pipette.blockentities;

import java.util.UUID;

import dev.octalide.pipette.PBlocks;
import dev.octalide.pipette.api.blockentities.PipeEntityBase;
import dev.octalide.pipette.api.blocks.PipeBase;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;

public class PipeEnderEntity extends PipeEntityBase {
    private UUID owner;

    public PipeEnderEntity() {
        super(PBlocks.PIPE_ENDER_ENTITY);
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

    public void setOwner(UUID uuid) {
        owner = uuid;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        
        owner = tag.getUuid("owner");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putUuid("owner", owner);

        return super.toTag(tag);
    }
}
