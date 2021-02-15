package dev.octalide.pipette.ender;

import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;

public class EnderChannelManager {
    private Map<String, EnderChannel> channels;

    public EnderChannelManager() {
        this.channels = new HashMap<>();
    }

    public EnderChannel getOrCreateChannel(String name) {
        if (channels.containsKey(name)) return channels.get(name);

        addChannel(name);

        return getChannel(name);
    }

    public EnderChannel getChannel(String name) {
        return channels.get(name);
    }

    public void removeChannel(String name) {
        channels.remove(name);
    }

    public void addChannel(String name) {
        channels.put(name, new EnderChannel());
    }

    public void fromTag(CompoundTag tag) {
        this.channels.clear();

        CompoundTag channelsTag = tag.getCompound("channels");
        channelsTag.getKeys().forEach(name -> {
            EnderChannel channel = new EnderChannel();
            channel.fromTag(channelsTag.getCompound(name));

            this.channels.put(name, channel);
        });
    }

    public CompoundTag toTag(CompoundTag tag) {
        CompoundTag channelsTag = new CompoundTag();
        this.channels.forEach((name, channel) -> channelsTag.put(name, channel.toTag(new CompoundTag())));

        tag.put("channels", channelsTag);

        return tag;
    }
}
