package dev.octalide.pipette.blocks;

import dev.octalide.pipette.Pipette;
import dev.octalide.pipette.blockentities.PipeEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.BlockView;

public class Pipe extends PipeBase {
    public static final String NAME = "pipe";
    public static final Identifier ID = new Identifier(Pipette.MOD_ID, NAME);

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new PipeEntity();
    }
}
