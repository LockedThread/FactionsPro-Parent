package dev.lockedthread.factionspro.configs.serializer;

import org.bukkit.configuration.ConfigurationSection;

public interface Serializer<V> {

    Class<V> getValueClass();

    void serialize(ConfigurationSection section, V v);

    V deserialize(ConfigurationSection section);
}
