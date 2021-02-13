package dev.octalide.pipette.blockentities;

import dev.octalide.pipette.blocks.Destroyer;
import dev.octalide.pipette.blocks.PBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;

public class DestroyerEntity extends BlockEntity implements Tickable {
    private static final int DELAY_MAX = 8;
    private static final int PROGRESS_MAX = 9;
    private static final int PROGRESS_MIN = -1;
    private static final Tag.Identified<Block> BLACKLIST = BlockTags.WITHER_IMMUNE;
    //private static final Tag.Identified<Block> BLACKLIST = BlockTags.DRAGON_IMMUNE;

    private int progress;
    private int delay;

    public DestroyerEntity() {
        super(PBlocks.DESTROYER_ENTITY);
    }

    private boolean canMine(BlockState target) {
        if (world == null || world.isClient()) return false;

        if (!getCachedState().get(Destroyer.Props.powered)) return false;
        if (target.getBlock() == Blocks.AIR) return false;
        if (target.getBlock().isIn(BLACKLIST)) return false;
        if (target.getMaterial() == Material.WATER || target.getMaterial() == Material.LAVA) return false;

        return true;
    }

    @Override
    public void tick() {
        if (world == null) return;

        BlockPos targetPos = pos.offset(getCachedState().get(Destroyer.Props.facing));
        BlockState target = world.getBlockState(targetPos);

        if (canMine(target)) {
            delay--;
            if (delay < 0) {
                progress++;

                if (progress > PROGRESS_MAX) {
                    world.breakBlock(targetPos, true);
                    world.playSound(null, targetPos, target.getSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 1, 1);
                    progress = PROGRESS_MIN;
                } else {
                    world.playSound(null, targetPos, target.getSoundGroup().getHitSound(), SoundCategory.BLOCKS, 1, 1);
                }

                delay = DELAY_MAX;
            }
        } else {
            delay = DELAY_MAX;
            progress = PROGRESS_MIN;
        }


        /*
        Can't figure out how to display the progress for multiple destroyers.
        This is an issue where the client seems to only save one block breaking state per player entity.
        might be possible to display breaking info through a mixin or something, but for now, just gonna let it be :(

        You'd think this would work...
        if (world.isClient()) {
            world.getPlayers().forEach(playerEntity -> {
                world.setBlockBreakingInfo(playerEntity.getEntityId(), targetPos, progress);
            });
        }
        */
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        progress = tag.getInt("progress");
        delay = tag.getInt("delay");

        super.fromTag(state, tag);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag = super.toTag(tag);
        tag.putInt("progress", progress);
        tag.putInt("delay", delay);

        return tag;
    }
}
