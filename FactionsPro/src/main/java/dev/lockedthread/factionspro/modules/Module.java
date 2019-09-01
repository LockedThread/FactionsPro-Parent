package dev.lockedthread.factionspro.modules;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class Module extends JavaPlugin {

    @Override
    public void onEnable() {
        enable();
    }

    @Override
    public void onDisable() {
        disable();
    }

    public abstract void enable();

    public abstract void disable();

}
