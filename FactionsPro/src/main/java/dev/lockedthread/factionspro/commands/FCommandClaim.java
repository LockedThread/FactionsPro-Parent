package dev.lockedthread.factionspro.commands;

import dev.lockedthread.factionspro.commands.arguments.exception.ArgumentParseException;
import dev.lockedthread.factionspro.structure.enums.ClaimResponse;
import dev.lockedthread.factionspro.structure.factions.Faction;
import dev.lockedthread.factionspro.structure.position.ChunkPosition;
import org.bukkit.Chunk;

@FCommand.Data(name = "claim", permission = "factionspro.comnads.claim", requirePlayer = true, usage = "/f claim [radius]", description = "Allows players to claim land for their faction")
public class FCommandClaim extends FCommand {

    public FCommandClaim() {
        super();
    }

    @Override
    public void execute() throws ArgumentParseException {
        assert commandContext.getFactionPlayer() != null;
        if (commandContext.getArguments().length <= 1) {
            if (commandContext.getFactionPlayer().hasFaction()) {
                Faction faction = commandContext.getFactionPlayer().getFaction();
                int radius = commandContext.getArguments().length == 0 ? 0 : commandContext.getArgument(int.class, 0).forceParse();

                if (radius == 0) {

                } else {
                    int chunkSuccessCount = 0;
                    Chunk lastKnownChunk = commandContext.getFactionPlayer().getLastKnownLocation().getChunk();
                    String worldName = lastKnownChunk.getWorld().getName();
                    for (int x = -radius + lastKnownChunk.getX(); x < radius + lastKnownChunk.getX(); x++) {
                        for (int z = -radius + lastKnownChunk.getZ(); z < radius + lastKnownChunk.getZ(); z++) {
                            ClaimResponse claimResponse = faction.claim(commandContext.getFactionPlayer(), new ChunkPosition(worldName, x, z));
                            if (claimResponse == ClaimResponse.SUCCESS) {
                                chunkSuccessCount++;
                            }
                        }
                    }
                }
            } else {

            }
        } else {
            msgUsage();
        }
    }
}
