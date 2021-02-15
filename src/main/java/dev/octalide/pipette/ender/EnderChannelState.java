package dev.octalide.pipette.ender;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.PersistentState;

public class EnderChannelState extends PersistentState {
    private Map<UUID, EnderChannelManager> managers;
    
    public EnderChannelState(String key) {
        super(key);
        this.managers = new HashMap<>();
    }

    public EnderChannel getOrCreateChannel(UUID owner, String name) {
        EnderChannel channel = getChannel(owner, name);
        if (channel != null) return channel;

        return getOrCreateChannelManager(owner).getOrCreateChannel(name);
    }

    public EnderChannel getChannel(UUID owner, String name) {
        if (channelExists(owner, name)) return getChannelManager(owner).getChannel(name);

        return null;
    }

    public EnderChannelManager getOrCreateChannelManager(UUID owner) {
        EnderChannelManager manager = getChannelManager(owner);
        if (manager != null) return manager;

        manager = new EnderChannelManager();
        managers.put(owner, manager);

        return manager;
    }

    public EnderChannelManager getChannelManager(UUID owner) {
        return managers.get(owner);
    }

    public void createChannelManager(UUID owner) {
        managers.put(owner, new EnderChannelManager());
    }
    
    public void removeChannelManager(UUID owner) {
        managers.remove(owner);
    }

    public boolean channelExists(UUID owner, String name) {
        EnderChannelManager manager = getChannelManager(owner);
        if (manager != null) {
            EnderChannel channel = manager.getChannel(name);
            return channel != null;
        }

        return false;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        CompoundTag managersTag = tag.getCompound("managers");

        managers.clear();
        managersTag.getKeys().forEach(uuid -> {
            EnderChannelManager manager = new EnderChannelManager();
            manager.fromTag(managersTag.getCompound(uuid));

            this.managers.put(UUID.fromString(uuid), manager);
        });
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        CompoundTag managersTag = new CompoundTag();
        this.managers.forEach((uuid, manager) -> managersTag.put(uuid.toString(), manager.toTag(new CompoundTag())));

        tag.put("managers", managersTag);

        return tag;
    }

    @Override
    public boolean isDirty() {
        return true;
    }
}
