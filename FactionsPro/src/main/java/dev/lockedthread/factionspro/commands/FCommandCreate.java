package dev.lockedthread.factionspro.commands;

import dev.lockedthread.factionspro.FactionsPro;
import dev.lockedthread.factionspro.commands.arguments.Argument;
import dev.lockedthread.factionspro.commands.arguments.exception.ArgumentParseException;
import dev.lockedthread.factionspro.messages.FactionsMessages;
import dev.lockedthread.factionspro.structure.Faction;

import java.util.Optional;

@FCommand.Data(name = "create", permission = "factionspro.commands.create", usage = "/f create [faction]", description = "The command to create factions", requirePlayer = true)
public class FCommandCreate extends FCommand {

    @Override
    public void execute() throws ArgumentParseException {
        if (commandContext.getArguments().length == 0) {
            msgUsage();
        } else if (commandContext.getArguments().length == 1) {
            Argument<Faction> factionArgument = commandContext.getArgument(Faction.class, 0);
            Optional<Faction> factionOptional = factionArgument.parse();
            if (factionOptional.isPresent()) {
                msg(FactionsMessages.COMMAND_FACTIONS_CREATE_ERROR_FACTION_EXISTS);
            } else {
                Faction faction = new Faction(commandContext.getRawArgument(0), commandContext.getFactionPlayer());
                FactionsPro.get().getFactionMap().put(faction.getUuid(), faction);
                msg(FactionsMessages.COMMAND_FACTIONS_CREATE_SUCCESS, "{name}", faction.getName());
                // TODO: Create broadcast
            }
        } else {
            msg(FactionsMessages.COMMAND_FACTIONS_CREATE_ERROR_TOO_MANY_ARGUMENTS);
        }
    }
}
