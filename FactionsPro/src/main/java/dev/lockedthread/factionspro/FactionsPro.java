package dev.lockedthread.factionspro;

import dev.lockedthread.factionspro.commands.FCommandRoot;
import dev.lockedthread.factionspro.modules.Module;
import dev.lockedthread.factionspro.modules.ModuleInfo;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name = "FactionsPro")
public class FactionsPro extends Module {

    private static FactionsPro instance;

    @NotNull
    public static FactionsPro get() {
        return instance;
    }

    @Override
    public void enable() {
        instance = this;
        registerCommand(FCommandRoot.class);
    }

    @Override
    public void disable() {
        instance = null;
    }
}
