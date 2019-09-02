package dev.lockedthread.factionspro.modules;

import dev.lockedthread.factionspro.commands.FCommand;
import dev.lockedthread.factionspro.commands.executor.FCommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Module extends JavaPlugin {

    @Override
    public void onEnable() {
        enable();
    }

    @Override
    public void onDisable() {
        disable();
    }

    public abstract void enable();

    public abstract void disable();

    public <T extends FCommand> void registerCommand(Class<T> fCommandClass) {
        FCommand fCommand = FCommand.of(fCommandClass, fCommandClass.getAnnotation(FCommand.Data.class));
        FCommandExecutor.get().registerCommand(this, fCommand);
    }

    public void registerCommand(FCommand fCommand) {
        FCommandExecutor.get().registerCommand(this, fCommand);
    }
}
