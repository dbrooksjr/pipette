package dev.octalide.pipette.blockentities;

import dev.octalide.pipette.PBlocks;
import dev.octalide.pipette.api.blockentities.PipeEntityBase;
import dev.octalide.pipette.api.blocks.properties.PipeProps;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class PipeEntity extends PipeEntityBase {
    public PipeEntity() {
        super(PBlocks.PIPE_ENTITY);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return this.getCachedState().get(PipeProps.output) != dir;
    }
}
