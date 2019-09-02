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

public abstract class Module extends JavaPlugin {

    @Getter
    @Setter
    private Set<Config> configSet;

    @Override
    public void onEnable() {
        enable();
        configSet.forEach(Config::load);
    }

    public void preDisable() {
    }

    @Override
    public void onDisable() {
        preDisable();
        disable();
        configSet.forEach(Config::unload);
    }

    public abstract void enable();

    public abstract void disable();

    public void registerConfigs(@NotNull Config... configs) {
        (configSet == null ? configSet = new HashSet<>() : configSet).addAll(Arrays.asList(configs));
    }

    public <T extends FCommand> void registerCommand(Class<T> fCommandClass) {
        FCommand fCommand = FCommand.of(fCommandClass, fCommandClass.getAnnotation(FCommand.Data.class));
        FCommandExecutor.get().registerCommand(this, fCommand);
    }

    public void registerCommand(FCommand fCommand) {
        FCommandExecutor.get().registerCommand(this, fCommand);
    }
}
