package dev.lockedthread.factionspro.commands.arguments;

import dev.lockedthread.factionspro.commands.exception.CommandParseException;

import java.util.Optional;

public interface Argument<T> {

    default T forceParse() throws CommandParseException {
        return forceParse(null);
    }

    T forceParse(String message) throws CommandParseException;

    Optional<T> parse();

    Class<T> getType();

}
