package dev.octalide.pipette.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;

public class PipeWrench extends Item {
    public static final String NAME = "pipe_wrench";

    public PipeWrench() {
        super(new FabricItemSettings()
            .maxCount(1)
            .group(ItemGroup.TOOLS)
        );
    }
}
