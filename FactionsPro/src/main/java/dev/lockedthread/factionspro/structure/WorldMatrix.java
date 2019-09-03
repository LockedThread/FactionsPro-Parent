package dev.lockedthread.factionspro.structure;

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
}
