package dev.lockedthread.factionspro.commands;

import dev.lockedthread.factionspro.commands.arguments.exception.ArgumentParseException;
import dev.lockedthread.factionspro.messages.FactionsMessages;
import dev.lockedthread.factionspro.structure.FactionPlayer;
import dev.lockedthread.factionspro.structure.factions.Faction;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class FCommandDisband extends FCommand {

    public FCommandDisband() {
        super();
    }

    @Override
    public void execute() throws ArgumentParseException {
        Faction faction = null;
        if (commandContext.getArguments().length == 0) {

            boolean canDisband = canDisband(faction = commandContext.getFactionPlayer().getFaction());
            if (commandContext instanceof Player && !commandContext.getFactionPlayer().hasFaction() && !canDisband) {
                msg(FactionsMessages.COMMAND_FACTIONS_DISBAND_ERROR_NO_FACTION);
            } else if (!canDisband) {
                msg(FactionsMessages.COMMAND_FACTIONS_DISBAND_ERROR_YOU_ARE_NOT_LEADER);
                return;
            } else {
                msg("&cCan't infer faction since you are console, /f disband [faction/player]");
            }
        } else if (commandContext.getArguments().length == 1) {
            Optional<Faction> factionOptional = commandContext.getArgument(Faction.class, 0).parse();
            if (factionOptional.isPresent()) {
                faction = factionOptional.get();
            } else {
                Optional<FactionPlayer> factionPlayerOptional = commandContext.getArgument(FactionPlayer.class, 0).parse();
                if (factionPlayerOptional.isPresent()) {
                    faction = factionPlayerOptional.get().getFaction();
                } else {
                    msg(FactionsMessages.COMMAND_FACTIONS_DISBAND_UNABLE_TO_FIND_FACTION_OR_PLAYER, "{name}", commandContext.getRawArgument(0));
                    return;
                }
            }
        } else {
            msg(FactionsMessages.COMMAND_FACTIONS_DISBAND_ERROR_TOO_MANY_ARGUMENTS);
            return;
        }
        if (faction != null) {
            if (faction.disband()) {
                msg(FactionsMessages.COMMAND_FACTIONS_DISBAND_SUCCESS, "{name}", faction.getName());
            }

        } else {
            msg(FactionsMessages.COMMAND_FACTIONS_DISBAND_UNABLE_TO_FIND_FACTION_OR_PLAYER, "{name}", commandContext.getRawArgument(0));
        }
    }

    private boolean canDisband(Faction faction) {
        return commandContext.getSender() instanceof ConsoleCommandSender ||
                commandContext.getSender().hasPermission("factionspro.admin") ||
                (commandContext.getFactionPlayer().hasFaction() && faction.getLeader().getUuid().equals(commandContext.getFactionPlayer().getFactionUUID()));
    }
}
