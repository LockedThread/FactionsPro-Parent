package dev.lockedthread.factionspro.commands.context;

import dev.lockedthread.factionspro.commands.arguments.Argument;
import dev.lockedthread.factionspro.commands.arguments.types.ImmutableArgument;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.command.CommandSender;

@EqualsAndHashCode
@ToString(doNotUseGetters = true)
@RequiredArgsConstructor
public class ImmutableCommandContext implements CommandContext {

    private final CommandSender commandSender;
    private final String fullCommandString;
    @NonNull
    private String[] arguments;
    @NonNull
    private String label;

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

    @Override
    public String getFullCommandString() {
        return fullCommandString;
    }

    @Override
    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }
}
