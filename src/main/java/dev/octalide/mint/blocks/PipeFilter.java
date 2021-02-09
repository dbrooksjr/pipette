package dev.octalide.mint.blocks;

import dev.octalide.mint.Mint;
import dev.octalide.mint.blockentities.PipeFilterEntity;
import dev.octalide.mint.items.MItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PipeFilter extends PipeExtractor {
    public static final String NAME = "pipe_filter";
    public static final Identifier ID = new Identifier(Mint.MOD_ID, NAME);

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        ActionResult result = super.onUse(state, world, pos, player, hand, blockHitResult);

        if (!player.isHolding(MItems.PIPE_WRENCH)) {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));

            result = ActionResult.SUCCESS;
        }

        return result;
    }

    @Override
    protected boolean canExtend(BlockState state, BlockState other, Direction direction) {
        boolean can = super.canExtend(state, other, direction);

        if (direction == state.get(Properties.FACING).getOpposite()) can = true;

        return can;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new PipeFilterEntity();
    }
}
