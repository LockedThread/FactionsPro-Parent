package dev.lockedthread.factionspro.commands.executor;

import dev.lockedthread.factionspro.collections.CaseInsensitiveHashMap;
import dev.lockedthread.factionspro.commands.FCommand;
import dev.lockedthread.factionspro.modules.Module;
import dev.lockedthread.factionspro.utils.CommandMapUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class FCommandExecutor implements CommandExecutor {

    private static FCommandExecutor instance;
    private final Map<String, FCommand> fCommandMap = new CaseInsensitiveHashMap<>();

    private FCommandExecutor() {
        instance = this;
    }

    public static FCommandExecutor get() {
        return instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        FCommand fCommand = fCommandMap.get(label);
        if (fCommand != null) {
            return fCommand.perform(sender, label, args);
        }
        throw new RuntimeException("Unable to find registered FCommand even though it's registered to this executor");
    }

    public Map<String, FCommand> getCommandMap() {
        return fCommandMap;
    }

    public void registerCommand(Module module, FCommand fCommand) {
        CommandMapUtil.getInstance().registerCommand(module, fCommand);
        if (fCommand.getAliases() != null) {
            for (String alias : fCommand.getAliases()) {
                fCommandMap.put(alias, fCommand);
            }
        }
        fCommandMap.put(fCommand.getName(), fCommand);
    }
}
