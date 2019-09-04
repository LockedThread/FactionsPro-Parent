package dev.lockedthread.factionspro.configs.serializer.registery;

import dev.lockedthread.factionspro.configs.serializer.Serializer;
import dev.lockedthread.factionspro.structure.enums.Relation;
import dev.lockedthread.factionspro.structure.enums.Role;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SerializerRegistry {

    private static SerializerRegistry instance;

    private final Map<Class<?>, Serializer<?>> serializerMap = new HashMap<>();

    private SerializerRegistry() {
        /* Default registrars */

        register(Role.class, new Serializer<Role>() {
            @Override
            public Class<Role> getValueClass() {
                return Role.class;
            }

            @Override
            public void serialize(ConfigurationSection section, Role role) {
                section.set("name", role.getName());
                section.set("icon", role.getIcon());
            }

            @Override
            public Role deserialize(ConfigurationSection section) {
                Role role = Role.valueOf(section.getName().toUpperCase());
                role.setIcon(section.getString("icon"));
                role.setName(section.getString("name"));
                return role;
            }
        });

        register(Relation.class, new Serializer<Relation>() {
            @Override
            public Class<Relation> getValueClass() {
                return Relation.class;
            }

            @Override
            public void serialize(ConfigurationSection section, Relation relation) {
                section.set("color", relation.getChatColor().name());
                section.set("enabled", relation.isEnabled());
            }

            @Override
            public Relation deserialize(ConfigurationSection section) {
                Relation relation = Relation.valueOf(section.getName().toUpperCase());
                relation.setChatColor(ChatColor.valueOf(Objects.requireNonNull(section.getString("color")).toUpperCase()));
                relation.setEnabled(section.getBoolean("enabled"));
                return relation;
            }
        });
    }

    public static SerializerRegistry get() {
        return instance == null ? instance = new SerializerRegistry() : instance;
    }

    public <T> void register(Class<T> tClass, Serializer<T> serializer) {
        serializerMap.put(tClass, serializer);
    }

    public <T> Serializer<T> getSerializer(Class<T> tClass) {
        return (Serializer<T>) serializerMap.get(tClass);
    }

    public <T> void serialize(T t, ConfigurationSection section) {
        Serializer<T> serializer = (Serializer<T>) getSerializer(t.getClass());
        serializer.serialize(section, t);
    }

    public <T> void deserialize(Class<T> tClass, ConfigurationSection section) {
        getSerializer(tClass).deserialize(section);
    }
}
