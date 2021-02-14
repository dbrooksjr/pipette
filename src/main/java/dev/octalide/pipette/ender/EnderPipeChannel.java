package dev.octalide.pipette.ender;

import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import dev.octalide.pipette.api.PipeInventoryImpl;

public class EnderPipeChannel implements PipeInventoryImpl {
    private ServerWorld world;

    private String name;
    private UUID owner;
    private ArrayList<UUID> whitelist = new ArrayList<>();

    protected DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public EnderPipeChannel(ServerWorld world, String name, UUID owner, @Nullable ArrayList<UUID> whitelist) {
        this.world = world;
        this.name = name;
        this.owner = owner;
        this.whitelist = whitelist != null ? whitelist : new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
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
        return player == owner || whitelist.contains(player);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    public void remove() {
        EnderChannelState ecs = EnderChannelState.getState(world);
        ecs.remove(this);
    }

    @Override
    public void markDirty() {
        EnderChannelState.getState(world).markDirty();
    }

    public CompoundTag toTag(CompoundTag tag) {
        System.out.println("EPC toTag");

        ListTag list = new ListTag();
        for (UUID player : whitelist) {
            System.out.printf("adding whitelisted player: %s\n", player);
            list.add(NbtHelper.fromUuid(player));
        }

        System.out.println("EPC list created");

        tag.putString("name", name);
        tag.putUuid("owner", owner);
        tag.put("whitelist", list);
        Inventories.toTag(tag, items);

        System.out.println("EPC returning tag");

        return tag;
    }

    public static EnderPipeChannel getOrCreate(ServerWorld world, String name, UUID owner, @Nullable ArrayList<UUID> whitelist) {
        return EnderChannelState.getState(world).getOrCreateChannel(world, name, owner, whitelist);
    }

    public static EnderPipeChannel fromTag(ServerWorld world, CompoundTag tag) {
        System.out.println("EPC fromTag");
        String name;
        UUID owner;
        ArrayList<UUID> whitelist = new ArrayList<>();
        
        name = tag.getString("name");
        owner = tag.getUuid("owner");

        // 11 is the type for IntArrayTag
        tag.getList("whitelist", 11).forEach(uuidTag -> whitelist.add(NbtHelper.toUuid(uuidTag)));

        EnderPipeChannel ecs = new EnderPipeChannel(world, name, owner, whitelist);
        Inventories.fromTag(tag, ecs.items);

        return ecs;
    }
}
