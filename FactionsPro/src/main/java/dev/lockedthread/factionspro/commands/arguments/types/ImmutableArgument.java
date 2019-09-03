package dev.lockedthread.factionspro.commands.arguments.types;

import dev.lockedthread.factionspro.commands.arguments.Argument;
import dev.lockedthread.factionspro.commands.arguments.ArgumentRegistry;
import dev.lockedthread.factionspro.commands.arguments.exception.ArgumentParseException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ImmutableArgument<T> implements Argument<T> {

    private final String argument;
    private final Class<T> tClass;

    @Override
    public T forceParse(String message) throws ArgumentParseException {
        Optional<T> parse = parse();
        if (parse.isPresent()) {
            return parse.get();
        }
        if (message == null) {
            throw new ArgumentParseException(argument, tClass.getSimpleName());
        } else {
            throw new ArgumentParseException(message);
        }
    }

    @Override
    public Optional<T> parse() {
        return ArgumentRegistry.get().parse(tClass, argument);
    }

    @Override
    public Class<T> getType() {
        return tClass;
    }
}
