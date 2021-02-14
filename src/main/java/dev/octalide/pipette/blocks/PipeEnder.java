package dev.octalide.pipette.blocks;

import java.util.Arrays;

import dev.octalide.pipette.Pipette;
import dev.octalide.pipette.api.blocks.PipeBase;
import dev.octalide.pipette.api.blocks.properties.PipeEnderProps;
import dev.octalide.pipette.api.blocks.properties.PipeExtractorProps;
import dev.octalide.pipette.api.blocks.properties.PipeProps;
import dev.octalide.pipette.blockentities.PipeEnderEntity;
import dev.octalide.pipette.ender.EnderPipeChannel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PipeEnder extends PipeBase {
    public static final String NAME = "pipe_ender";
    public static final Identifier ID = new Identifier(Pipette.MOD_ID, NAME);

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new PipeEnderEntity();
    }

    @Override
    protected boolean canExtend(BlockState state, BlockState other, Direction direction) {
        boolean can = false;

        DirectionProperty[] props = { Properties.FACING, Properties.HORIZONTAL_FACING, Properties.HOPPER_FACING };

        if (other.getBlock() == Blocks.HOPPER)
            can = Arrays.stream(props).anyMatch(directionProperty -> other.contains(directionProperty)
                    && other.get(directionProperty) == direction.getOpposite());

        else if (other.getBlock() instanceof PipeExtractor)
            can = other.get(PipeExtractorProps.output) == direction.getOpposite()
                    || other.get(PipeExtractorProps.input) == direction.getOpposite();
        else if (other.getBlock() instanceof Pipe)
            can = other.get(PipeProps.output) == direction.getOpposite();
        else if (other.getBlock() instanceof PipeSplitter)
            can = true;

        return can;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult blockHitResult) {
        return ActionResult.PASS;
    }

    @Override
    public void appendProperties(Builder<Block, BlockState> builder) {
        PipeEnderProps.appendProperties(builder);
    }

    @Override
    public BlockState buildDefaultState() {
        return PipeEnderProps.buildDefaultState(this.getDefaultState());
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState state = super.getPlacementState(context);
        
        BlockEntity entity = context.getWorld().getBlockEntity(context.getBlockPos());
        if (entity != null && entity instanceof PipeEnderEntity) {
            PipeEnderEntity enderEntity = (PipeEnderEntity) entity;

            enderEntity.setOwner(context.getPlayer().getUuid());
            enderEntity.setChannel(EnderPipeChannel.getOrCreate((ServerWorld) context.getWorld(), "default", context.getPlayer().getUuid(), null));
        } else {
            System.out.println("entity does not exist!!");
        }

        return state;
    }
}
