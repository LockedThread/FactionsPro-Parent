package dev.lockedthread.factionspro.commands.exception;

import dev.lockedthread.factionspro.FactionsPro;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.function.Consumer;

public class CommandParseException extends Exception {

    private Consumer<CommandSender> senderConsumer;

    public CommandParseException(String argument, String parsableType) {
        senderConsumer = sender -> sender.sendMessage(ChatColor.translateAlternateColorCodes('&', FactionsPro.get().getConfig().getString("messages.default-unable-to-parse-argument")));
    }

    public CommandParseException(String message) {

    }
}
