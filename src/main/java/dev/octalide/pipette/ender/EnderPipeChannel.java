package dev.octalide.pipette.ender;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class EnderPipeChannel {
    private UUID owner;
    private ArrayList<UUID> whitelist = new ArrayList<>();

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public ArrayList<UUID> getWhitelist() {
        return whitelist;
    }

    public boolean canPlayerUse(UUID player) {
        return player == owner || whitelist.contains(player);
    }

    public CompoundTag toTag(CompoundTag tag) {
        ListTag list = whitelist.stream().map(NbtHelper::fromUuid).collect(Collectors.toCollection(ListTag::new));

        tag.put("whitelist", list);
        tag.putUuid("owner", owner);

        return tag;
    }

    public static EnderPipeChannel fromTag(CompoundTag tag) {
        EnderPipeChannel epc = new EnderPipeChannel();
        
        // 11 is the type for IntArrayTag
        ListTag list = tag.getList("whitelist", 11);
        list.forEach(uuidTag -> epc.whitelist.add(NbtHelper.toUuid(uuidTag)));

        epc.owner = tag.getUuid("owner");

        return epc;
    }
}
