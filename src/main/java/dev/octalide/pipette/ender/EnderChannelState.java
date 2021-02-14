package dev.octalide.pipette.ender;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class EnderChannelState extends PersistentState {
    private ServerWorld serverWorld;
    private ArrayList<EnderPipeChannel> channels = new ArrayList<>();
    
    public EnderChannelState(ServerWorld world, String key) {
        super(key);
        serverWorld = world;
    }
    
    public static EnderChannelState get(ServerWorld world) {
        return new EnderChannelState(world, "pipette_ender_channels");
    }
    
    public ArrayList<EnderPipeChannel> getPlayerChannels(UUID playerUuid) {
        return channels.stream().filter(channel -> channel.getOwner() == playerUuid).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void fromTag(CompoundTag tag) {
        ListTag list = tag.getList("channels", 10);

        channels.clear();
        list.forEach(channelTag -> channels.add(EnderPipeChannel.fromTag((CompoundTag)channelTag)));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        ListTag list = channels.stream().map(channel -> channel.toTag(tag)).collect(Collectors.toCollection(ListTag::new));

        tag.put("channels", list);

        return tag;
    }

    public static PersistentState getEnderChannelState(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(() -> { return get(world); }, "pipette_ender_channels");
    }
}
