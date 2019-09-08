package dev.lockedthread.factionspro.configs.serializer.registery;

import dev.lockedthread.factionspro.FactionsPro;
import dev.lockedthread.factionspro.configs.serializer.Serializer;
import dev.lockedthread.factionspro.structure.enums.Relation;
import dev.lockedthread.factionspro.structure.enums.Role;
import dev.lockedthread.factionspro.structure.factions.types.SystemFaction;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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
                Role role = Role.valueOf(section.getName().toUpperCase().replace('-', '_'));
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
                if (!relation.isSystemRelation()) {
                    section.set("default", relation.is_default());
                }
            }

            @Override
            public Relation deserialize(ConfigurationSection section) {
                Relation relation = Relation.valueOf(section.getName().toUpperCase());
                relation.setChatColor(ChatColor.valueOf(Objects.requireNonNull(section.getString("color")).toUpperCase()));
                relation.setEnabled(section.getBoolean("enabled"));
                if (!relation.isSystemRelation()) {
                    relation.set_default(section.getBoolean("default"));
                }
                return relation;
            }
        });

        register(SystemFaction.class, new Serializer<SystemFaction>() {
            @Override
            public Class<SystemFaction> getValueClass() {
                return SystemFaction.class;
            }

            @Override
            public void serialize(ConfigurationSection section, SystemFaction systemFaction) {
                section.set("name", systemFaction.getName().toLowerCase().replace('_', '-'));
                section.set("uuid", systemFaction.getUuid().toString());
            }

            @Override
            public SystemFaction deserialize(ConfigurationSection section) {
                Relation relation = null;
                String name = section.getString("name").toUpperCase().replace('-', '_');
                switch (name.toUpperCase()) {
                    case "WILDERNESS":
                        relation = Relation.WILDERNESS;
                        break;
                    case "WAR_ZONE":
                        relation = Relation.WAR_ZONE;
                        break;
                    case "SAFE_ZONE":
                        relation = Relation.SAFE_ZONE;
                        break;
                }
                UUID uuid;
                String uuidString = section.getString("uuid");
                if (uuidString == null) {
                    throw new RuntimeException("THIS IS VERY BAD! You have changed the uuid of factions. Your FactionMap instance will now be destroyed.");
                } else {
                    try {
                        uuid = UUID.fromString(uuidString);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                if (relation == null) {
                    throw new RuntimeException("Unable to find relation with name " + name);
                }
                SystemFaction systemFaction = new SystemFaction(name, relation, uuid);

                FactionsPro.get().getFactionMap().put(uuid, systemFaction);
                return systemFaction;
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
