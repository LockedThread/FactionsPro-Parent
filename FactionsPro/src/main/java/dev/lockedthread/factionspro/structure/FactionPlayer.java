package dev.lockedthread.factionspro.structure;

import dev.lockedthread.factionspro.structure.enums.Role;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
@Setter
public class FactionPlayer {

    private transient Faction faction;
    private transient Player player;
    private UUID uuid;
    private double power;
    private Role role;


    @Nullable
    public Player getPlayer() {
        return player;
    }

    public @NotNull OfflinePlayer getOfflinePlayer() {
        return player == null ? player = (Player) Bukkit.getOfflinePlayer(uuid) : player;
    }

    public boolean isOnline() {
        return player != null && player.isOnline();
    }
}
