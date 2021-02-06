package dev.octalide.mint.blockentities;

import dev.octalide.mint.InventoryImpl;
import dev.octalide.mint.blocks.MBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

public class PipeEntity extends BlockEntity implements InventoryImpl, SidedInventory {
    private final String transferCooldownTag = "transfer_cooldown";
    private float transferCooldown = 0;

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public PipeEntity() {
        super(MBlocks.PIPE_ENTITY);
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

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction direction) {
        return direction != Direction.UP;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        // Just return an array of all slots
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }

        return result;
    }
}
