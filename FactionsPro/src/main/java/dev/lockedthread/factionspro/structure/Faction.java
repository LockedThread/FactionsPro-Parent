package dev.lockedthread.factionspro.structure;

import dev.lockedthread.factionspro.structure.position.ChunkPosition;
import lombok.Data;

import java.util.Set;

@Data
public class Faction {


    private int id;

    private Set<ChunkPosition> chunkPositionSet;
    private transient Set<FactionPlayer> factionPlayerSet;
    private String name;

    public Faction(int id) {

    }

}
