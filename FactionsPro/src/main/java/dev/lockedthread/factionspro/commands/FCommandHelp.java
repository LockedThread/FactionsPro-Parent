package dev.lockedthread.factionspro.commands;

@FCommand.Data(name = "help", permission = "factionspro.commands.help", usage = "/f help", description = "The help command for help with features and commands")
public class FCommandHelp extends FCommand {

    public FCommandHelp() {
        super();
    }

    @Override
    public void execute() {
        msg("&cYou have executed " + commandContext.getFullCommandString());
    }
}
