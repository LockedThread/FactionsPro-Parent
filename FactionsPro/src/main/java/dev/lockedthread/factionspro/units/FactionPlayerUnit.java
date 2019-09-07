package dev.lockedthread.factionspro.units;

import dev.lockedthread.factionspro.FactionsPro;
import dev.lockedthread.factionspro.events.EventPost;
import dev.lockedthread.factionspro.structure.FactionPlayer;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;
import java.util.UUID;

public class FactionPlayerUnit extends Unit {

    @Override
    public void execute() {
        EventPost.of(PlayerJoinEvent.class, EventPriority.LOWEST)
                .handle(event -> {
                    Map<UUID, FactionPlayer> map = FactionsPro.get().getFactionPlayerMap();
                    FactionPlayer factionPlayer = map.get(event.getPlayer().getUniqueId());
                    System.out.println("couldn't find factionPlayer with uuid " + event.getPlayer().getUniqueId().toString());
                    if (factionPlayer != null) {
                        System.out.println("found factionPlayer = " + factionPlayer.toString());
                    } else {
                        map.put(event.getPlayer().getUniqueId(), factionPlayer = new FactionPlayer(event.getPlayer().getUniqueId()));
                        System.out.println("created factionPlayer = " + factionPlayer.toString());
                    }
                }).post(FactionsPro.get());
    }
}
