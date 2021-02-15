package dev.octalide.pipette.ender;

import java.util.ArrayList;
import java.util.UUID;

import dev.octalide.pipette.api.PipeInventoryImpl;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.collection.DefaultedList;

public class EnderChannel implements PipeInventoryImpl {
    private ArrayList<UUID> whitelist;
    private DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public EnderChannel() {
        this.whitelist = new ArrayList<UUID>();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    public ArrayList<UUID> getWhitelist() {
        return whitelist;
    }

    public void addPlayerToWhitelist(UUID player) {
        whitelist.add(player);
    }

    public void removePlayerFromWhitelist(UUID player) {
        whitelist.remove(player);
    }

    public boolean canPlayerUse(UUID player) {
        return whitelist.contains(player);
    }

    public CompoundTag toTag(CompoundTag tag) {
        ListTag list = new ListTag();
        for (UUID player : whitelist) {
            list.add(NbtHelper.fromUuid(player));
        }

        tag.put("whitelist", list);
        Inventories.toTag(tag, items);

        return tag;
    }

    public EnderChannel fromTag(CompoundTag tag) {
        // 11 is the type for IntArrayTag (UUID)
        tag.getList("whitelist", 11).forEach(uuidTag -> whitelist.add(NbtHelper.toUuid(uuidTag)));

        Inventories.fromTag(tag, items);

        return this;
    }
}
