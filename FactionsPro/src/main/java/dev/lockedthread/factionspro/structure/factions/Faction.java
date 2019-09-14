package dev.lockedthread.factionspro.structure.factions;

import dev.lockedthread.factionspro.FactionsPro;
import dev.lockedthread.factionspro.configs.FactionsConfig;
import dev.lockedthread.factionspro.messages.FactionsMessages;
import dev.lockedthread.factionspro.structure.FactionPlayer;
import dev.lockedthread.factionspro.structure.enums.Relation;
import dev.lockedthread.factionspro.structure.enums.Role;
import dev.lockedthread.factionspro.structure.factions.types.SystemFaction;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@ToString(doNotUseGetters = true)
@Getter
@Setter
@EqualsAndHashCode
public class Faction {

    private final UUID uuid;

    private transient final Set<FactionPlayer> factionPlayerSet;

    private transient FactionPlayer leader;

    private String description;
    private String name;
    private double power;
    private double maxPower;

    private Map<UUID, Relation> relationMap;

    public Faction(String name, FactionPlayer leader, UUID uuid) {
        this.uuid = uuid;
        this.name = name;
        this.description = FactionsConfig.factions_default_description;
        this.leader = leader;
        if (leader != null) {
            (this.factionPlayerSet = new HashSet<>(1)).add(leader);
            leader.setRole(Role.LEADER);
            this.power = leader.getPower();
        } else {
            this.factionPlayerSet = new HashSet<>(0);
            this.power = 0.0;
        }
        this.maxPower = FactionsConfig.factions_power_default_max_power;
    }

    public Faction(String name, FactionPlayer leader) {
        this(name, leader, new UUID(ThreadLocalRandom.current().nextLong(), ThreadLocalRandom.current().nextLong()));
    }

    public void recalculatePower() {
        this.power = 0;
        for (FactionPlayer factionPlayer : factionPlayerSet) {
            this.power += factionPlayer.getPower();
        }
    }

    @NotNull
    public Set<FactionPlayer> getFactionPlayers(Role role) {
        if (factionPlayerSet.size() == 1) {
            return role == Role.LEADER ? factionPlayerSet : Collections.emptySet();
        }
        Set<FactionPlayer> set = null;
        for (FactionPlayer factionPlayer : factionPlayerSet) {
            if (factionPlayer.getRole() == role) {
                if (set == null) {
                    set = new HashSet<>();
                }
                set.add(factionPlayer);
            }
        }
        return set == null ? Collections.emptySet() : set;
    }

    @NotNull
    public Relation getRelation(Faction faction) {
        if (faction instanceof SystemFaction) {
            return ((SystemFaction) faction).getGlobalRelation();
        } else if (faction == null) {
            return Relation.getDefaultRelation();
        } else if (this.getName().equals(faction.getName())) {
            return Relation.MEMBER;
        } else if (relationMap == null) {
            return Relation.getDefaultRelation();
        }
        return relationMap.getOrDefault(faction.getUuid(), Relation.getDefaultRelation());
    }

    @NotNull
    public Relation getRelation(FactionPlayer factionPlayer) {
        return getRelation(factionPlayer.getFaction());
    }

    @NotNull
    public Map<UUID, Relation> getRelationMap() {
        return relationMap == null ? this.relationMap = new HashMap<>() : relationMap;
    }

    public Set<Faction> getFactionsWithRelation(Relation relation) {
        if (relationMap == null) {
            return Collections.emptySet();
        }
        return relationMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == relation)
                .map(entry -> FactionsPro.get().getFactionMap().get(entry.getKey()))
                .collect(Collectors.toSet());
    }

    public Map.Entry<Set<FactionPlayer>, Set<FactionPlayer>> getOnlineAndOfflinePlayers() {
        Set<FactionPlayer> online = new HashSet<>();
        Set<FactionPlayer> offline = new HashSet<>();

        for (FactionPlayer factionPlayer : getFactionPlayerSet()) {
            if (factionPlayer.isOnline()) {
                online.add(factionPlayer);
            } else {
                offline.add(factionPlayer);
            }
        }
        return new AbstractMap.SimpleImmutableEntry<>(offline, online);
    }


    public boolean disband(CommandSender sender) {
        FactionsPro.get().getFactionMap().remove(this);
        this.leader.setRole(Role.MEMBER);
        this.leader = null;
        for (FactionPlayer factionPlayer : factionPlayerSet) {
            factionPlayer.setFaction(FactionsConfig.systemFactionWilderness, true, true, true);
        }

        String player = sender instanceof Player ? sender.getName() : "CONSOLE";
        if (FactionsConfig.factions_disband_broadcast_enabled) {
            for (String line : FactionsMessages.COMMAND_FACTIONS_DISBAND_GLOBAL_BROADCAST.getArrayMessage()) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', line.replace("{player}", player).replace("{name}", this.getName())));
            }
        }
        return true;
    }

    public boolean isPermanent() {
        return this instanceof SystemFaction;
    }
}
