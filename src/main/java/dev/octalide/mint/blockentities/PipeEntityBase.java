package dev.octalide.mint.blockentities;

import dev.octalide.mint.PipeInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;

public abstract class PipeEntityBase extends BlockEntity implements PipeInventory, Tickable {
    public static final int TRANSFER_COOLDOWN_MAX = 2;

    protected final String transferCooldownTag = "transfer_cooldown";
    protected float transferCooldown = -1;

    protected DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public PipeEntityBase(BlockEntityType type) {
        super(type);
    }

    protected abstract boolean attemptOutput();

    @Override
    public void tick() {
        if (world == null || world.isClient()) return;

        transferCooldown--;

        if (transferCooldown > 0) return;
        transferCooldown = 0;

        if (attemptOutput()) {
            transferCooldown = TRANSFER_COOLDOWN_MAX;
            markDirty();
        }
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    // Serialize the BlockEntity
    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putFloat(transferCooldownTag, transferCooldown);

        Inventories.toTag(tag, items);

        return super.toTag(tag);
    }

    // Deserialize the BlockEntity
    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        transferCooldown = tag.getFloat(transferCooldownTag);

        Inventories.fromTag(tag, items);
    }
}
