package dev.lockedthread.factionspro.modules;

import dev.lockedthread.factionspro.commands.FCommand;
import dev.lockedthread.factionspro.commands.executor.FCommandExecutor;
import dev.lockedthread.factionspro.configs.Config;
import dev.lockedthread.factionspro.events.EventPost;
import dev.lockedthread.factionspro.units.Unit;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public abstract class Module extends JavaPlugin {

    @Getter(lazy = true)
    private final Set<Config> configSet = new HashSet<>();
    @Getter(lazy = true)
    private final Set<Unit> units = new HashSet<>();
    @Getter(lazy = true)
    private final Set<EventPost> eventPosts = new HashSet<>();

    @Override
    public void onEnable() {
        enable();
        getConfigSet().forEach(Config::load);
    }

    public void preDisable() {
    }

    @Override
    public void onDisable() {
        preDisable();
        getConfigSet().forEach(Config::unload);
        disable();
        getUnits().forEach(Unit::shutdown);
    }

    public abstract void enable();

    public abstract void disable();

    public void log(Object... objects) {
        for (Object object : objects) {
            getLogger().log(Level.INFO, object.toString());
        }
    }

    public void registerConfigs(@NotNull Config... configs) {
        getConfigSet().addAll(Arrays.asList(configs));
    }

    public <T extends FCommand> void registerCommand(Class<T> fCommandClass) {
        FCommandExecutor.get().registerCommand(this, FCommand.of(fCommandClass));
    }

    public void registerUnits(Unit... units) {
        for (Unit unit : units) {
            getUnits().add(unit);
            unit.execute();
        }
    }

    public void registerCommand(FCommand fCommand) {
        FCommandExecutor.get().registerCommand(this, fCommand);
    }
}
