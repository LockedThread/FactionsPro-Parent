package dev.lockedthread.factionspro.configs.serializer;

public interface Serializer<T, V> {

    T deserialize(V v);

    void serialize(V v);

}
