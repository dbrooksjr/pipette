package dev.octalide.pipette.blockentities;

import java.util.UUID;

import dev.octalide.pipette.PBlocks;
import dev.octalide.pipette.api.blockentities.PipeEntityBase;
import dev.octalide.pipette.ender.EnderPipeChannel;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

public class PipeEnderEntity extends PipeEntityBase {
    private UUID owner;
    private EnderPipeChannel channel;

    public PipeEnderEntity() {
        super(PBlocks.PIPE_ENDER_ENTITY);
    }

    @Override
    protected boolean attemptOutput() {
        return false;
    }

    public void setOwner(UUID uuid) {
        owner = uuid;
    }

    public EnderPipeChannel getChannel() {
        return channel;
    }

    public void setChannel(EnderPipeChannel channel) {
        System.out.printf("channel set: %s $s", channel.getOwner(), channel.getName());
        this.channel = channel;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return channel == null ? DefaultedList.ofSize(1, ItemStack.EMPTY) : channel.getItems();
    }

    @Override
    public void markDirty() {
        channel.markDirty();
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
