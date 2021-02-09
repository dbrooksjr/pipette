package dev.octalide.mint.blockentities;

import dev.octalide.mint.blocks.PipeBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;

import java.util.stream.IntStream;

public abstract class PipeExtractorEntityBase extends PipeEntityBase {
    public static int INPUT_COOLDOWN_MAX = 0;
    private int inputCooldown = 0;

    public PipeExtractorEntityBase(BlockEntityType type) {
        super(type);
    }

    protected boolean attemptInput() {
        if (world == null || world.isClient()) return false;
        if (!this.isEmpty()) return false;
        if (getCachedState().get(PipeBase.Props.powered)) return false;

        Direction facing = getCachedState().get(PipeBase.Props.facing);
        Direction target = facing.getOpposite();

        Inventory input = HopperBlockEntity.getInventoryAt(world, pos.offset(target));

        if (input == null) return false;
        if (isInventoryEmpty(input, target)) return false;

        return getAvailableSlots(input, target).anyMatch(i -> extract(input, this, i, facing));
    }

    protected boolean extract(Inventory from, Inventory to, int slot, Direction direction) {
        ItemStack stack = from.getStack(slot).copy();
        if (stack.isEmpty() || !canExtract(from, stack, slot, direction)) return false;

        ItemStack result = HopperBlockEntity.transfer(from, to, from.removeStack(slot, 1), null);
        if (result.isEmpty()) {
            to.markDirty();
            return true;
        }

        from.setStack(slot, stack);
        return false;
    }

    protected static IntStream getAvailableSlots(Inventory inventory, Direction side) {
        return inventory instanceof SidedInventory ? IntStream.of(((SidedInventory)inventory).getAvailableSlots(side)) : IntStream.range(0, inventory.size());
    }

    protected static boolean canExtract(Inventory inv, ItemStack stack, int slot, Direction facing) {
        return !(inv instanceof SidedInventory) || ((SidedInventory)inv).canExtract(slot, stack, facing);
    }

    protected static boolean isInventoryEmpty(Inventory inv, Direction facing) {
        return getAvailableSlots(inv, facing).allMatch((i) -> inv.getStack(i).isEmpty());
    }

    @Override
    public void tick() {
        if (world == null || world.isClient()) return;

        super.tick();

        inputCooldown--;

        if (inputCooldown > 0) return;
        inputCooldown = 0;

        if (attemptInput()) {
            inputCooldown = INPUT_COOLDOWN_MAX;
            markDirty();
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag = super.toTag(tag);

        tag.putFloat("input_cooldown", inputCooldown);

        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        inputCooldown = tag.getInt("input_cooldown");
    }
}
