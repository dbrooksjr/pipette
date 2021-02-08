package dev.octalide.mint.blockentities;

import alexiil.mc.lib.attributes.SearchOptions;
import alexiil.mc.lib.attributes.item.ItemAttributes;
import alexiil.mc.lib.attributes.item.ItemExtractable;
import alexiil.mc.lib.attributes.item.ItemInsertable;
import alexiil.mc.lib.attributes.item.ItemInvUtil;
import alexiil.mc.lib.attributes.item.compat.FixedInventoryVanillaWrapper;
import alexiil.mc.lib.attributes.item.impl.RejectingItemInsertable;
import dev.octalide.mint.blocks.MBlocks;
import dev.octalide.mint.blocks.Pipe;
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

public class PipeFilterEntity extends PipeEntityBase implements ExtendedScreenHandlerFactory {
    public PipeFilterEntity() {
        super(MBlocks.PIPE_FILTER_ENTITY);
        this.items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    }

    @Override
    protected boolean attemptOutput() {
        if (world == null) return false;

        ItemStack stack = getStack(0);
        if (stack == null) return false;

        if (getCachedState().get(PipeBase.Props.powered)) return false;

        Direction outputDirection = getCachedState().get(Pipe.Props.facing);
        Inventory outputInventory = HopperBlockEntity.getInventoryAt(world, pos.offset(outputDirection));

        if (outputInventory != null) {
            ItemStack stackCopy = this.getStack(0).copy();
            ItemStack ret = HopperBlockEntity.transfer(this, outputInventory, this.removeStack(0, 1), outputDirection.getOpposite());

            if (ret.isEmpty()) {
                outputInventory.markDirty();
                return true;
            }

            this.setStack(0, stackCopy);
        } else {
            ItemInsertable insertable = ItemAttributes.INSERTABLE.get(world, pos.offset(outputDirection), SearchOptions.inDirection(outputDirection));
            if (insertable == RejectingItemInsertable.NULL) {
                return false;
            }

            ItemExtractable extractable = new FixedInventoryVanillaWrapper(this).getExtractable();

            return ItemInvUtil.move(extractable, insertable, 1) > 0;
        }

        return false;
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
