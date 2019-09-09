package dev.lockedthread.factionspro.commands;

import com.google.common.base.Joiner;
import dev.lockedthread.factionspro.commands.arguments.exception.ArgumentParseException;
import dev.lockedthread.factionspro.messages.FactionsMessages;
import dev.lockedthread.factionspro.structure.FactionPlayer;
import dev.lockedthread.factionspro.structure.enums.Relation;
import dev.lockedthread.factionspro.structure.factions.Faction;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
            Map.Entry<Set<FactionPlayer>, Set<FactionPlayer>> onlineAndOfflinePlayers = faction.getOnlineAndOfflinePlayers();
            msg(FactionsMessages.COMMAND_FACTIONS_SHOW_RESPONSE,
                    "{description}", faction.getDescription(),
                    "{claims-amount}", String.valueOf(faction.getChunkPositionSet().size()),
                    "{power}", String.valueOf(faction.getPower()),
                    "{max-power}", String.valueOf(faction.getMaxPower()),
                    "{ally-color}", Relation.ALLY.getChatColor().toString(),
                    "{enemy-color}", Relation.ENEMY.getChatColor().toString(),
                    "{truce-color}", Relation.TRUCE.getChatColor().toString(),
                    "{allies}", Joiner.on(", ").skipNulls().join(faction.getFactionsWithRelation(Relation.ALLY).stream().map(Faction::getName).collect(Collectors.toSet())),
                    "{enemies}", Joiner.on(", ").skipNulls().join(faction.getFactionsWithRelation(Relation.ENEMY).stream().map(Faction::getName).collect(Collectors.toSet())),
                    "{truces}", Joiner.on(", ").skipNulls().join(faction.getFactionsWithRelation(Relation.TRUCE).stream().map(Faction::getName).collect(Collectors.toSet())),
                    "{online-member-count}", String.valueOf(onlineAndOfflinePlayers.getValue().size()),
                    "{offline-member-count}", String.valueOf(onlineAndOfflinePlayers.getKey().size()),
                    "{total-member-count}", String.valueOf(faction.getFactionPlayerSet().size()),
                    "{online-list}", Joiner.on(", ").skipNulls().join(onlineAndOfflinePlayers.getValue().stream().map(FactionPlayer::getLastKnownName).collect(Collectors.toSet())),
                    "{offline-list}", Joiner.on(", ").skipNulls().join(onlineAndOfflinePlayers.getKey().stream().map(FactionPlayer::getLastKnownName).collect(Collectors.toSet())));

            /*"&6Description: &f{description}",
                    "&6Claims / Power / Max-power: &f{claims-amount}/{power}/{max-power}",
                    "&6Allies: {ally-color}{allies}",
                    "&6Enemies: {enemy-color}{enemies}",
                    "&6Truces: {truce-color}{truces}",
                    "&6Online ({online-member-count}/{total-member-count}): {online-list}",
                    "&6Online ({offline-member-count}/{total-member-count}): {offline-list}"*/
        } else {
            msg(FactionsMessages.COMMAND_FACTIONS_SHOW_UNABLE_TO_FIND_FACTION_OR_PLAYER, "{name}", commandContext.getRawArgument(0));
        }
    }
}
