package dev.lockedthread.factionspro.commands;

import org.bukkit.command.CommandSender;

public class FCommandRoot extends FCommand {

    FCommandRoot(Data data) {
        super(data);
        registerSubCommands(FCommandHelp.class, FCommandStatistics.class);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] arguments) {

    }
}
