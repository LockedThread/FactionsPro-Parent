package dev.lockedthread.factionspro;

import dev.lockedthread.factionspro.commands.FCommandRoot;
import dev.lockedthread.factionspro.configs.types.YamlConfig;
import dev.lockedthread.factionspro.modules.Module;
import dev.lockedthread.factionspro.modules.annotations.ModuleInfo;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name = "FactionsPro")
public class FactionsPro extends Module {

    private static FactionsPro instance;
    private YamlConfig mainConfig;

    @NotNull
    public static FactionsPro get() {
        return instance;
    }

    @Override
    public void enable() {
        instance = this;
        registerCommand(FCommandRoot.class);
        registerConfigs(this.mainConfig = new YamlConfig(this, "config.yml", true));
        mainConfig.onLoad(() -> {
            log("Called YamlConfig#onLoad");
        });
        mainConfig.onUnload(() -> {
            log("Called YamlConfig#onUnload");
        });
    }

    @Override
    public void disable() {
        instance = null;
    }
}
