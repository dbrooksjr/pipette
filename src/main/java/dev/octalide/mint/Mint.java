package dev.octalide.mint;

import dev.octalide.mint.blocks.MBlocks;
import dev.octalide.mint.blocks.PipeFilter;
import dev.octalide.mint.items.MItems;
import dev.octalide.mint.screens.PipeFilterGuiDescription;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class Mint implements ModInitializer {
    public static final String MOD_ID = "mint";

    public static final ScreenHandlerType<PipeFilterGuiDescription> PIPE_FILTER_SCREEN_HANDLER =
        ScreenHandlerRegistry.registerExtended(
            PipeFilter.ID,
            (syncId, inventory, buf) -> new PipeFilterGuiDescription(
                syncId,
                inventory,
                ScreenHandlerContext.create(inventory.player.world, buf.readBlockPos())));

    public static final ItemGroup MINT_TAB = FabricItemGroupBuilder.create(
        new Identifier(Mint.MOD_ID, "mint_tab"))
        .icon(() -> new ItemStack(MItems.TELESCOPIC_BEAM))
        .appendItems(stacks -> {
            stacks.add(new ItemStack(MItems.PIPE_WRENCH));
            stacks.add(new ItemStack(MBlocks.PIPE));
            stacks.add(new ItemStack(MBlocks.PIPE_EXTRACTOR));
            stacks.add(new ItemStack(MBlocks.PIPE_FILTER));
            stacks.add(new ItemStack(MItems.TELESCOPIC_BEAM));
        })
        .build();

    @Override
    public void onInitialize() {
        System.out.println("Initializing mint...");

        System.out.println("Registering items...");
        MItems.register();

        System.out.println("Registering blocks...");
        MBlocks.register();

        System.out.println("Successfully initialized mint.");
    }
}
