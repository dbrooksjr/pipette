package dev.octalide.pipette.blocks;

import java.util.Arrays;

import dev.octalide.pipette.Pipette;
import dev.octalide.pipette.api.blocks.PipeBase;
import dev.octalide.pipette.api.blocks.properties.PipeSplitterProps;
import dev.octalide.pipette.blockentities.PipeSplitterEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PipeSplitter extends PipeBase {
    public static final String NAME = "pipe_splitter";
    public static final Identifier ID = new Identifier(Pipette.MOD_ID, NAME);

    @Override
    protected boolean canExtend(BlockState state, BlockState other, Direction direction) {
        boolean can = false;

        Block[] linkable = { Blocks.CHEST, Blocks.FURNACE, Blocks.DISPENSER, Blocks.DROPPER, Blocks.TRAPPED_CHEST,
                Blocks.BARREL, Blocks.HOPPER, Blocks.SHULKER_BOX, };

        if (Arrays.stream(linkable).anyMatch(block -> other.getBlock() == block))
            can = true;
        else if (other.getBlock() instanceof PipeBase)
            can = true;

        return can;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new PipeSplitterEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult blockHitResult) {
        return ActionResult.PASS;
    }

    @Override
    public void appendProperties(Builder<Block, BlockState> builder) {
        PipeSplitterProps.appendProperties(builder);
    }

    @Override
    public BlockState buildDefaultState() {
        return PipeSplitterProps.buildDefaultState(this.getDefaultState());
    }
}
