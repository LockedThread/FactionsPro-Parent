package dev.lockedthread.factionspro.structure.worldmatrix.map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.lockedthread.factionspro.FactionsPro;
import dev.lockedthread.factionspro.structure.factions.Faction;
import dev.lockedthread.factionspro.structure.position.ChunkPosition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WorldMatrixMap extends HashMap<ChunkPosition, UUID> {

    private transient final Multimap<UUID, ChunkPosition> worldMatrixMultimap = HashMultimap.create();

    public UUID put(ChunkPosition chunkPosition, Faction faction) {
        UUID uuid = put(chunkPosition, faction.getUuid());
        if (uuid != null) {
            worldMatrixMultimap.remove(uuid, chunkPosition);
        }

        worldMatrixMultimap.put(faction.getUuid(), chunkPosition);
        return uuid;
    }

    public Faction get(ChunkPosition chunkPosition) {
        return FactionsPro.get().getFactionMap().get(super.get(chunkPosition));
    }

    public UUID remove(ChunkPosition chunkPosition) {
        UUID uuid = super.remove(chunkPosition);
        if (uuid != null) {
            worldMatrixMultimap.remove(uuid, chunkPosition);
        }
        return uuid;
    }

    public Multimap<UUID, ChunkPosition> getWorldMatrixMultimap() {
        return worldMatrixMultimap;
    }

    public Map<UUID, Collection<ChunkPosition>> getChunkPositionMultiMapAsMap() {
        return worldMatrixMultimap.asMap();
    }

    public Collection<ChunkPosition> getChunkPositionsByFaction(Faction faction) {
        return getChunkPositionsByFactionId(faction.getUuid());
    }

    public Collection<ChunkPosition> getChunkPositionsByFactionId(UUID uuid) {
        return worldMatrixMultimap.get(uuid);
    }
}
