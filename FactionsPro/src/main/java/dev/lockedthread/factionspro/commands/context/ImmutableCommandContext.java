package dev.lockedthread.factionspro.commands.context;

import dev.lockedthread.factionspro.commands.arguments.Argument;
import dev.lockedthread.factionspro.commands.arguments.ImmutableArgument;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.command.CommandSender;

@EqualsAndHashCode
@ToString(doNotUseGetters = true)
@AllArgsConstructor
public class ImmutableCommandContext implements CommandContext {

    private final CommandSender commandSender;
    private final String[] arguments;
    private final String label;

    @Override
    public CommandSender getSender() {
        return commandSender;
    }

    @Override
    public String[] getArguments() {
        return arguments;
    }

    @Override
    public <T> Argument<T> getArgument(Class<T> tClass, int index) {
        return new ImmutableArgument<>(arguments[index], tClass);
    }

    @Override
    public String getLabel() {
        return label;
    }
}
