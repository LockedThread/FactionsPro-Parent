package dev.lockedthread.factionspro.units;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

@EqualsAndHashCode
@ToString
public abstract class Unit {

    @Getter(onMethod_ = @Nullable)
    private Runnable shutdownHook;

    public abstract void execute();

    public void shutdown() {
        if (shutdownHook != null) {
            shutdownHook.run();
        }
    }

}
