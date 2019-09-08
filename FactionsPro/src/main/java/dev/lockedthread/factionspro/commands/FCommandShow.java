package dev.lockedthread.factionspro.commands;

import dev.lockedthread.factionspro.commands.arguments.exception.ArgumentParseException;
import dev.lockedthread.factionspro.messages.FactionsMessages;
import dev.lockedthread.factionspro.structure.FactionPlayer;
import dev.lockedthread.factionspro.structure.factions.Faction;

import java.util.Optional;

@FCommand.Data(name = "show", aliases = {"who", "whois", "showme"}, permission = "factionspro.commands.show", usage = "/f show {player/faction}", description = "The command to show a player or faction")
public class FCommandShow extends FCommand {

    public FCommandShow() {
        super();
    }

    @Override
    public void execute() throws ArgumentParseException {
        Faction faction = null;
        if (commandContext.getArguments().length == 0) {
            faction = commandContext.getFactionPlayer().getFaction();
        } else if (commandContext.getArguments().length == 1) {
            Optional<Faction> factionOptional = commandContext.getArgument(Faction.class, 0).parse();
            if (factionOptional.isPresent()) {
                faction = factionOptional.get();
            } else {
                Optional<FactionPlayer> factionPlayerOptional = commandContext.getArgument(FactionPlayer.class, 0).parse();
                if (factionPlayerOptional.isPresent()) {
                    faction = factionPlayerOptional.get().getFaction();
                } else {
                    msg(FactionsMessages.COMMAND_FACTIONS_SHOW_UNABLE_TO_FIND_FACTION_OR_PLAYER, "{name}", commandContext.getRawArgument(0));
                    return;
                }
            }
        } else {
            msg(FactionsMessages.COMMAND_FACTIONS_SHOW_ERROR_TOO_MANY_ARGUMENTS);
            return;
        }
        if (faction != null) {

        } else {
            msg(FactionsMessages.COMMAND_FACTIONS_SHOW_UNABLE_TO_FIND_FACTION_OR_PLAYER, "{name}", commandContext.getRawArgument(0));
        }
    }
}
