package dev.lockedthread.factionspro.modules;

import dev.lockedthread.factionspro.commands.FCommand;
import dev.lockedthread.factionspro.commands.executor.FCommandExecutor;
import dev.lockedthread.factionspro.configs.Config;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public abstract class Module extends JavaPlugin {

    @Getter
    @Setter
    private Set<Config> configSet;

    @Override
    public void onEnable() {
        enable();
        if (configSet != null) {
            configSet.forEach(Config::load);
        }
    }

    public void preDisable() {
    }

    @Override
    public void onDisable() {
        preDisable();
        if (configSet != null) {
            configSet.forEach(Config::unload);
        }
        disable();
    }

    public abstract void enable();

    public abstract void disable();

    public void log(Object... objects) {
        for (Object object : objects) {
            getLogger().log(Level.INFO, object.toString());
        }
    }

    public void registerConfigs(@NotNull Config... configs) {
        (configSet == null ? configSet = new HashSet<>() : configSet).addAll(Arrays.asList(configs));
    }

    public <T extends FCommand> void registerCommand(Class<T> fCommandClass) {
        FCommandExecutor.get().registerCommand(this, FCommand.of(fCommandClass));
    }

    public void registerCommand(FCommand fCommand) {
        FCommandExecutor.get().registerCommand(this, fCommand);
    }
}
