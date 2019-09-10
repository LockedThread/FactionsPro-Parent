package dev.lockedthread.factionspro.commands.context.types;

import dev.lockedthread.factionspro.commands.arguments.Argument;
import dev.lockedthread.factionspro.commands.arguments.types.ImmutableArgument;
import dev.lockedthread.factionspro.commands.context.CommandContext;
import dev.lockedthread.factionspro.structure.FactionPlayer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@EqualsAndHashCode
@ToString(doNotUseGetters = true)
@AllArgsConstructor
public class ImmutableCommandContext implements CommandContext {

    @NotNull
    private final CommandSender commandSender;
    @NotNull
    private final String fullCommandString;
    @Nullable
    private final FactionPlayer factionPlayer;
    @NotNull
    private String[] arguments;
    @Nullable
    private String label;

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
