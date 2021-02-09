package dev.octalide.mint.blockentities;

import dev.octalide.mint.blocks.MBlocks;
import dev.octalide.mint.blocks.PipeBase;
import dev.octalide.mint.screens.PipeFilterGuiDescription;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

public class PipeFilterEntity extends PipeExtractorEntityBase implements ExtendedScreenHandlerFactory {
    public PipeFilterEntity() {
        super(MBlocks.PIPE_FILTER_ENTITY);
        this.items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    }

    private boolean matchesFilter(ItemStack itemStack) {
        ItemStack filterStack = items.get(1);
        if (filterStack.isEmpty() || itemStack.isEmpty()) return true;

        return itemStack.getItem() == filterStack.getItem();
    }

    @Override
    protected boolean extract(Inventory from, Inventory to, int slot, Direction direction) {
        if (!matchesFilter(from.getStack(slot).copy())) return false;

        return super.extract(from, to, slot, direction);
    }

    @Override
    public boolean isEmpty() {
        return this.items.get(0).isEmpty();
    }

    @Override
    public int size() {
        return super.size() - 1;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
        return new PipeFilterGuiDescription(syncId, inventory, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
        packetByteBuf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }
}
