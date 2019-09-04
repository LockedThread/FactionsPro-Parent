package dev.lockedthread.factionspro.configs.serializer.types;

import dev.lockedthread.factionspro.configs.serializer.Serializer;
import org.bukkit.configuration.ConfigurationSection;

public interface YamlSerializer<T> extends Serializer<T, ConfigurationSection> {
}
