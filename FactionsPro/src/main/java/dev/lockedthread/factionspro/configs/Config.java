package dev.lockedthread.factionspro.configs;

import dev.lockedthread.factionspro.modules.Module;
import lombok.Getter;

import java.io.File;
import java.io.IOException;

public abstract class Config {

    @Getter
    private final File file;

    public Config(Module module, String fileName, boolean loadAsResource) {
        this.file = new File(module.getDataFolder(), fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        if (loadAsResource) {
            module.saveResource(fileName, false);
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void load();

    public abstract void unload();
}
