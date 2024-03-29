package dev.lockedthread.factionspro.commands.arguments.parser;

import java.util.Optional;
import java.util.function.Function;

public interface ArgumentParser<T> {

    Function<String, Optional<T>> parse();
}
