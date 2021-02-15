package dev.octalide.pipette.ender;

import dev.octalide.pipette.api.PipeInventoryImpl;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

import java.util.UUID;

public class EnderChannel implements PipeInventoryImpl {
    private UUID owner;
    private DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public EnderChannel(UUID owner) {
        this.owner = owner;
    }

    public UUID getOwner() {
        return owner;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    public CompoundTag toTag(CompoundTag tag) {
        tag.putUuid("owner", owner);
        Inventories.toTag(tag, items);

        return tag;
    }

    public EnderChannel fromTag(CompoundTag tag) {
        owner = tag.getUuid("owner");
        Inventories.fromTag(tag, items);

        return this;
    }
}
