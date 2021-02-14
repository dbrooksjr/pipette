package dev.octalide.pipette.blockentities;

import java.util.UUID;

import dev.octalide.pipette.PBlocks;
import dev.octalide.pipette.api.blockentities.PipeEntityBase;
import dev.octalide.pipette.api.blocks.PipeBase;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

public class PipeEnderEntity extends PipeEntityBase {
    private UUID owner;

    public PipeEnderEntity() {
        super(PBlocks.PIPE_ENDER_ENTITY);
    }

    @Override
    protected boolean attemptOutput() {
        if (getCachedState().get(PipeBase.Props.powered)) return false;
        if (this.isEmpty()) return false;

        return true;
    }

    public void setOwner(UUID uuid) {
        owner = uuid;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.items;
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
