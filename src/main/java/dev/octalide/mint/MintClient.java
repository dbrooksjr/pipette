package dev.octalide.mint;

import dev.octalide.mint.blocks.MBlocks;
import dev.octalide.mint.blocks.PipeFilter;
import dev.octalide.mint.screens.PipeFilterGuiDescription;
import dev.octalide.mint.screens.PipeFilterScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;

public class MintClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(MBlocks.PIPE, RenderLayer.getCutout());

        ScreenRegistry.<PipeFilterGuiDescription, PipeFilterScreen>register(
            Mint.PIPE_FILTER_SCREEN_HANDLER,
            (gui, inventory, title) -> new PipeFilterScreen(gui, inventory.player, title));
    }
}

