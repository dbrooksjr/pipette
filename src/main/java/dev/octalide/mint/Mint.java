package dev.octalide.mint;

import dev.octalide.mint.items.MItems;
import net.fabricmc.api.ModInitializer;

public class Mint implements ModInitializer {

    public static final String MOD_ID = "mint";

	@Override
	public void onInitialize() {
		System.out.println("Initializing mint...");

		System.out.println("Registering items...");
        MItems.init();

		System.out.println("Successfully initialized mint.");
	}
}
