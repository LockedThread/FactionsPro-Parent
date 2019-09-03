package dev.lockedthread.factionspro.commands.arguments;

import dev.lockedthread.factionspro.commands.arguments.exception.ArgumentParseException;

import java.util.Optional;

public interface Argument<T> {

    default T forceParse() throws ArgumentParseException {
        return forceParse(null);
    }

    T forceParse(String message) throws ArgumentParseException;

    Optional<T> parse();

    Class<T> getType();

}
