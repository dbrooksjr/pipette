package dev.octalide.pipette.ender;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnderChannelState extends PersistentState {
    private Map<UUID, EnderChannel> channels;
    
    public EnderChannelState(String key) {
        super(key);
        this.channels = new HashMap<>();
    }

    public ArrayList<EnderChannel> getAllChannels() {
        return (ArrayList<EnderChannel>) channels.values();
    }

    public EnderChannel getOrCreateChannel(UUID owner) {
        EnderChannel channel = getChannel(owner);
        if (channel != null) return channel;

        resetChannel(owner);

        return getChannel(owner);
    }

    public EnderChannel getChannel(UUID owner) {
        if (hasChannel(owner)) return channels.get(owner);

        return null;
    }

    public void removeChannel(UUID owner) {
        channels.remove(owner);
    }

    public boolean hasChannel(UUID owner) {
        return channels.get(owner) != null;
    }

    public void resetChannel(UUID owner) {
        channels.put(owner, new EnderChannel(owner));
    }

    @Override
    public void fromTag(CompoundTag tag) {
        CompoundTag channelsTag = tag.getCompound("channels");

        channels.clear();
        channelsTag.getKeys().forEach(uuid -> {
            EnderChannel channel = new EnderChannel(UUID.fromString(uuid));
            channel.fromTag(channelsTag.getCompound(uuid));

            this.channels.put(UUID.fromString(uuid), channel);
        });
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        CompoundTag channelsTag = new CompoundTag();
        this.channels.forEach((uuid, channel) -> channelsTag.put(uuid.toString(), channel.toTag(new CompoundTag())));

        tag.put("channels", channelsTag);

        return tag;
    }

    @Override
    public boolean isDirty() {
        return true;
    }
}
