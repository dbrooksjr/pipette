package dev.octalide.mint.blocks;

import dev.octalide.mint.blockentities.PipeEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class MBlocks {

    public static Pipe PIPE = new Pipe();

    public static BlockEntityType<PipeEntity> PIPE_ENTITY;

    public static void register() {
        // blocks
        Registry.register(Registry.BLOCK, Pipe.ID, PIPE);

        // block entities
        PIPE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, Pipe.ID, BlockEntityType.Builder.create(PipeEntity::new, MBlocks.PIPE).build(null));

        // items
        Registry.register(Registry.ITEM, Pipe.ID, new BlockItem(PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
    }
}
