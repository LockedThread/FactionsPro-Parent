package dev.lockedthread.factionspro;

import dev.lockedthread.factionspro.commands.FCommandRoot;
import dev.lockedthread.factionspro.configs.FactionsConfig;
import dev.lockedthread.factionspro.configs.types.YamlConfig;
import dev.lockedthread.factionspro.messages.FactionsMessages;
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
    private FactionsConfig mainConfig;
    @Getter
    @Setter
    private Map<Integer, Faction> factionMap;

    @Override
    public void enable() {
        instance = this;
        registerCommand(FCommandRoot.class);
        YamlConfig messagesConfig = new YamlConfig(this, "messages.yml", false);
        registerConfigs((this.mainConfig = new FactionsConfig(this, "config.yml", false)), messagesConfig.onLoad(() -> messagesConfig.loadMessageConfig(FactionsMessages.class)));
    }

    @Override
    public void disable() {
        instance = null;
    }

    public static FactionsPro get() {
        return instance;
    }
}
