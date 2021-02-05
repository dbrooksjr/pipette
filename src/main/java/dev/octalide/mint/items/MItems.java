package dev.octalide.mint.items;

import dev.octalide.mint.Mint;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MItems {
    public static TelescopicBeam TELESCOPIC_BEAM;

    public static final ItemGroup TAB_GROUP = FabricItemGroupBuilder.create(
        new Identifier(Mint.MOD_ID, "mint_tab"))
        .icon(() -> new ItemStack(TELESCOPIC_BEAM))
        .appendItems(stacks -> {
            stacks.add(new ItemStack(TELESCOPIC_BEAM));
        })
        .build();

    public static void init() {
        TELESCOPIC_BEAM = new TelescopicBeam();
        register();
    }

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier(Mint.MOD_ID, "telescopic_beam"), TELESCOPIC_BEAM);
    }
}
