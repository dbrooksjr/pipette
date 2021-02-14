package dev.octalide.pipette.api.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

import java.util.Map.Entry;

import org.jetbrains.annotations.Nullable;

import dev.octalide.pipette.api.PipeInventoryImpl;
import dev.octalide.pipette.api.blocks.properties.PipeExtractorProps;
import dev.octalide.pipette.api.blocks.properties.PipeProps;
import dev.octalide.pipette.blocks.PipeExtractor;

public abstract class PipeEntityBase extends BlockEntity implements PipeInventoryImpl, Tickable {
    public int OUTPUT_COOLDOWN_MAX = 0;
    protected int outputCooldown = 0;

    protected DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public PipeEntityBase(BlockEntityType<?> type) {
        super(type);
    }

    protected boolean attemptOutput() {
        if (world == null || world.isClient())
            return false;
        if (getCachedState().get(PipeProps.powered))
            return false;
        if (this.isEmpty())
            return false;

        // this was implemented as a solution for pipe-to-extractor connections
        // causing a sort of "race condition" between inventories. If a pipe has
        // an extractor connected to it, the pipe will manually trigger the
        // extractor's output then input cycles, causing it to attempt to pull
        // the pipe's item BEFORE the pipe transfers to it's output.
        // Note that the pipe will select the *first* extractor it finds and
        // does NOT split output between multiple extractors.
        PipeExtractorEntityBase extractor = getExtractor();
        if (extractor != null) {
            extractor.attemptOutput();
            if (extractor.attemptInput())
                return true;
        }

        Direction output = getCachedState().get(PipeProps.output);

        Inventory outputInventory = HopperBlockEntity.getInventoryAt(world, pos.offset(output));
        if (outputInventory == null)
            return false;

        return transfer(this, outputInventory, output.getOpposite());
    }

    protected PipeExtractorEntityBase getExtractor() {
        for (Entry<Direction, BooleanProperty> extension : PipeProps.extensions.entrySet()) {
            BlockState state = world.getBlockState(pos.offset(extension.getKey()));

            if (state.getBlock() instanceof PipeExtractor) {
                // there is an extractor pipe connected
                if (state.get(PipeExtractorProps.input).getOpposite() == extension.getKey()) {
                    // the pipes's input is this pipe
                    BlockEntity entity = world.getBlockEntity(pos.offset(extension.getKey()));

                    if (entity instanceof PipeExtractorEntityBase)
                        return (PipeExtractorEntityBase) entity;
                }
            }
        }

        return null;
    }

    protected static boolean transfer(Inventory from, Inventory to, Direction direction) {
        ItemStack stack = from.getStack(0).copy();

        ItemStack result = HopperBlockEntity.transfer(from, to, from.removeStack(0, 1), direction);
        if (result.isEmpty()) {
            to.markDirty();
            return true;
        }

        from.setStack(0, stack);
        return false;
    }

    @Override
    public void tick() {
        if (world == null || world.isClient())
            return;
        if (this.isEmpty()) {
            outputCooldown = OUTPUT_COOLDOWN_MAX;
            return;
        }

        outputCooldown--;

        if (outputCooldown > 0)
            return;
        outputCooldown = 0;

        if (attemptOutput()) {
            outputCooldown = OUTPUT_COOLDOWN_MAX;
            markDirty();
        }
    }

    @Override
    public int getMaxCountPerStack() {
        return 1;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isEmpty();
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return this.getCachedState().get(PipeProps.output) != dir;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        Inventories.fromTag(tag, items);
        outputCooldown = tag.getInt("output_cooldown");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Inventories.toTag(tag, items);
        tag.putInt("output_cooldown", outputCooldown);
        return super.toTag(tag);
    }
}
