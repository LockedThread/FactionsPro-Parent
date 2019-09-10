package dev.lockedthread.factionspro.structure.position;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Data
@AllArgsConstructor
public class ChunkPosition {

    private final String world;
    private final int x, z;

    public ChunkPosition(Chunk chunk) {
        this(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
    }

    @NotNull
    private static Set<Block> getBlocks(Chunk chunk) {
        Set<Block> blocks = new HashSet<>();
        for (int x = 0; x < 15; x++) {
            for (int z = 0; z < 15; z++) {
                blocks.add(chunk.getBlock(x, chunk.getChunkSnapshot().getHighestBlockYAt(x, z), z));
            }
        }
        return blocks;
    }

    @NotNull
    public Optional<World> toWorld() {
        return Optional.ofNullable(Bukkit.getWorld(world));
    }

    @NotNull
    public Chunk toChunk() {
        return toWorld().orElseThrow(RuntimeException::new).getChunkAt(x, z);
    }

    @NotNull
    public CompletableFuture<Chunk> getChunk() {
        return toWorld().orElseThrow(RuntimeException::new).getChunkAtAsync(x, z);
    }

    public void loadChunk() {
        toWorld().orElseThrow(RuntimeException::new).loadChunk(x, z);
    }

    public boolean unloadChunk() {
        return toWorld().orElseThrow(RuntimeException::new).unloadChunk(x, z);
    }

    public boolean isChunkLoaded() {
        return toWorld().orElseThrow(RuntimeException::new).isChunkLoaded(x, z);
    }

    @NotNull
    public Set<Block> getBlocks() {
        World world = toWorld().orElseThrow(RuntimeException::new);
        if (!world.isChunkLoaded(x, z)) {
            world.loadChunk(x, z);
        }
        return getBlocks(world.getChunkAt(x, z));
    }

    @Nullable
    public Set<Block> getBlocksAsync() {
        World world = toWorld().orElseThrow(RuntimeException::new);
        if (!world.isChunkLoaded(x, z)) {
            world.loadChunk(x, z);
        }
        try {
            return world.getChunkAtAsync(x, z).thenApply(ChunkPosition::getBlocks).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
