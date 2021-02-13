package dev.octalide.pipette;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public interface PipeInventoryImpl extends SidedInventory {
    DefaultedList<ItemStack> getItems();

    static PipeInventoryImpl of(DefaultedList<ItemStack> items) {
        return () -> items;
    }

    static PipeInventoryImpl ofSize(int size) {
        return of(DefaultedList.ofSize(size, ItemStack.EMPTY));
    }

    default int getMaxCountPerStack() {
        return 1;
    }

    default int size() {
        return getItems().size();
    }

    default boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = getStack(i);

            if (!stack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    default ItemStack getStack(int slot) {
        return getItems().get(slot);
    }

    default ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(getItems(), slot, amount);
        if (!result.isEmpty()) {
            markDirty();
        }

        return result;
    }

    default ItemStack removeStack(int slot) {
        return Inventories.removeStack(getItems(), slot);
    }

    default void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);

        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
    }

    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    default void clear() {
        getItems().clear();
    }

    default void markDirty() {

    }

    default int[] getAvailableSlots(Direction side) {
        return IntStream.range(0, this.size()).toArray();
    }

    default boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return true;
    }

    default boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }
}
