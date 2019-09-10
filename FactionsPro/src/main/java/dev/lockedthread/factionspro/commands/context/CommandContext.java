package dev.lockedthread.factionspro.commands.context;

import dev.lockedthread.factionspro.commands.arguments.Argument;
import dev.lockedthread.factionspro.structure.FactionPlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandContext {

    @Nullable
    FactionPlayer getFactionPlayer();

    @NotNull
    CommandSender getSender();

    @NotNull
    String[] getArguments();

    void setArguments(@NotNull String[] arguments);

    String getRawArgument(int index);

    @NotNull <T> Argument<T> getArgument(Class<T> tClass, int index);

    @NotNull
    String getLabel();

    void setLabel(@NotNull String label);

    @NotNull
    String getFullCommandString();
}
