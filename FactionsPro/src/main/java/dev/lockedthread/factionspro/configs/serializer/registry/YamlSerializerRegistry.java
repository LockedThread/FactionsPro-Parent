package dev.lockedthread.factionspro.configs.serializer.registry;

import dev.lockedthread.factionspro.configs.serializer.types.YamlSerializer;

public class YamlSerializerRegistry implements SerializerRegistry<YamlSerializer<?>> {

    private static YamlSerializerRegistry instance;


    private YamlSerializerRegistry() {
    }

    public static YamlSerializerRegistry get() {
        return instance == null ? instance = new YamlSerializerRegistry() : instance;
    }

    public void register() {
    }

    @Override
    public void register(YamlSerializer<?> yamlSerializer) {

    }

    @Override
    public YamlSerializer<?> get(Class<YamlSerializer<?>> yamlSerializerClass) {
        return null;
    }

}
