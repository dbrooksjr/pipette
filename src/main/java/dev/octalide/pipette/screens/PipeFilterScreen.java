package dev.octalide.pipette.screens;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class PipeFilterScreen extends CottonInventoryScreen<PipeFilterGuiDescription> {
    public PipeFilterScreen(PipeFilterGuiDescription gui, PlayerEntity player, Text title) {
        super(gui, player, title);
    }
}
