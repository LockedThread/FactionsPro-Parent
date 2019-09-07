package dev.lockedthread.factionspro.structure;

import dev.lockedthread.factionspro.structure.enums.Relation;
import dev.lockedthread.factionspro.structure.enums.Role;
import dev.lockedthread.factionspro.structure.position.ChunkPosition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@ToString(doNotUseGetters = true)
@Getter
@Setter
@EqualsAndHashCode
public class Faction {

    private final UUID uuid;

    private transient final Set<FactionPlayer> factionPlayerSet;

    private transient FactionPlayer leader;
    private transient Set<ChunkPosition> chunkPositionSet;

    private String name;

    private Map<UUID, Relation> relationMap;

    public Faction(String name, FactionPlayer leader) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        this.uuid = new UUID(random.nextLong(), random.nextLong());
        this.name = name;
        this.leader = leader;
        (this.factionPlayerSet = new HashSet<>(1)).add(leader);
        leader.setRole(Role.LEADER);
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
        if (faction == null || relationMap == null) {
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


}
