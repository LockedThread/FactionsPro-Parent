package dev.lockedthread.factionspro.commands;

import dev.lockedthread.factionspro.FactionsPro;
import dev.lockedthread.factionspro.commands.arguments.exception.ArgumentParseException;
import dev.lockedthread.factionspro.configs.FactionsConfig;
import dev.lockedthread.factionspro.messages.FactionsMessages;
import dev.lockedthread.factionspro.structure.Faction;
import org.bukkit.Bukkit;

import java.util.Optional;

@FCommand.Data(name = "create", permission = "factionspro.commands.create", aliases = {"new", "found"}, usage = "/f create [faction]", description = "The command to create factions", requirePlayer = true)
public class FCommandCreate extends FCommand {

    public FCommandCreate() {
        super();
    }

    @Override
    public void execute() throws ArgumentParseException {
        if (commandContext.getArguments().length == 0) {
            msgUsage();
        } else if (commandContext.getArguments().length == 1) {
            Optional<Faction> factionOptional = commandContext.getArgument(Faction.class, 0).parse();
            if (factionOptional.isPresent()) {
                msg(FactionsMessages.COMMAND_FACTIONS_CREATE_ERROR_FACTION_EXISTS);
            } else {
                Faction faction = new Faction(commandContext.getRawArgument(0), commandContext.getFactionPlayer());
                FactionsPro.get().getFactionMap().put(faction.getUuid(), faction);
                commandContext.getFactionPlayer().setFaction(faction);
                msg(FactionsMessages.COMMAND_FACTIONS_CREATE_SUCCESS, "{name}", faction.getName());
                if (FactionsConfig.factions_create_broadcast_enabled) {
                    Bukkit.broadcastMessage(FactionsMessages.COMMAND_FACTIONS_CREATE_BROADCAST.getMessage().replace("{player}", commandContext.getSender().getName()).replace("{name}", faction.getName()));
                }
            }
        } else {
            msg(FactionsMessages.COMMAND_FACTIONS_CREATE_ERROR_TOO_MANY_ARGUMENTS);
        }
    }
}
