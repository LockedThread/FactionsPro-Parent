package dev.lockedthread.factionspro.commands.exception;

import dev.lockedthread.factionspro.FactionsPro;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.function.Consumer;

public class CommandParseException extends Exception {

    private final Consumer<CommandSender> senderConsumer;

    public CommandParseException(String argument, String parsableType) {
        senderConsumer = sender -> sender.sendMessage(ChatColor.translateAlternateColorCodes('&', FactionsPro.get().getConfig().getString("messages.default-unable-to-parse-argument")
                .replace("{argument}}", argument)
                .replace("{type}", parsableType)));
    }

    public CommandParseException(String message) {
        senderConsumer = sender -> sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public Consumer<CommandSender> getSenderConsumer() {
        return senderConsumer;
    }
}
