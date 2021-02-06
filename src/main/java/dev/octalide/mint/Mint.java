package dev.octalide.mint;

import dev.octalide.mint.blocks.MBlocks;
import dev.octalide.mint.items.MItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.logging.Logger;

public class Mint implements ModInitializer {
    public static final String MOD_ID = "mint";

    public static final Logger Log = Logger.getLogger(MOD_ID);

    public static final ItemGroup MINT_TAB = FabricItemGroupBuilder.create(
        new Identifier(Mint.MOD_ID, "mint_tab"))
        .icon(() -> new ItemStack(MItems.TELESCOPIC_BEAM))
        .appendItems(stacks -> {
            stacks.add(new ItemStack(MItems.TELESCOPIC_BEAM));
            stacks.add(new ItemStack(MBlocks.PIPE));
        })
        .build();

	@Override
	public void onInitialize() {
		Log.info("Initializing mint...");

		Log.info("Registering items...");
        MItems.register();

        Log.info("Registering blocks...");
        MBlocks.register();

		Log.info("Successfully initialized mint.");
	}
}
