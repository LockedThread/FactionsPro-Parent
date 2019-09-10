package dev.lockedthread.factionspro.configs.types;

import dev.lockedthread.factionspro.configs.Config;
import dev.lockedthread.factionspro.messages.iface.IMessages;
import dev.lockedthread.factionspro.modules.Module;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class YamlConfig extends Config {

    @Getter
    private YamlConfiguration yamlConfiguration;
    private Runnable unloadRunnable, loadRunnable;

    public YamlConfig(Module module, String fileName, boolean loadAsResource) {
        super(module, fileName, loadAsResource);
    }

    @Override
    public void load() {
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(getFile());
        if (loadRunnable != null) {
            loadRunnable.run();
        }
    }

    @Override
    public void unload() {
        if (unloadRunnable != null) {
            unloadRunnable.run();
        }
        //save();
    }

    public YamlConfig onLoad(Runnable onLoad) {
        this.loadRunnable = onLoad;
        return this;
    }

    public YamlConfig onUnload(Runnable onUnload) {
        this.unloadRunnable = onUnload;
        return this;
    }

    public void loadMessageConfig(Class<? extends IMessages> iMessagesClass) {
        for (IMessages messageEnum : iMessagesClass.getEnumConstants()) {
            if (isSet("messages." + messageEnum.getConfigKey())) {
                if (messageEnum.isArrayMessage()) {
                    messageEnum.setArrayMessage(getStringList("messages." + messageEnum.getConfigKey()).toArray(new String[0]));
                } else {
                    messageEnum.setMessage(getString("messages." + messageEnum.getConfigKey()));
                }
            } else {
                set("messages." + messageEnum.getConfigKey(), messageEnum.isArrayMessage() ? messageEnum.getArrayMessage() : messageEnum.getUnformattedMessage());
            }
        }
        save();
    }

    public void save() {
        try {
            yamlConfiguration.save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    public Set<String> getKeys(boolean deep) {
        return getYamlConfiguration().getKeys(deep);
    }

    public boolean isSet(@NotNull String path) {
        return getYamlConfiguration().isSet(path);
    }

    public void set(@NotNull String path, Object value) {
        getYamlConfiguration().set(path, value);
    }

    public Object get(@NotNull String path) {
        return getYamlConfiguration().get(path);
    }

    @NotNull
    public ConfigurationSection createSection(@NotNull String path) {
        return getYamlConfiguration().createSection(path);
    }

    public String getString(@NotNull String path) {
        return getYamlConfiguration().getString(path);
    }

    public String getString(@NotNull String path, String def) {
        return getYamlConfiguration().getString(path, def);
    }

    public boolean isString(@NotNull String path) {
        return getYamlConfiguration().isString(path);
    }

    public int getInt(@NotNull String path) {
        return getYamlConfiguration().getInt(path);
    }

    public int getInt(@NotNull String path, int def) {
        return getYamlConfiguration().getInt(path, def);
    }

    public boolean isInt(@NotNull String path) {
        return getYamlConfiguration().isInt(path);
    }

    public boolean getBoolean(@NotNull String path) {
        return getYamlConfiguration().getBoolean(path);
    }

    public boolean getBoolean(@NotNull String path, boolean def) {
        return getYamlConfiguration().getBoolean(path, def);
    }

    public boolean isBoolean(@NotNull String path) {
        return getYamlConfiguration().isBoolean(path);
    }

    public double getDouble(@NotNull String path) {
        return getYamlConfiguration().getDouble(path);
    }

    public double getDouble(@NotNull String path, double def) {
        return getYamlConfiguration().getDouble(path, def);
    }

    public boolean isDouble(@NotNull String path) {
        return getYamlConfiguration().isDouble(path);
    }

    public long getLong(@NotNull String path) {
        return getYamlConfiguration().getLong(path);
    }

    public long getLong(@NotNull String path, long def) {
        return getYamlConfiguration().getLong(path, def);
    }

    public boolean isLong(@NotNull String path) {
        return getYamlConfiguration().isLong(path);
    }

    public List<?> getList(@NotNull String path) {
        return getYamlConfiguration().getList(path);
    }

    public boolean isList(@NotNull String path) {
        return getYamlConfiguration().isList(path);
    }

    @NotNull
    public List<String> getStringList(@NotNull String path) {
        return getYamlConfiguration().getStringList(path);
    }

    @NotNull
    public List<Double> getDoubleList(@NotNull String path) {
        return getYamlConfiguration().getDoubleList(path);
    }

    @NotNull
    public List<Float> getFloatList(@NotNull String path) {
        return getYamlConfiguration().getFloatList(path);
    }

    @NotNull
    public List<Long> getLongList(@NotNull String path) {
        return getYamlConfiguration().getLongList(path);
    }

    public boolean isVector(@NotNull String path) {
        return getYamlConfiguration().isVector(path);
    }

    public boolean isColor(@NotNull String path) {
        return getYamlConfiguration().isColor(path);
    }

    public ConfigurationSection getConfigurationSection(@NotNull String path) {
        return getYamlConfiguration().getConfigurationSection(path);
    }

    public boolean isConfigurationSection(@NotNull String path) {
        return getYamlConfiguration().isConfigurationSection(path);
    }
}
