package dev.octalide.pipette.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Arrays;

import org.jetbrains.annotations.Nullable;

import dev.octalide.pipette.PBlocks;
import dev.octalide.pipette.Pipette;
import dev.octalide.pipette.api.blocks.PipeBase;
import dev.octalide.pipette.blockentities.PipeDisposalEntity;
import dev.octalide.pipette.blockentities.PipeEnderEntity;

public class PipeEnder extends PipeBase {
    public static final String NAME = "pipe_ender";
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

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity entity = world.getBlockEntity(pos);

        if (entity == null) return;
        if (entity instanceof PipeEnderEntity) {
            PipeEnderEntity enderEntity = (PipeEnderEntity) entity;
            enderEntity.setOwner(placer.getUuid());
        }
    }
}
