package dev.lockedthread.factionspro.commands;

public class FCommandRoot extends FCommand {

    public FCommandRoot() {
        super();
        registerSubCommands(FCommandHelp.class, FCommandStatistics.class);
    }

    @Override
    public void execute() {

    }

}
