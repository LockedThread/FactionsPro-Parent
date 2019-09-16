package dev.lockedthread.factionspro.structure.worldmatrix.map;

import dev.lockedthread.factionspro.FactionsPro;
import dev.lockedthread.factionspro.structure.factions.Faction;
import dev.lockedthread.factionspro.structure.position.ChunkPosition;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class WorldMatrixMap extends HashMap<ChunkPosition, UUID> {

    public UUID put(ChunkPosition chunkPosition, Faction faction) {
        UUID uuid = put(chunkPosition, faction.getUuid());
        if (uuid != null) {
            if (uuid.equals(faction.getUuid())) {
                faction.getChunkPositions().remove(chunkPosition);
            } else {
                FactionsPro.get().getFactionMap().get(uuid).getChunkPositions().remove(chunkPosition);
            }
        }

        faction.getChunkPositions().add(chunkPosition);
        return uuid;
    }

    public Faction get(ChunkPosition chunkPosition) {
        return FactionsPro.get().getFactionMap().get(super.get(chunkPosition));
    }

    public UUID remove(ChunkPosition chunkPosition) {
        UUID uuid = super.remove(chunkPosition);
        if (uuid != null) {
            FactionsPro.get().getFactionMap().get(uuid).getChunkPositions().remove(chunkPosition);
        }
        return uuid;
    }

    public Set<ChunkPosition> getChunkPositionsByFaction(Faction faction) {
        return faction.getChunkPositions();
    }
}
