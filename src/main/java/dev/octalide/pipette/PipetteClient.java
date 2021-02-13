package dev.octalide.pipette;

import dev.octalide.pipette.screens.PipeFilterGuiDescription;
import dev.octalide.pipette.screens.PipeFilterScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;

public class PipetteClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(PBlocks.PIPE, RenderLayer.getCutout());

        //noinspection RedundantTypeArguments
        ScreenRegistry.<PipeFilterGuiDescription, PipeFilterScreen>register(
            Pipette.PIPE_FILTER_SCREEN_HANDLER,
            (gui, inventory, title) -> new PipeFilterScreen(gui, inventory.player, title));
    }
}

