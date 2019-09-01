package dev.lockedthread.factionspro.configs;

import dev.lockedthread.factionspro.modules.Module;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public abstract class Config {

    private File file;
    private YamlConfiguration yamlConfiguration;

    public Config(Module module, String fileName, boolean load) {
        this.file = new File(module.getDataFolder(), fileName);
    }
}
