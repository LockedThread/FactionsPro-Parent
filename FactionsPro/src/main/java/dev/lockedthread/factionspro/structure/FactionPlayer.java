package dev.lockedthread.factionspro.structure;

import dev.lockedthread.factionspro.FactionsPro;
import dev.lockedthread.factionspro.configs.FactionsConfig;
import dev.lockedthread.factionspro.structure.enums.Relation;
import dev.lockedthread.factionspro.structure.enums.Role;
import dev.lockedthread.factionspro.structure.factions.Faction;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@ToString(doNotUseGetters = true, exclude = {"faction"})
@Getter
@Setter
@EqualsAndHashCode
public class FactionPlayer {

    private final UUID uuid;
    private transient Faction faction;
    private transient Player player;
    private UUID factionUUID;
    private double power;
    private double maxPower;
    private Role role;
    private String lastKnownName;

    public FactionPlayer(OfflinePlayer offlinePlayer) {
        if (offlinePlayer.isOnline()) {
            this.player = offlinePlayer.getPlayer();
        }
        this.uuid = offlinePlayer.getUniqueId();
        this.lastKnownName = offlinePlayer.getName();

        /* Power */
        this.maxPower = FactionsConfig.players_power_maximum;
        if (FactionsConfig.players_power_startWithNone) {
            this.power = 0.0;
        } else {
            this.power = FactionsConfig.players_power_starting;
        }

        setFaction(FactionsConfig.systemFactionWilderness);
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

    public Faction getFaction() {
        if (factionUUID != null) {
            if (faction != null) {
                return faction;
            } else if ((faction = FactionsPro.get().getFactionMap().get(factionUUID)) == null) {
                factionUUID = null;
            }
        }
        return faction;
    }

    public Relation getRelation(Faction faction) {
        return getFaction().getRelation(faction);
    }

    public String getFormattedPlayerName() {
        String icon = role == null ? Role.MEMBER.getIcon() : role.getIcon();
        return icon + getLastKnownName();
    }

    public void setFaction(Faction faction) {
        setFaction(faction, true, true, false);
    }

    public void setFaction(Faction faction, boolean updateMap, boolean recalculatePower, boolean ignoreLeader) {
        if (this.faction != null) {
            if (this.role == Role.LEADER && !ignoreLeader) {
                this.faction.disband(null);
            }
            if (updateMap) {
                this.faction.getFactionPlayerSet().add(this);
            }
            if (recalculatePower) {
                this.faction.recalculatePower();
            }
        }
        this.faction = faction;
        this.factionUUID = faction.getUuid();
        if (updateMap) {
            this.faction.getFactionPlayerSet().add(this);
        }
        if (recalculatePower) {
            this.faction.recalculatePower();
        }
        if (!ignoreLeader) {
            this.role = Role.MEMBER;
        }
    }

    public void logout() {
        this.player = null;
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

    public boolean hasFaction() {
        if (factionUUID != null) {
            return faction == null ? FactionsPro.get().getFactionMap().get(factionUUID).isPermanent() : !faction.isPermanent();
        }
        return false;
    }
}
