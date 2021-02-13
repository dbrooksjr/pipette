package dev.octalide.pipette.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class TelescopicBeam extends Item {
    public static final String NAME = "telescopic_beam";

    public TelescopicBeam() {
        super(new FabricItemSettings()
            .group(ItemGroup.MATERIALS)
        );
    }
}
