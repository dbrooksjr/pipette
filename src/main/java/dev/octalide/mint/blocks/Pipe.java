package dev.octalide.mint.blocks;

import dev.octalide.mint.Mint;
import dev.octalide.mint.blockentities.PipeEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.BlockView;

public class Pipe extends PipeBase {
    public static final String NAME = "pipe";
    public static final Identifier ID = new Identifier(Mint.MOD_ID, NAME);

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new PipeEntity();
    }
}
