package dev.lockedthread.factionspro;

import dev.lockedthread.factionspro.collections.maps.FactionMap;
import dev.lockedthread.factionspro.commands.FCommandRoot;
import dev.lockedthread.factionspro.configs.FactionsConfig;
import dev.lockedthread.factionspro.configs.types.YamlConfig;
import dev.lockedthread.factionspro.messages.FactionsMessages;
import dev.lockedthread.factionspro.modules.Module;
import dev.lockedthread.factionspro.modules.annotations.ModuleInfo;
import dev.lockedthread.factionspro.structure.FactionPlayer;
import dev.lockedthread.factionspro.units.FactionPlayerUnit;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ModuleInfo(name = "FactionsPro")
@Getter
@Setter
public class FactionsPro extends Module {

    private static FactionsPro instance;
    private FactionsConfig mainConfig;
    private FactionMap factionMap;
    private Map<UUID, FactionPlayer> factionPlayerMap;

    @Override
    public void enable() {
        instance = this;
        registerCommand(FCommandRoot.class);
        YamlConfig messagesConfig = new YamlConfig(this, "messages.yml", false);
        registerConfigs((this.mainConfig = new FactionsConfig(this, "config.yml", false)), messagesConfig.onLoad(() -> messagesConfig.loadMessageConfig(FactionsMessages.class)));
        setupDatabase();
        registerUnits(new FactionPlayerUnit());
    }

    private void setupDatabase() {


        // Temporary Memory Based Implementation

        this.factionMap = new FactionMap();
        this.factionPlayerMap = new HashMap<>();
    }

    @Override
    public void disable() {
        instance = null;
    }

    public static FactionsPro get() {
        return instance;
    }
}
