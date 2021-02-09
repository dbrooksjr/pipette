package dev.octalide.mint.screens;

import dev.octalide.mint.Mint;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPlayerInvPanel;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

public class PipeFilterGuiDescription extends SyncedGuiDescription {
    private static final int INVENTORY_SIZE = 2;

    public PipeFilterGuiDescription(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
        super(Mint.PIPE_FILTER_SCREEN_HANDLER, syncId, inventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context));

        WGridPanel root = new WGridPanel();

        WItemSlot filterSlot = WItemSlot.of(blockInventory, 1);
        WPlayerInvPanel invPanel = this.createPlayerInventoryPanel();

        root.add(filterSlot, 4, 1);
        root.add(invPanel, 0, 2);

        setRootPanel(root);
        root.validate(this);
    }
}
