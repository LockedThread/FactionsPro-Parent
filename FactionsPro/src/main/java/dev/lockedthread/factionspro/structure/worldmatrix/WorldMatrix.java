package dev.lockedthread.factionspro.structure.worldmatrix;

import dev.lockedthread.factionspro.structure.factions.Faction;
import dev.lockedthread.factionspro.structure.position.ChunkPosition;
import dev.lockedthread.factionspro.structure.worldmatrix.map.WorldMatrixMap;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Player;

@ToString
public class WorldMatrix {

    private static WorldMatrix instance;

    @Getter
    private final WorldMatrixMap worldMatrixMap = new WorldMatrixMap();

    public WorldMatrix() {
        instance = this;
    }

    public static WorldMatrix getInstance() {
        return instance;
    }

    public void sendFactionMap(Player player) {

    }

    public Faction getFactionAt(ChunkPosition chunkPosition) {
        return worldMatrixMap.get(chunkPosition);
    }

    public ChunkPosition[][] getNearbyChunkPositions(ChunkPosition center, int radius) {
        final ChunkPosition[][] chunkPositions = new ChunkPosition[radius * 2 + 1][radius * 2 + 1];
        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                if (x == 0 && z == 0) {
                    chunkPositions[x][z] = center;
                } else {
                    chunkPositions[x][z] = new ChunkPosition(center.getWorld(), center.getX() + x, center.getZ() + z);
                }
            }
        }
        return chunkPositions;
    }

}
