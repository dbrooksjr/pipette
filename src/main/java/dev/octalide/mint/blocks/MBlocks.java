package dev.octalide.mint.blocks;

import dev.octalide.mint.blockentities.PipeEntity;
import dev.octalide.mint.blockentities.PipeExtractorEntity;
import dev.octalide.mint.blockentities.PipeFilterEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class MBlocks {

    public static Pipe PIPE;
    public static PipeExtractor PIPE_EXTRACTOR;
    public static PipeFilter PIPE_FILTER;

    public static BlockEntityType<PipeEntity> PIPE_ENTITY;
    public static BlockEntityType<PipeExtractorEntity> PIPE_EXTRACTOR_ENTITY;
    public static BlockEntityType<PipeFilterEntity> PIPE_FILTER_ENTITY;

    public static void register() {
        // blocks
        PIPE = Registry.register(Registry.BLOCK, Pipe.ID, PIPE);
        PIPE_EXTRACTOR = Registry.register(Registry.BLOCK, PipeExtractor.ID, PIPE_EXTRACTOR);
        PIPE_FILTER = Registry.register(Registry.BLOCK, PipeFilter.ID, PIPE_FILTER);

        // block entities
        PIPE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, Pipe.ID, BlockEntityType.Builder.create(PipeEntity::new, MBlocks.PIPE).build(null));
        PIPE_EXTRACTOR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PipeExtractor.ID, BlockEntityType.Builder.create(PipeExtractorEntity::new, MBlocks.PIPE_EXTRACTOR).build(null));
        PIPE_FILTER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PipeFilter.ID, BlockEntityType.Builder.create(PipeFilterEntity::new, MBlocks.PIPE_FILTER).build(null));

        // block items
        Registry.register(Registry.ITEM, Pipe.ID, new BlockItem(PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
        Registry.register(Registry.ITEM, PipeExtractor.ID, new BlockItem(PIPE_EXTRACTOR, new FabricItemSettings().group(ItemGroup.REDSTONE)));
        Registry.register(Registry.ITEM, PipeFilter.ID, new BlockItem(PIPE_FILTER, new FabricItemSettings().group(ItemGroup.REDSTONE)));
    }

    static {
        PIPE = new Pipe();
        PIPE_EXTRACTOR = new PipeExtractor();
        PIPE_FILTER = new PipeFilter();
    }
}
