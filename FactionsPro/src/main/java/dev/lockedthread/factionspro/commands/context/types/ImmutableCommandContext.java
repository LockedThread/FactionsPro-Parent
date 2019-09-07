package dev.lockedthread.factionspro.commands.context.types;

import dev.lockedthread.factionspro.commands.arguments.Argument;
import dev.lockedthread.factionspro.commands.arguments.types.ImmutableArgument;
import dev.lockedthread.factionspro.commands.context.CommandContext;
import dev.lockedthread.factionspro.structure.FactionPlayer;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

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
    @NotNull
    private final FactionPlayer factionPlayer;

    @NotNull
    @Override
    public FactionPlayer getFactionPlayer() {
        return factionPlayer;
    }

    @NotNull
    @Override
    public CommandSender getSender() {
        return commandSender;
    }

    @NotNull
    @Override
    public String[] getArguments() {
        return arguments;
    }

    @Override
    public void setArguments(@NotNull String[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public String getRawArgument(int index) {
        return arguments[index];
    }

    @NotNull
    @Override
    public <T> Argument<T> getArgument(Class<T> tClass, int index) {
        return new ImmutableArgument<>(arguments[index], tClass);
    }

    @NotNull
    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(@NotNull String label) {
        this.label = label;
    }

    @NotNull
    @Override
    public String getFullCommandString() {
        return fullCommandString;
    }
}
