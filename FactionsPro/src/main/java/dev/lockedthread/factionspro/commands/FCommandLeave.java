package dev.lockedthread.factionspro.commands;

import dev.lockedthread.factionspro.commands.arguments.exception.ArgumentParseException;
import dev.lockedthread.factionspro.configs.FactionsConfig;
import dev.lockedthread.factionspro.messages.FactionsMessages;
import dev.lockedthread.factionspro.structure.FactionPlayer;
import dev.lockedthread.factionspro.structure.enums.Role;
import org.bukkit.entity.Player;

@FCommand.Data(name = "leave", permission = "factionspro.commands.leave", usage = "/f leave", description = "The command to leave a faction", requirePlayer = true)
public class FCommandLeave extends FCommand {

    @Override
    public void execute() throws ArgumentParseException {
        if (commandContext.getArguments().length == 0) {
            if (commandContext.getFactionPlayer().hasFaction()) {
                if (commandContext.getFactionPlayer().getRole() == Role.LEADER) {
                    msg(FactionsMessages.COMMAND_FACTIONS_LEAVE_ERROR_YOU_ARE_LEADER);
                } else {
                    if (FactionsConfig.factions_leave_faction_broadcast_enabled) {
                        for (FactionPlayer factionPlayer : commandContext.getFactionPlayer().getFaction().getFactionPlayerSet()) {
                            if (factionPlayer.isOnline()) {
                                Player player = factionPlayer.getPlayer().get();
                                for (String line : FactionsMessages.COMMAND_FACTIONS_LEAVE_FACTION_BROADCAST.getArrayMessage()) {
                                    player.sendMessage(line.replace("{player}", commandContext.getSender().getName()));
                                }
                            }
                        }
                    }
                    commandContext.getFactionPlayer().setFaction(FactionsConfig.systemFactionWilderness);
                    msg(FactionsMessages.COMMAND_FACTIONS_LEAVE_SUCCESS);
                }
            } else {
                msg(FactionsMessages.COMMAND_FACTIONS_LEAVE_ERROR_NO_FACTION);
            }
        } else {
            msg(FactionsMessages.COMMAND_FACTIONS_LEAVE_ERROR_TOO_MANY_ARGUMENTS);
        }
    }
}
