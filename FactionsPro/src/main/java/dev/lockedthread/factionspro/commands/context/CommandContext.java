package dev.lockedthread.factionspro.commands.context;

import dev.lockedthread.factionspro.commands.arguments.Argument;
import org.bukkit.command.CommandSender;

public interface CommandContext {

    CommandSender getSender();

    String[] getArguments();

    <T> Argument<T> getArgument(Class<T> tClass, int index);

    String getLabel();

    String getFullCommandString();

    void setArguments(String[] arguments);

    void setLabel(String label);
}
