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
    COMMAND_FACTIONS_CREATE_ERROR_ALREADY_HAVE_FACTION("&c&lERROR &8» &cYou already have a faction, you can't create one when you're in one."),
    COMMAND_FACTIONS_CREATE_SUCCESS("&a&lSUCCESS &8» &aYou have created your faction named {name}."),
    COMMAND_FACTIONS_CREATE_BROADCAST(true, "", "&e&lNOTICE &8» &e{player} &fhas created a faction named &e{name}.", ""),

    COMMAND_FACTIONS_LEAVE_ERROR_YOU_ARE_LEADER("&c&lERROR &8» &cYou can't leave a faction you lead! Do /f disband to disband the faction."),
    COMMAND_FACTIONS_LEAVE_ERROR_NO_FACTION("&c&lERROR &8» &cYou're not in a faction to leave!"),
    COMMAND_FACTIONS_LEAVE_ERROR_TOO_MANY_ARGUMENTS("&c&lERROR &8» &cToo many arguments!"),
    COMMAND_FACTIONS_LEAVE_SUCCESS("&a&lSUCCESS &8» &aYou have left your faction."),
    COMMAND_FACTIONS_LEAVE_FACTION_BROADCAST(true, "", "&e&lNOTICE &8» &e{player} &fhas left your faction!", ""),

    COMMAND_FACTIONS_DISBAND_ERROR_YOU_ARE_NOT_LEADER("&c&lERROR &8» &cYou can't disband a faction you don't lead! Do /f leave to leave the faction."),
    COMMAND_FACTIONS_DISBAND_ERROR_NO_FACTION("&c&lERROR &8» &cYou're not in a faction to disband!"),
    COMMAND_FACTIONS_DISBAND_ERROR_TOO_MANY_ARGUMENTS("&c&lERROR &8» &cToo many arguments!"),
    COMMAND_FACTIONS_DISBAND_UNABLE_TO_FIND_FACTION_OR_PLAYER("&c&lERROR &8» &cUnable to find faction or player with name '{name}'"),
    COMMAND_FACTIONS_DISBAND_SUCCESS("&a&lSUCCESS &8» &aYou have disbanded {name}."),
    COMMAND_FACTIONS_DISBAND_GLOBAL_BROADCAST(true, "", "&e&lNOTICE &8» &e{player} &fhas disbanded &e{name}.", ""),

    COMMAND_FACTIONS_SHOW_ERROR_TOO_MANY_ARGUMENTS("&c&lERROR &8» &cToo many arguments! Can't find multi-word factions or players."),
    COMMAND_FACTIONS_SHOW_UNABLE_TO_FIND_FACTION_OR_PLAYER("&c&lERROR &8» &cUnable to find faction or player with name '{name}'"),
    COMMAND_FACTIONS_SHOW_RESPONSE_HEADER("<center-line(&7&m|-)> {relation-color}{faction} <center-line>"),
    COMMAND_FACTIONS_SHOW_RESPONSE(true, "&6Description: &f{description}",
            "&6Land / Power / Maxpower: {relation-color}{land-amount}/{power}/{max-power}",
            "&6Allies: {ally-color}{allies}",
            "&6Enemies: {enemy-color}{enemies}",
            "&6Truces: {truce-color}{truces}",
            "&6Online ({online-member-count}/{total-member-count}): {relation-color}{online-list}",
            "&6Offline ({offline-member-count}/{total-member-count}): {relation-color}{offline-list}"),

    COMMAND_FACTIONS_CLAIM_SUCCESS_SINGLE("&a&lSUCCESS &8» &aYou have successfully claimed a chunk at ({x},{z})"),
    COMMAND_FACTIONS_CLAIM_SUCCESS_MULTIPLE("&a&lSUCCESS &8» &aYou have successfully claimed {success-amount} chunks and unsuccessfully claimed {unsuccess-amount} chunks"),
    ;

    private final boolean array;
    private String[] unformattedMessage;

    FactionsMessages(String... unformattedMessage) {
        this(false, unformattedMessage);
    }

    FactionsMessages(boolean array, String... unformattedMessage) {
        this.unformattedMessage = unformattedMessage;
        this.array = array;
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
        return array;
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
