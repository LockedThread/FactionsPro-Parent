package dev.lockedthread.factionspro.structure;

import dev.lockedthread.factionspro.structure.factions.Faction;
import dev.lockedthread.factionspro.structure.position.ChunkPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Data
@AllArgsConstructor
public class WorldMatrix {

    @NotNull
    private final Map<ChunkPosition, Faction> chunkPositionFactionMap;

    public Faction getFactionAt(ChunkPosition chunkPosition) {
        return chunkPositionFactionMap.get(chunkPosition);
    }

    public void sendFactionMap(Player player) {

    }

    public ChunkPosition[][] getNearbyWorldMatrix(ChunkPosition center, int radius) {
        final ChunkPosition[][] chunkPositions = new ChunkPosition[radius * 2 + 1][radius * 2 + 1];
        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                chunkPositions[x][z] = new ChunkPosition(center.getWorld(), center.getX() + x, center.getZ() + z);
            }
        }
        return chunkPositions;
    }

}
