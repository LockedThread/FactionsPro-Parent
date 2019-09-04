package dev.lockedthread.factionspro.structure;

import dev.lockedthread.factionspro.structure.enums.Role;
import dev.lockedthread.factionspro.structure.position.ChunkPosition;
import lombok.Data;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Data
public class Faction {


    private final int id;

    private transient final Set<FactionPlayer> factionPlayerSet;

    private transient FactionPlayer leader;
    private transient Set<ChunkPosition> chunkPositionSet;

    private String name;

    public Faction(int id, String name, FactionPlayer leader) {
        this.id = id;
        this.name = name;
        this.leader = leader;
        (this.factionPlayerSet = new HashSet<>(1)).add(leader);
    }

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


}
