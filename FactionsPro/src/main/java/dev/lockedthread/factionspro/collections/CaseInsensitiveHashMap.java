package dev.lockedthread.factionspro.collections;

import java.util.HashMap;

public class CaseInsensitiveHashMap<V> extends HashMap<String, V> {

    @Override
    public V get(Object key) {
        return super.get(((String) key).toLowerCase());
    }

    @Override
    public V put(String key, V value) {
        return super.put(key.toLowerCase(), value);
    }
}
