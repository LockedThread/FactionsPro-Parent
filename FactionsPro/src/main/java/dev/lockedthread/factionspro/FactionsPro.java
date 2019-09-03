package dev.lockedthread.factionspro;

import dev.lockedthread.factionspro.commands.FCommandRoot;
import dev.lockedthread.factionspro.configs.types.YamlConfig;
import dev.lockedthread.factionspro.modules.Module;
import dev.lockedthread.factionspro.modules.annotations.ModuleInfo;
import dev.lockedthread.factionspro.structure.Faction;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@ModuleInfo(name = "FactionsPro")
public class FactionsPro extends Module {

    private static FactionsPro instance;
    @Getter
    private YamlConfig mainConfig;
    @Getter
    @Setter
    private Map<Integer, Faction> factionMap;


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

    public static FactionsPro get() {
        return instance;
    }
}
