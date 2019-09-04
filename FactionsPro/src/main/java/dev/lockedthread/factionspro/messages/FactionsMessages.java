package dev.lockedthread.factionspro.messages;

import dev.lockedthread.factionspro.messages.iface.IMessages;
import org.bukkit.ChatColor;

public enum FactionsMessages implements IMessages {

    COMMAND_UNABLE_TO_PARSE_ARGUMENT("&cUnable to parse {argument} as {type}");

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
