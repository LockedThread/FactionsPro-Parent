package dev.lockedthread.factionspro.configs;

import dev.lockedthread.factionspro.FactionsPro;
import dev.lockedthread.factionspro.configs.annotations.ConfigEntry;
import dev.lockedthread.factionspro.configs.serializer.Serializer;
import dev.lockedthread.factionspro.configs.serializer.registery.SerializerRegistry;
import dev.lockedthread.factionspro.configs.types.YamlConfig;
import dev.lockedthread.factionspro.modules.Module;
import dev.lockedthread.factionspro.structure.enums.Relation;
import dev.lockedthread.factionspro.structure.enums.Role;
import dev.lockedthread.factionspro.structure.factions.types.SystemFaction;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

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

    @ConfigEntry(key = "factions.system-factions.wilderness")
    public static SystemFaction systemFactionWilderness = new SystemFaction("Wilderness", Relation.WILDERNESS, UUID.randomUUID());
    @ConfigEntry(key = "factions.system-factions.war-zone")
    public static SystemFaction systemFactionWarZone = new SystemFaction("Warzone", Relation.WAR_ZONE, UUID.randomUUID());
    @ConfigEntry(key = "factions.system-factions.safe-zone")
    public static SystemFaction systemFactionSafeZone = new SystemFaction("SafeZone", Relation.SAFE_ZONE, UUID.randomUUID());

    @ConfigEntry(comments = {"Whether or not faction creations broadcast"})
    public static boolean factions_create_broadcast_enabled = true;

    @ConfigEntry(comments = {"Whether or not faction disbands broadcast"})
    public static boolean factions_disband_broadcast_enabled = true;

    @ConfigEntry(comments = {"Whether or not faction disbands broadcast"}, key = "factions.leave.faction-broadcast.enabled")
    public static boolean factions_leave_faction_broadcast_enabled = true;

    @ConfigEntry(key = "factions.create.blocked-chars")
    public static List<String> factions_create_blocked_chars = new ArrayList<>(Arrays.asList("-", "=", "+", "_", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "`", "~", "[", "{", "]", "}", "|", "\\", "\"", "\"", ";", ":", ",", "<", ">", ".", "/"));

    @ConfigEntry(comments = {"The default faction description"}, key = "factions.default.description")
    public static String factions_default_description = "Wow you fr using the default description?";

    @ConfigEntry(comments = {"The default faction max power"}, key = "factions.default.power.max-power")
    public static double factions_power_default_max_power = 100.0;

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
            if (Modifier.isPrivate(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            try {
                Serializer serializer = null;
                Object value = null;
                Class<?> type = field.getType();
                if (!type.isPrimitive()) {
                    value = field.get(null);
                    serializer = SerializerRegistry.get().getSerializer(value.getClass());
                }
                String key = field.getName().replace("_", ".");
                ConfigEntry configEntry = getConfigEntry(field);
                if (configEntry != null && !configEntry.key().isEmpty()) {
                    key = configEntry.key();
                }
                if (isSet(key)) {
                    if (serializer != null) {
                        field.set(null, serializer.deserialize(getConfigurationSection(key)));
                    } else {
                        if (type.isArray()) {
                            field.set(null, getList(key).toArray(new Object[0]));
                        } else if (type.isInstance(List.class)) {
                            field.set(null, getList(key));
                        } else {
                            field.set(null, get(key));
                        }
                    }
                } else {
                    if (configEntry != null) {
                        String[] comments = configEntry.comments();
                        if (comments.length > 0) {
                            int lastIndexOfColon = key.lastIndexOf(".");
                            String sectionString = lastIndexOfColon == -1 ? COMMENT_SECRET + key : key.substring(0, lastIndexOfColon + 1) + COMMENT_SECRET + key.substring(lastIndexOfColon + 1);

                            for (int i = 0; i < comments.length; i++) {
                                set(sectionString + i, comments[i]);
                            }
                            setupComments = true;
                        }
                    }

                    if (serializer != null) {
                        ConfigurationSection section = isSet(key) && isConfigurationSection(key) ? getConfigurationSection(key) : createSection(key);
                        serializer.serialize(section, value);
                    } else {
                        if (value == null) {
                            value = field.get(null);
                        }
                        if (value.getClass().isArray()) {
                            set(key, new ArrayList<>(Collections.singleton(value)));
                        }
                        set(key, value);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("This shouldn't be thrown, report this to LockedThread immediately", e);
            }
        }
        save();

        if (setupComments) {
            try {
                String[] lines = getYamlConfiguration().saveToString().split("\n");
                for (int i = 0; i < lines.length; i++) {
                    String line = lines[i];
                    System.out.println("line = " + line);
                    String spaces = getSpaces(line);
                    System.out.println("spaces = \"" + spaces + "\"");
                    boolean startsWith = line.startsWith(spaces + COMMENT_SECRET);
                    System.out.println("startsWith = " + startsWith);
                    if (startsWith) {
                        line = spaces + line.replaceFirst(line.substring(0, line.indexOf(":") + 1), "#");
                        lines[i] = line;
                        System.out.println("lines[i] = " + lines[i]);
                    }
                }
                try (FileWriter writer = new FileWriter(getFile())) {
                    for (String str : lines) {
                        System.out.println("str = " + str);
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
