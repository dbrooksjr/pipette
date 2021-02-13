package dev.octalide.pipette.items;

import dev.octalide.pipette.Pipette;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PItems {
    public static final PipeWrench PIPE_WRENCH = new PipeWrench();

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier(Pipette.MOD_ID, PipeWrench.NAME), PIPE_WRENCH);
    }
}
