package dev.octalide.mint.items;

import dev.octalide.mint.Mint;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MItems {
    public static final TelescopicBeam TELESCOPIC_BEAM = new TelescopicBeam();
    public static final PipeWrench PIPE_WRENCH = new PipeWrench();

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier(Mint.MOD_ID, TelescopicBeam.NAME), TELESCOPIC_BEAM);
        Registry.register(Registry.ITEM, new Identifier(Mint.MOD_ID, PipeWrench.NAME), PIPE_WRENCH);
    }
}
