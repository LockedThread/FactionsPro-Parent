package dev.lockedthread.factionspro.configs;

import com.google.common.base.Joiner;
import dev.lockedthread.factionspro.FactionsPro;
import dev.lockedthread.factionspro.configs.annotations.ConfigEntry;
import dev.lockedthread.factionspro.configs.serializer.Serializer;
import dev.lockedthread.factionspro.configs.serializer.registery.SerializerRegistry;
import dev.lockedthread.factionspro.configs.types.YamlConfig;
import dev.lockedthread.factionspro.modules.Module;
import dev.lockedthread.factionspro.structure.enums.Relation;
import dev.lockedthread.factionspro.structure.enums.Role;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

public class FactionsConfig extends YamlConfig {

    private static final String COMMENT_SECRET = "COMMENT_";


    @ConfigEntry(comments = {"The player's starting power"})
    public static double players_power_starting = 20.0;
    @ConfigEntry(comments = {"The maximum amount of power a player can have"})
    public static double players_power_maximum = 20.0;
    @ConfigEntry(comments = {"The minimum amount of power a player can have"})
    public static double players_power_minimum = 0.0;
    @ConfigEntry(comments = {"Whether or not players start with 0 power"}, key = "players.power.start-with-none")
    public static boolean players_power_startWithNone = false;
    @ConfigEntry(comments = {"Whether or not players can have negative power"}, key = "players.power.allow-negative")
    public static boolean players_power_allowNegative = false;
    @ConfigEntry(comments = {"Whether or not players lose power when they die"})

    public static boolean players_power_death_loose = false;
    @ConfigEntry(comments = {"How much power players lose when they die"})
    public static double players_power_death_amount = 1.0;

    @ConfigEntry(comments = {"Config section for the leader role"}, key = "roles.leader")
    public static Role leaderRole = Role.LEADER;
    @ConfigEntry(comments = {"Config section for the co-leader role"}, key = "roles.co-leader")
    public static Role coLeaderRole = Role.CO_LEADER;
    @ConfigEntry(comments = {"Config section for the moderator role"}, key = "roles.moderator")
    public static Role moderatorRole = Role.MODERATOR;
    @ConfigEntry(comments = {"Config section for the member role"}, key = "roles.member")
    public static Role memberRole = Role.MEMBER;

    @ConfigEntry(comments = {"Config section for the enemy relation"}, key = "relations.enemy")
    public static Relation enemyRelation = Relation.ENEMY;
    @ConfigEntry(comments = {"Config section for the ally relation"}, key = "relations.ally")
    public static Relation allyRelation = Relation.ALLY;
    @ConfigEntry(comments = {"Config section for the member relation"}, key = "relations.member")
    public static Relation memberRelation = Relation.MEMBER;
    @ConfigEntry(comments = {"Config section for the neutral relation"}, key = "relations.neutral")
    public static Relation neutralRelation = Relation.NEUTRAL;
    @ConfigEntry(comments = {"Config section for the truce relation"}, key = "relations.truce")
    public static Relation trueRelation = Relation.TRUCE;

    @Nullable
    private static FactionsConfig instance;

    public FactionsConfig(Module module, String fileName, boolean loadAsResource) {
        super(module, fileName, loadAsResource);
        if (instance != null) {
            throw new RuntimeException("Unable to find instance of FactionsConfig");
        }
        instance = this;
    }

    private static String getSpaces(String string) {
        StringBuilder spaces = new StringBuilder();
        for (char aChar : string.toCharArray()) {
            if (Character.toString(aChar).equals(" ")) {
                spaces.append(" ");
            } else {
                break;
            }
        }
        return spaces.toString();
    }

    @Nullable
    private static ConfigEntry getConfigEntry(Field field) {
        return field.getAnnotation(ConfigEntry.class);
    }

    @NotNull
    public static FactionsConfig get() {
        if (instance == null) {
            throw new RuntimeException("Unable to find instance of FactionsConfig");
        }
        return instance;
    }

    @Override
    public void load() {
        final long timeMillis = System.currentTimeMillis();
        super.load();
        Field[] fields = FactionsConfig.this.getClass().getFields();
        boolean setupComments = false;
        for (Field field : fields) {
            String key = field.getName().contains("_") ? Joiner.on(".").skipNulls().join(field.getName().split("_")) : field.getName();
            try {
                field.setAccessible(true);
                Serializer serializer = null;
                Object value = null;
                if (!field.getType().isPrimitive()) {
                    value = field.get(null);
                    serializer = SerializerRegistry.get().getSerializer(value.getClass());
                }
                if (isSet(key)) {
                    if (serializer != null) {
                        ConfigEntry configEntry = getConfigEntry(field);
                        if (configEntry != null && !configEntry.key().isEmpty()) {
                            key = configEntry.key();
                        }
                        field.set(null, serializer.deserialize(getConfigurationSection(key)));
                    } else {
                        field.set(null, get(key));
                    }
                } else {
                    ConfigEntry configEntry = getConfigEntry(field);
                    if (configEntry != null) {
                        if (!configEntry.key().isEmpty()) {
                            key = configEntry.key();
                        }
                        int lastIndexOfColon = key.lastIndexOf(".");
                        String sectionString = lastIndexOfColon == -1 ? COMMENT_SECRET + key : key.substring(0, lastIndexOfColon + 1) + COMMENT_SECRET + key.substring(lastIndexOfColon + 1);

                        String[] comments = configEntry.comments();
                        for (int i = 0; i < comments.length; i++) {
                            set(sectionString + i, comments[i]);
                        }
                        setupComments = true;
                    }

                    if (serializer != null) {
                        ConfigurationSection section = isSet(key) && isConfigurationSection(key) ? getConfigurationSection(key) : createSection(key);
                        serializer.serialize(section, value);
                        continue;

                    }
                    set(key, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("This shouldn't be thrown, report this to LockedThread immediately", e);
            }
        }

        if (setupComments) {
            try {
                String[] lines = getYamlConfiguration().saveToString().split("\n");
                for (int i = 0; i < lines.length; i++) {
                    String line = lines[i];
                    String spaces = getSpaces(line);
                    boolean startsWith = line.startsWith(spaces + COMMENT_SECRET);
                    if (startsWith) {
                        lines[i] = spaces + line.replaceFirst(line.substring(0, line.indexOf(":") + 1), "#");
                    }
                }
                try (FileWriter writer = new FileWriter(getFile())) {
                    for (String str : lines) {
                        writer.write(str + System.lineSeparator());
                    }
                }
                getYamlConfiguration().load(getFile());
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        FactionsPro.get().log("Loaded FactionsConfig (" + (System.currentTimeMillis() - timeMillis) + ")");
    }

    public void destroy() {
        instance = null;
    }
}
