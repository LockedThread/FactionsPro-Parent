package dev.lockedthread.factionspro.commands;

@FCommand.Data(name = "factions", aliases = {"f", "faction"}, permission = "factionspro.commands.root", usage = "/f", description = "The root factions command")
public class FCommandRoot extends FCommand {

    public FCommandRoot() {
        super();
        registerSubCommands(FCommandHelp.class,
                FCommandStatistics.class,
                FCommandCreate.class,
                FCommandShow.class,
                FCommandLeave.class,
                FCommandDisband.class,
                FCommandClaim.class);
    }

    @Override
    public void execute() {
        msg("&cYou have executed " + commandContext.getFullCommandString());
    }

}
