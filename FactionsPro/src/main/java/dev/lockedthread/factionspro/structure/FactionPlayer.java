package dev.lockedthread.factionspro.structure;

import dev.lockedthread.factionspro.configs.FactionsConfig;
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
    private final UUID uuid;
    private double power;
    private double maxPower;
    private Role role;

    public FactionPlayer(UUID uuid, Role role) {
        this.uuid = uuid;
        this.role = role;

        /* Power */
        this.maxPower = FactionsConfig.players_power_maximum;
        if (FactionsConfig.players_power_startWithNone) {
            this.power = 0.0;
        } else {
            this.power = FactionsConfig.players_power_starting;
        }
    }

    public void die() {
        if (FactionsConfig.players_power_death_loose) {
            double futureAmount = power - FactionsConfig.players_power_death_amount;
            if (FactionsConfig.players_power_allowNegative) {
                this.power = futureAmount;
            } else if (futureAmount < FactionsConfig.players_power_minimum) {
                this.power = FactionsConfig.players_power_minimum;
            } else {
                this.power = futureAmount;
            }
        }
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public OfflinePlayer getOfflinePlayer() {
        return player == null ? player = (Player) Bukkit.getOfflinePlayer(uuid) : player;
    }

    public boolean isOnline() {
        return player != null && player.isOnline();
    }
}
