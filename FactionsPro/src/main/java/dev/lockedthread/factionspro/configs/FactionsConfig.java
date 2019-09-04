package dev.lockedthread.factionspro.configs;

import com.google.common.base.Joiner;
import dev.lockedthread.factionspro.FactionsPro;
import dev.lockedthread.factionspro.configs.annotations.ConfigComment;
import dev.lockedthread.factionspro.configs.types.YamlConfig;
import dev.lockedthread.factionspro.modules.Module;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

public class FactionsConfig extends YamlConfig {

    private static final String COMMENT_SECRET = "COMMENT_";
    @ConfigComment(comment = {"The player's starting power", "line 2", "line 3"})
    public static double players_power_starting = 20.0;
    @ConfigComment(comment = {"The maximum amount of power a player can have", "line 2", "line 3"})
    public static double players_power_maximum = 20.0;
    @ConfigComment(comment = {"The minimum amount of power a player can have", "line 2", "line 3"})
    public static double players_power_minimum = 0.0;
    @ConfigComment(comment = {"Whether or not players start with 0 power", "line 2", "line 3"})
    public static boolean players_power_startWithNone = false;
    @ConfigComment(comment = {"Whether or not players can have negative power", "line 2", "line 3"})
    public static boolean players_power_allowNegative = false;
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
    private static String[] getConfigComment(Field field) {
        ConfigComment configComment = field.getAnnotation(ConfigComment.class);
        return configComment != null ? configComment.comment() : null;
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
        for (Field field : fields) {
            String key = field.getName().contains("_") ? Joiner.on(".").skipNulls().join(field.getName().split("_")) : field.getName();
            try {
                field.setAccessible(true);
                if (isSet(key)) {
                    field.set(null, get(key));
                    System.out.println("setting field " + field.getName() + " - config key: " + key + " with value of " + get(key));
                } else {
                    System.out.println("setting config entry of " + field.getName() + " - config key: " + key + " with value of " + field.get(null));
                    @Nullable String[] configComment = getConfigComment(field);
                    if (configComment != null) {
                        int lastIndexOfColon = key.lastIndexOf(".");
                        String section = lastIndexOfColon == -1 ? COMMENT_SECRET : key.substring(0, lastIndexOfColon + 1) + COMMENT_SECRET + key.substring(lastIndexOfColon + 1);
                        for (int i = 0; i < configComment.length; i++) {
                            set(section + i, configComment[i]);
                        }
                    }
                    set(key, field.get(null));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("This shouldn't be thrown, report this to LockedThread immediately", e);
            }
        }
        try {
            String[] lines = getYamlConfiguration().saveToString().split("\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                String spaces = getSpaces(line);
                boolean startsWith = line.startsWith(spaces + COMMENT_SECRET);
                if (startsWith) {
                    line = spaces + line.replaceFirst(line.substring(0, line.indexOf(":") + 1), "#");
                    lines[i] = line;
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
        FactionsPro.get().log("Loaded FactionsConfig (" + (System.currentTimeMillis() - timeMillis) + ")");
    }

    public void destroy() {
        instance = null;
    }
}
