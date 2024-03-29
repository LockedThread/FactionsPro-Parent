package dev.lockedthread.factionspro.collections.maps;

import dev.lockedthread.factionspro.collections.CaseInsensitiveHashMap;
import dev.lockedthread.factionspro.structure.factions.Faction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class FactionMap implements Map<Object, Faction> {

    private final HashMap<String, Faction> stringFactionHashMap = new CaseInsensitiveHashMap<>();
    private final HashMap<UUID, Faction> uuidFactionHashMap = new HashMap<>();

    @Override
    public int size() {
        return uuidFactionHashMap.size();
    }

    @Override
    public boolean isEmpty() {
        return uuidFactionHashMap.isEmpty() && stringFactionHashMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        if (key instanceof String) {
            return stringFactionHashMap.containsKey(key);
        } else if (key instanceof UUID) {
            return stringFactionHashMap.containsKey(key);
        } else {
            return false;
        }
    }

    @Override
    public boolean containsValue(Object value) {
        if (value instanceof Faction) {
            return stringFactionHashMap.containsValue(value) && uuidFactionHashMap.containsValue(value);
        } else {
            throw new RuntimeException("Unable to parse the object value as a Faction, tf you expect me to do here?");
        }
    }

    @Override
    public Faction get(Object key) {
        if (key instanceof String) {
            return stringFactionHashMap.get(key);
        } else if (key instanceof UUID) {
            return uuidFactionHashMap.get(key);
        } else if (key == null) {
            return null;
        } else {
            throw new RuntimeException("Unable to parse the object key as a string or uuid, tf you expect me to do here?");
        }
    }

    @Nullable
    @Override
    public Faction put(Object key, Faction value) {
        if (key instanceof String) {
            stringFactionHashMap.put((String) key, value);
            return uuidFactionHashMap.put(value.getUuid(), value);
        } else if (key instanceof UUID) {
            uuidFactionHashMap.put(value.getUuid(), value);
            return stringFactionHashMap.put(value.getName(), value);
        } else {
            throw new RuntimeException("Unable to parse the object key as a string or uuid, tf you expect me to do here?");
        }
    }

    @Override
    public Faction remove(Object key) {
        if (key instanceof Faction) {
            return stringFactionHashMap.remove(uuidFactionHashMap.remove(((Faction) key).getUuid()).getName());
        } else {
            return key instanceof String ? uuidFactionHashMap.remove(stringFactionHashMap.remove(key).getUuid()) : stringFactionHashMap.remove(uuidFactionHashMap.remove(key).getName());
        }
    }

    @Override
    public void putAll(@NotNull Map<?, ? extends Faction> map) {
        for (Entry<?, ? extends Faction> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        uuidFactionHashMap.clear();
        stringFactionHashMap.clear();
    }

    @NotNull
    @Override
    public Set<Object> keySet() {
        throw new UnsupportedOperationException("Unable to infer type for Set<?>");
    }

    public Set<UUID> uuidKeySet() {
        return uuidFactionHashMap.keySet();
    }

    public Set<String> stringKeySet() {
        return stringFactionHashMap.keySet();
    }

    @NotNull
    @Override
    public Collection<Faction> values() {
        return uuidFactionHashMap.values();
    }

    @NotNull
    @Override
    public Set<Entry<Object, Faction>> entrySet() {
        return (Set) this.uuidFactionHashMap.entrySet();
    }

    public Set<Entry<UUID, Faction>> uuidFactionEntrySet() {
        return uuidFactionHashMap.entrySet();
    }

    public Set<Entry<String, Faction>> stringFactionEntrySet() {
        return stringFactionHashMap.entrySet();
    }
}

