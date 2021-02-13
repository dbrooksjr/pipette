package dev.octalide.pipette;

import dev.octalide.pipette.blocks.PBlocks;
import dev.octalide.pipette.blocks.PipeFilter;
import dev.octalide.pipette.items.PItems;
import dev.octalide.pipette.screens.PipeFilterGuiDescription;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class Pipette implements ModInitializer {
    public static final String MOD_ID = "pipette";

    public static final ScreenHandlerType<PipeFilterGuiDescription> PIPE_FILTER_SCREEN_HANDLER =
        ScreenHandlerRegistry.registerExtended(
            PipeFilter.ID,
            (syncId, inventory, buf) -> new PipeFilterGuiDescription(
                syncId,
                inventory,
                ScreenHandlerContext.create(inventory.player.world, buf.readBlockPos())));

    public static final ItemGroup MINT_TAB = FabricItemGroupBuilder.create(
        new Identifier(Pipette.MOD_ID, "pipette_tab"))
        .icon(() -> new ItemStack(PItems.PIPE_WRENCH))
        .appendItems(stacks -> {
            stacks.add(new ItemStack(PItems.PIPE_WRENCH));
            stacks.add(new ItemStack(PBlocks.PIPE));
            stacks.add(new ItemStack(PBlocks.PIPE_EXTRACTOR));
            stacks.add(new ItemStack(PBlocks.PIPE_FILTER));
            stacks.add(new ItemStack(PBlocks.PIPE_SPLITTER));
            stacks.add(new ItemStack(PBlocks.PIPE_DISPOSAL));
        })
        .build();

    @Override
    public void onInitialize() {
        System.out.println("Initializing pipette...");

        System.out.println("Registering items...");
        PItems.register();

        System.out.println("Registering blocks...");
        PBlocks.register();
    }
}
