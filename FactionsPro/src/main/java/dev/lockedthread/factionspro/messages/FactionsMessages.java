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

    ;

    private String unformattedMessage;

    FactionsMessages(String unformattedMessage) {
        this.unformattedMessage = unformattedMessage;
    }

    @Override
    public String getConfigKey() {
        return name().toLowerCase().replace("_", "-");
    }

    @Override
    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&', unformattedMessage);
    }

    @Override
    public void setMessage(String message) {
        this.unformattedMessage = message;
    }

    @Override
    public String getUnformattedMessage() {
        return unformattedMessage;
    }
}
