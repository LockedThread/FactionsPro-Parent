package dev.lockedthread.factionspro.messages;

import dev.lockedthread.factionspro.messages.iface.IMessages;
import org.bukkit.ChatColor;

public enum FactionsMessages implements IMessages {

    COMMAND_UNABLE_TO_PARSE_ARGUMENT("&cUnable to parse {argument} as {type}"),

    COMMAND_USAGE_MESSAGE("&c&lERROR &8» &c{usage}"),
    COMMAND_USAGE_UNABLE_TO_EXECUTE("&c&lERROR &8» &cUnable to execute command because {reason}"),

    COMMAND_FACTIONS_CREATE_ERROR_UNABLE_TO_CREATE_FACTION_WITH_CHAR("&c&lERROR &8» &cUnable to create faction with character {char}"),
    COMMAND_FACTIONS_CREATE_ERROR_TOO_MANY_ARGUMENTS("&c&lERROR &8» &cToo many arguments! Can't create create multi-word faction names."),
    COMMAND_FACTIONS_CREATE_ERROR_FACTION_EXISTS("&c&lERROR &8» &cUnable to create faction because it already exists."),
    COMMAND_FACTIONS_CREATE_SUCCESS("&a&lSUCCESS &8» &aYou have created your faction named {name}."),
    COMMAND_FACTIONS_CREATE_BROADCAST("\n&e&lNOTICE &8» &e{player} &fhas created a faction named &e{name}.\n"),

    COMMAND_FACTIONS_SHOW_ERROR_TOO_MANY_ARGUMENTS("&c&lERROR &8» &cToo many arguments! Can't find multi-word factions or players."),
    COMMAND_FACTIONS_SHOW_UNABLE_TO_FIND_FACTION_OR_PLAYER("&c&lERROR &8» &cUnable to find faction or player with name '{name}'"),
    COMMAND_FACTIONS_SHOW_RESPONSE_HEADER("<center-line(&7-)> {relation-color}{faction} <center-line>"),
    COMMAND_FACTIONS_SHOW_RESPONSE("&6Description: &f{description}",
            "&6Land / Power / Maxpower: &f{claims}/{power}/{max-power}",
            "&6Allies: {ally-color}{allies}",
            "&6Enemies: {enemy-color}{enemies}",
            "&6Truces: {truce-color}{truces}",
            "&6Online ({online-member-count}/{total-member-count}): {online-list}",
            "&6Online ({offline-member-count}/{total-member-count}): {offline-list}");

    private String[] unformattedMessage;

    FactionsMessages(String... unformattedMessage) {
        this.unformattedMessage = unformattedMessage;
    }

    @Override
    public String getConfigKey() {
        return name().toLowerCase().replace("_", "-");
    }

    @Override
    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&', unformattedMessage[0]);
    }

    @Override
    public void setMessage(String message) {
        this.unformattedMessage[0] = message;
    }

    @Override
    public String[] getArrayMessage() {
        return unformattedMessage;
    }

    @Override
    public boolean isArrayMessage() {
        return unformattedMessage.length > 1;
    }

    @Override
    public void setArrayMessage(String[] arrayMessage) {
        this.unformattedMessage = arrayMessage;
    }

    @Override
    public String getUnformattedMessage() {
        return unformattedMessage[0];
    }
}
