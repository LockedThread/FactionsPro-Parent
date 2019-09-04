package dev.lockedthread.factionspro.configs.serializer.registry;

public interface SerializerRegistry<T> {

    void register(T t);

    T get(Class<T> tClass);

}
