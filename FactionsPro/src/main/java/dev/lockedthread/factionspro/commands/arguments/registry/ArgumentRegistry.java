package dev.lockedthread.factionspro.commands.arguments.registry;

import dev.lockedthread.factionspro.FactionsPro;
import dev.lockedthread.factionspro.commands.arguments.parser.ArgumentParser;
import dev.lockedthread.factionspro.structure.FactionPlayer;
import dev.lockedthread.factionspro.structure.factions.Faction;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class ArgumentRegistry {

    private static ArgumentRegistry instance;
    @Getter
    private final HashMap<Class<?>, ArgumentParser<?>> argumentParserRegistry;

    private ArgumentRegistry() {
        this.argumentParserRegistry = new HashMap<>();
        // Integer
        register(int.class, () -> s -> {
            try {
                return Optional.of(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        });
        register(Integer.class, () -> s -> {
            try {
                return Optional.of(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        });
        // Double
        register(double.class, () -> s -> {
            try {
                return Optional.of(Double.parseDouble(s));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        });
        register(Double.class, () -> s -> {
            try {
                return Optional.of(Double.parseDouble(s));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        });
        // String
        register(String.class, () -> Optional::of);
        // Player
        register(Player.class, () -> s -> Optional.ofNullable(Bukkit.getPlayer(s)));
        // OfflinePlayer
        register(OfflinePlayer.class, () -> s -> Optional.of(Bukkit.getOfflinePlayer(s)));
        // UUID
        register(UUID.class, () -> s -> {
            try {
                return Optional.of(UUID.fromString(s));
            } catch (Exception e) {
                return Optional.empty();
            }
        });

        register(Material.class, () -> s -> {
            try {
                return Optional.of(Material.valueOf(s));
            } catch (IllegalArgumentException e) {
                for (Material material : Material.values()) {
                    if (material.name().equalsIgnoreCase(s)) {
                        return Optional.of(material);
                    }
                }
            }
            return Optional.empty();
        });

        register(Faction.class, () -> s -> Optional.ofNullable(FactionsPro.get().getFactionMap().get(s)));
        register(FactionPlayer.class, () -> s -> {
            System.out.println("ArgumentRegistry.ArgumentRegistry - FactionPlayer.class");
            OfflinePlayer player = Bukkit.getPlayer(s);
            System.out.println("0 player = " + player);
            if (player == null) {
                System.out.println("1 player = " + player);
                player = Bukkit.getOfflinePlayer(s);
                System.out.println("2 player = " + player);
                if (!player.hasPlayedBefore()) {
                    System.out.println("player = " + player + " never played before");
                    return Optional.empty();
                }
            }

            FactionPlayer factionPlayer = FactionsPro.get().getFactionPlayerMap().get(player.getUniqueId());
            System.out.println("3 factionPlayer = " + factionPlayer);
            return Optional.ofNullable(factionPlayer);
        });
    }

    public static ArgumentRegistry get() {
        return instance == null ? instance = new ArgumentRegistry() : instance;
    }

    @SuppressWarnings("unchecked")
    private <T> ArgumentParser<T> getArgumentParser(Class<T> tClass) {
        return (ArgumentParser<T>) argumentParserRegistry.get(tClass);
    }

    public <T> Optional<T> parse(Class<T> tClass, String application) {
        ArgumentParser<T> argumentParser = getArgumentParser(tClass);
        return argumentParser.parse().apply(application);
    }

    public <T> void register(Class<T> aClass, ArgumentParser<T> argument) {
        if (argumentParserRegistry.putIfAbsent(aClass, argument) != null) {
            throw new RuntimeException("Unable to put " + aClass.getSimpleName() + " argument parser in the map as it already exists.");
        }
    }
}
