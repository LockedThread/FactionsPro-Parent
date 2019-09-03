package dev.lockedthread.factionspro.commands;

import dev.lockedthread.factionspro.collections.CaseInsensitiveHashMap;
import dev.lockedthread.factionspro.commands.context.CommandContext;
import dev.lockedthread.factionspro.commands.context.ImmutableCommandContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

@ToString(doNotUseGetters = true)
@lombok.Data
public abstract class FCommand {

    @NotNull
    private final String name;
    @Nullable
    private final String permission, description, usage;
    @Nullable
    private final String[] aliases;
    private final boolean requirePlayer, requireConsole;

    /**
     * Temporarily field, not null when command is being processed.
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    public CommandContext commandContext;
    @Nullable
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Map<String, FCommand> subCommands;

    public FCommand() {
        Data data = getClass().getAnnotation(Data.class);
        if (data != null) {
            this.name = data.name();
            this.permission = data.permission().isEmpty() ? null : data.permission();
            this.aliases = data.aliases().length == 0 ? null : data.aliases();
            this.requirePlayer = data.requirePlayer();
            this.requireConsole = data.requireConsole();
            this.usage = data.usage().isEmpty() ? null : data.usage();
            this.description = data.description().isEmpty() ? null : data.description();
        } else {
            throw new RuntimeException("Unable to find Data annotation for " + getClass().getName());
        }
    }

    private FCommand(Data data) {
        this.name = data.name();
        this.permission = data.permission().isEmpty() ? null : data.permission();
        this.aliases = data.aliases().length == 0 ? null : data.aliases();
        this.requirePlayer = data.requirePlayer();
        this.requireConsole = data.requireConsole();
        this.usage = data.usage();
        this.description = data.description();
    }

    private static boolean fCommandCheck(CommandSender sender, FCommand fCommand, boolean sendMessage) {
        if (fCommand.getPermission() != null && !sender.hasPermission(fCommand.getPermission())) {
            if (sendMessage) {
                // TODO: Create message framework
            }
            return false;
        } else if (fCommand.requireConsole && !(sender instanceof ConsoleCommandSender)) {
            if (sendMessage) {
                // TODO: Create message framework
            }
            return false;
        } else if (fCommand.requirePlayer && !(sender instanceof Player)) {
            if (sendMessage) {
                // TODO: Create message framework
            }
            return false;
        }
        return true;
    }

    public static <T extends FCommand> T of(Class<T> tClass, Data data) {
        try {
            return tClass.getConstructor(Data.class).newInstance(data);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find constructor for FCommand with single constructor parameter of @FCommand.Data", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to access FCommand constructor with single constructor parameter of @FCommand.Data", e);
        } catch (InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Unable to instantiate FCommand constructor with single constructor parameter of @FCommand.Data", e);
        }
    }

    public abstract void execute();

    public boolean perform(CommandSender sender, String label, String[] arguments) {
        if (arguments.length > 0) {
            if (subCommands != null) {
                FCommand subCommand = subCommands.get(arguments[0]);
                if (fCommandCheck(sender, subCommand, true)) {
                    subCommand.perform(sender, arguments[0], Arrays.copyOfRange(arguments, 0, arguments.length - 1));
                    return true;
                }
                return false;
            }
        }
        if (fCommandCheck(sender, this, true)) {
            this.commandContext = new ImmutableCommandContext(sender, arguments, label);
            execute();
            this.commandContext = null;
            return true;
        }
        return false;
    }

    public Map<String, FCommand> getSubCommands() {
        return subCommands == null ? subCommands = new CaseInsensitiveHashMap<>() : subCommands;
    }

    public void registerSubCommand(FCommand fCommand) {
        if (fCommand.getAliases() != null) {
            for (String alias : fCommand.getAliases()) {
                fCommand.getSubCommands().put(alias, fCommand);
            }
        }
        fCommand.getSubCommands().put(fCommand.getName(), this);
    }

    public final void registerSubCommands(Class... clazzes) {
        for (Class clazz : clazzes) {
            registerSubCommand((Class<? extends FCommand>) clazz);
        }
    }

    public <T extends FCommand> void registerSubCommand(Class<T> tClass) {
        registerSubCommand(of(tClass, tClass.getAnnotation(Data.class)));
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = ElementType.TYPE)
    public @interface Data {

        String name();

        String permission() default "";

        String[] aliases() default {};

        String usage() default "";

        String description() default "";

        boolean requirePlayer() default false;

        boolean requireConsole() default false;
    }
}
