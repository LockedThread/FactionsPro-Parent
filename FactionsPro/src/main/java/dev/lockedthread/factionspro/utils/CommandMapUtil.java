package dev.lockedthread.factionspro.utils;

import dev.lockedthread.factionspro.commands.FCommand;
import dev.lockedthread.factionspro.commands.executor.FCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class CommandMapUtil {

    private static CommandMapUtil instance;

    private static Field commandMap;
    private static Constructor<PluginCommand> commandConstructor;

    private CommandMapUtil() {
        try {
            (commandMap = SimplePluginManager.class.getDeclaredField("commandMap")).setAccessible(true);
            (commandConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class)).setAccessible(true);
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static CommandMapUtil getInstance() {
        return instance == null ? instance = new CommandMapUtil() : instance;
    }

    public void unregisterCommand(String s) {
        Command command = getCommandMap().getCommand(s);
        Objects.requireNonNull(command, "command");
        command.unregister(getCommandMap());

    }

    public void unregisterCommands(Plugin plugin) {
        final CommandMap commandMap = getCommandMap();
        for (Command command : getCommandMap().getKnownCommands().values()) {
            PluginCommand pluginCommand = Bukkit.getPluginCommand(command.getName());
            if (pluginCommand != null && pluginCommand.getPlugin().getName().equals(plugin.getName())) {
                command.unregister(commandMap);
            }
        }
    }

    public <T extends FCommand> void registerCommand(Plugin plugin, T fCommand) {
        PluginCommand pluginCommand = getPluginCommand(plugin, fCommand);
        getCommandMap().register(plugin.getDescription().getName(), pluginCommand);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Command> getCommands() {
        try {
            return (Map<String, Command>) commandMap.get(Bukkit.getPluginManager());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to get the Bukkit Known Command Map. Please contact LockedThread.", e);
        }
    }

    private CommandMap getCommandMap() {
        try {
            return (CommandMap) commandMap.get(Bukkit.getPluginManager());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to get the Bukkit CommandMap. Please contact LockedThread.", e);
        }
    }

    private PluginCommand getPluginCommand(Plugin plugin, FCommand fCommand) {
        try {
            PluginCommand pluginCommand = commandConstructor.newInstance(fCommand.getName(), plugin);
            if (fCommand.getAliases() != null) {
                pluginCommand.setAliases(Arrays.asList(fCommand.getAliases()));
            }
            if (fCommand.getPermission() != null) {
                pluginCommand.setPermission(fCommand.getPermission());
            }
            pluginCommand.setExecutor(FCommandExecutor.get());
            return pluginCommand;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Unable to get PluginCommand. Please contact LockedThread.", e);
        }
    }
}
