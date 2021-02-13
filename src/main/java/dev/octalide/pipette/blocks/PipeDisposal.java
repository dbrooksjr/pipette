package dev.octalide.pipette.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

import java.util.Arrays;

import dev.octalide.pipette.Pipette;
import dev.octalide.pipette.blockentities.PipeDisposalEntity;

public class PipeDisposal extends PipeBase {
    public static final String NAME = "pipe_disposal";
    public static final Identifier ID = new Identifier(Pipette.MOD_ID, NAME);

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new PipeDisposalEntity();
    }

    protected boolean canExtend(BlockState state, BlockState other, Direction direction) {
        boolean can = false;

        DirectionProperty[] props = {
            Properties.FACING,
            Properties.HORIZONTAL_FACING,
            Properties.HOPPER_FACING
        };

        if (other.getBlock() == Blocks.HOPPER) {
            can = Arrays.stream(props).anyMatch(directionProperty ->
                other.contains(directionProperty) && other.get(directionProperty) == direction.getOpposite());

        } else if (other.getBlock() == PBlocks.PIPE) {
            can = other.get(Properties.FACING) == direction.getOpposite();

        } else if (other.getBlock() == PBlocks.PIPE_EXTRACTOR) {
            can = other.get(Properties.FACING) == direction.getOpposite() || other.get(Properties.FACING) == direction;

        } else if (other.getBlock() == PBlocks.PIPE_FILTER) {
            can = other.get(Properties.FACING) == direction.getOpposite() || other.get(Properties.FACING) == direction;

        } else if (other.getBlock() == PBlocks.PIPE_SPLITTER) {
            can = true;
        }

        return can;
    }

    /*
    Fun idea, but it looks a little odd...

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        double x = (double) pos.getX() + random.nextDouble();
        double y = (double) pos.getY() + random.nextDouble();
        double z = (double) pos.getZ() + random.nextDouble();

        world.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);

        super.randomDisplayTick(state, world, pos, random);
    }
    */
}
