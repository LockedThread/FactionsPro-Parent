package dev.lockedthread.factionspro.commands;

import dev.lockedthread.factionspro.FactionsPro;
import dev.lockedthread.factionspro.commands.arguments.exception.ArgumentParseException;
import dev.lockedthread.factionspro.configs.FactionsConfig;
import dev.lockedthread.factionspro.messages.FactionsMessages;
import dev.lockedthread.factionspro.structure.factions.Faction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

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
            } else if (commandContext.getFactionPlayer().hasFaction()) {
                msg(FactionsMessages.COMMAND_FACTIONS_CREATE_ERROR_ALREADY_HAVE_FACTION);
            } else {
                String rawArgument = commandContext.getRawArgument(0);

                for (String blockedChar : FactionsConfig.factions_create_blocked_chars) {
                    if (rawArgument.contains(blockedChar)) {
                        msg(FactionsMessages.COMMAND_FACTIONS_CREATE_ERROR_UNABLE_TO_CREATE_FACTION_WITH_CHAR, "{char}", blockedChar);
                        return;
                    }
                }

                Faction faction = new Faction(rawArgument, commandContext.getFactionPlayer());
                FactionsPro.get().getFactionMap().put(faction.getUuid(), faction);
                commandContext.getFactionPlayer().setFaction(faction, false, false, true);
                msg(FactionsMessages.COMMAND_FACTIONS_CREATE_SUCCESS, "{name}", faction.getName());
                if (FactionsConfig.factions_create_broadcast_enabled) {
                    for (String line : FactionsMessages.COMMAND_FACTIONS_CREATE_BROADCAST.getArrayMessage()) {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', line.replace("{player}", commandContext.getSender().getName()).replace("{name}", faction.getName())));
                    }
                }
            }
        } else {
            msg(FactionsMessages.COMMAND_FACTIONS_CREATE_ERROR_TOO_MANY_ARGUMENTS);
        }
    }
}
