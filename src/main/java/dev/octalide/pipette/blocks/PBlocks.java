package dev.octalide.pipette.blocks;

import dev.octalide.pipette.blockentities.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class PBlocks {

    public static Pipe PIPE = new Pipe();
    public static PipeExtractor PIPE_EXTRACTOR = new PipeExtractor();
    public static PipeFilter PIPE_FILTER = new PipeFilter();
    public static PipeSplitter PIPE_SPLITTER = new PipeSplitter();
    public static PipeDisposal PIPE_DISPOSAL = new PipeDisposal();

    public static BlockEntityType<PipeEntity> PIPE_ENTITY;
    public static BlockEntityType<PipeExtractorEntity> PIPE_EXTRACTOR_ENTITY;
    public static BlockEntityType<PipeFilterEntity> PIPE_FILTER_ENTITY;
    public static BlockEntityType<PipeSplitterEntity> PIPE_SPLITTER_ENTITY;
    public static BlockEntityType<PipeDisposalEntity> PIPE_DISPOSAL_ENTITY;

    public static void register() {
        // blocks
        PIPE = Registry.register(Registry.BLOCK, Pipe.ID, PIPE);
        PIPE_EXTRACTOR = Registry.register(Registry.BLOCK, PipeExtractor.ID, PIPE_EXTRACTOR);
        PIPE_FILTER = Registry.register(Registry.BLOCK, PipeFilter.ID, PIPE_FILTER);
        PIPE_SPLITTER = Registry.register(Registry.BLOCK, PipeSplitter.ID, PIPE_SPLITTER);
        PIPE_DISPOSAL = Registry.register(Registry.BLOCK, PipeDisposal.ID, PIPE_DISPOSAL);

        // block entities
        PIPE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, Pipe.ID, BlockEntityType.Builder.create(PipeEntity::new, PBlocks.PIPE).build(null));
        PIPE_EXTRACTOR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PipeExtractor.ID, BlockEntityType.Builder.create(PipeExtractorEntity::new, PBlocks.PIPE_EXTRACTOR).build(null));
        PIPE_FILTER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PipeFilter.ID, BlockEntityType.Builder.create(PipeFilterEntity::new, PBlocks.PIPE_FILTER).build(null));
        PIPE_SPLITTER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PipeSplitter.ID, BlockEntityType.Builder.create(PipeSplitterEntity::new, PBlocks.PIPE_SPLITTER).build(null));
        PIPE_DISPOSAL_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PipeDisposal.ID, BlockEntityType.Builder.create(PipeDisposalEntity::new, PBlocks.PIPE_DISPOSAL).build(null));

        // block items
        Registry.register(Registry.ITEM, Pipe.ID, new BlockItem(PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
        Registry.register(Registry.ITEM, PipeExtractor.ID, new BlockItem(PIPE_EXTRACTOR, new FabricItemSettings().group(ItemGroup.REDSTONE)));
        Registry.register(Registry.ITEM, PipeFilter.ID, new BlockItem(PIPE_FILTER, new FabricItemSettings().group(ItemGroup.REDSTONE)));
        Registry.register(Registry.ITEM, PipeSplitter.ID, new BlockItem(PIPE_SPLITTER, new FabricItemSettings().group(ItemGroup.REDSTONE)));
        Registry.register(Registry.ITEM, PipeDisposal.ID, new BlockItem(PIPE_DISPOSAL, new FabricItemSettings().group(ItemGroup.REDSTONE)));
    }
}
