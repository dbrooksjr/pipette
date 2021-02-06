package dev.octalide.mint.items;

import dev.octalide.mint.Mint;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MItems {
    public static final TelescopicBeam TELESCOPIC_BEAM = new TelescopicBeam();

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier(Mint.MOD_ID, "telescopic_beam"), TELESCOPIC_BEAM);
    }
}
