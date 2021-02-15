package dev.octalide.pipette.blockentities;

import dev.octalide.pipette.PBlocks;
import dev.octalide.pipette.Pipette;
import dev.octalide.pipette.api.blockentities.PipeEntityBase;
import dev.octalide.pipette.ender.EnderChannel;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

import java.util.UUID;

public class PipeEnderEntity extends PipeEntityBase {
    private UUID owner;
    private EnderChannel channel;

    public PipeEnderEntity() {
        super(PBlocks.PIPE_ENDER_ENTITY);
    }

    @Override
    protected boolean attemptOutput() {
        return false;
    }

    @Override
    public void tick() {
        if (world == null || world.isClient())
            return;
        if (!hasChannel()) {
            if (owner == null)
                return;

            updateChannel();
        }
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;

        updateChannel();
    }

    public EnderChannel getChannel() {
        return channel;
    }

    public void updateChannel() {
        if (world == null)
            return;
        if (world.isClient())
            return;
        if (Pipette.ECS == null)
            return;

        channel = Pipette.ECS.getOrCreateChannel(owner);
    }

    public boolean hasChannel() {
        return Pipette.ECS != null && channel != null;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return hasChannel() ? channel.getItems() : DefaultedList.ofSize(0, ItemStack.EMPTY);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        if (world != null && world.isClient())
            return;

        setOwner(tag.getUuid("owner"));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putUuid("owner", owner);

        return super.toTag(tag);
    }
}
