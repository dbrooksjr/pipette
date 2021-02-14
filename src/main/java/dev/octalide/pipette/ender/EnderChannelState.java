package dev.octalide.pipette.ender;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class EnderChannelState extends PersistentState {
    private ServerWorld world;
    private ArrayList<EnderPipeChannel> channels = new ArrayList<>();
    
    public EnderChannelState(ServerWorld world, String key) {
        super(key);
        this.world = world;
    }

    public ArrayList<EnderPipeChannel> getPlayerChannels(UUID owner) {
        return channels.stream().filter(channel -> channel.getOwner() == owner).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<EnderPipeChannel> getChannels() {
        return channels;
    }

    public void addChannel(EnderPipeChannel channel) {
        channels.add(channel);
    }
    
    public void remove(EnderPipeChannel channel) {
        channels.remove(channel);
    }

    public EnderPipeChannel getOrCreateChannel(ServerWorld world, String name, UUID owner, @Nullable ArrayList<UUID> whitelist) {
        ArrayList<EnderPipeChannel> channels = getPlayerChannels(owner);
        for (EnderPipeChannel channel : channels) {
            if (channel.getName() == name) {
                return channel;
            }
        }

        EnderPipeChannel epc = new EnderPipeChannel(world, name, owner, whitelist);
        addChannel(epc);

        return epc;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        System.out.println("ECS fromTag");
        ListTag list = tag.getList("channels", 10);

        channels.clear();
        list.forEach(channelTag -> channels.add(EnderPipeChannel.fromTag(world, (CompoundTag)channelTag)));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        System.out.println("ECS toTag");
        ListTag list = new ListTag();
        for (EnderPipeChannel channel : channels) {
            System.out.printf("adding channel: %s\n", channel.getName());
            list.add(channel.toTag(tag));
        }

        System.out.println("ECS list created");

        tag.put("channels", list);

        System.out.println("ECS returning tag");

        return tag;
    }

    public static EnderChannelState getState(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(() -> { return new EnderChannelState(world, "pipette_ender_channels"); }, "pipette_ender_channels");
    }
}
