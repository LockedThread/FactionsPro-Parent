package dev.lockedthread.factionspro.commands;

import dev.lockedthread.factionspro.commands.arguments.exception.ArgumentParseException;
import dev.lockedthread.factionspro.commands.context.CommandContext;
import dev.lockedthread.factionspro.messages.FactionsMessages;
import dev.lockedthread.factionspro.messages.iface.IMessages;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.ChatColor;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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
    @NotNull
    @Getter(lazy = true)
    private final Map<String, FCommand> subCommands = new HashMap<>();
    /**
     * Temporarily field, not null when command is being processed.
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    public CommandContext commandContext;

    public FCommand() {
        Data data = getClass().getAnnotation(Data.class);
        if (data != null) {
            this.name = data.name();
            this.permission = data.permission().isEmpty() ? null : data.permission();
            this.aliases = data.aliases();
            this.requirePlayer = data.requirePlayer();
            this.requireConsole = data.requireConsole();
            this.usage = data.usage().isEmpty() ? null : data.usage();
            this.description = data.description().isEmpty() ? null : data.description();
        } else {
            throw new RuntimeException("Unable to find Data annotation for " + getClass().getName());
        }
    }

    public FCommand(Data data) {
        this.name = data.name();
        this.permission = data.permission().isEmpty() ? null : data.permission();
        this.aliases = data.aliases();
        this.requirePlayer = data.requirePlayer();
        this.requireConsole = data.requireConsole();
        this.usage = data.usage().isEmpty() ? null : data.usage();
        this.description = data.description().isEmpty() ? null : data.description();
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

    public static <T extends FCommand> T of(Class<T> tClass) {
        try {
            return tClass.getConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find constructor for FCommand with single constructor parameter of @FCommand.Data", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to access FCommand constructor with single constructor parameter of @FCommand.Data", e);
        } catch (InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Unable to instantiate FCommand constructor with single constructor parameter of @FCommand.Data", e);
        }
    }

    private static String replacePlaceholders(String message, Object[] placeholders) {
        System.out.println("message = " + message + ", placeholders = " + Arrays.toString(placeholders));
        for (int i = 0; i < placeholders.length; i += 2) {
            String key = placeholders[i] instanceof String ? (String) placeholders[i] : placeholders[i].toString();
            message = message.replace(key, placeholders[i + 1].toString());
        }
        return message;
    }

    public abstract void execute() throws ArgumentParseException;

    public void registerSubCommand(FCommand fCommand) {
        if (fCommand.getAliases() != null) {
            for (String alias : fCommand.getAliases()) {
                getSubCommands().put(alias, fCommand);
            }
        }
        getSubCommands().put(fCommand.getName(), fCommand);
    }

    public final void registerSubCommands(Class... clazzes) {
        for (Class clazz : clazzes) {
            registerSubCommand((Class<? extends FCommand>) clazz);
        }
    }

    public <T extends FCommand> void registerSubCommand(Class<T> tClass) {
        registerSubCommand(of(tClass));
    }

    public boolean hasSubCommands() {
        return ((AtomicReference) subCommands).get() != null && !getSubCommands().isEmpty();
    }

    public void msg(String... messages) {
        for (String message : messages) {
            commandContext.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public void msg(Enum<? extends IMessages> iMessages) {
        msg(((IMessages) iMessages).getMessage());
    }

    public boolean perform(CommandContext commandContext) {
        this.commandContext = null;
        System.out.println("getClass().getName() = " + getClass().getName());
        System.out.println("toString() = " + toString() + "\n");
        System.out.println("0");
        if (commandContext.getArguments().length > 0) {
            System.out.println("1");
            if (hasSubCommands()) {
                System.out.println("2");
                FCommand subCommand = getSubCommands().get(commandContext.getArguments()[0]);
                if (subCommand != null) {
                    System.out.println("3");
                    if (fCommandCheck(commandContext.getSender(), subCommand, true)) {
                        @NotNull String[] args = Arrays.copyOfRange(commandContext.getArguments(), 1, commandContext.getArguments().length);
                        System.out.println("args = " + Arrays.toString(args));
                        commandContext.setLabel(commandContext.getArguments()[0]);
                        commandContext.setArguments(args);
                        System.out.println("4 return true");
                        System.out.println("commandContext = " + commandContext);
                        return subCommand.perform(commandContext);
                    }
                    System.out.println("5 return false");
                    return false;
                }
            }
        }
        System.out.println("6");
        if (fCommandCheck(commandContext.getSender(), this, true)) {
            System.out.println("7");
            this.commandContext = commandContext;
            try {
                System.out.println("8 execute");
                execute();
            } catch (ArgumentParseException e) {
                System.out.println("9 send exception");
                e.getSenderConsumer().accept(commandContext.getSender());
            }
            this.commandContext = null;
            System.out.println("0 return true");
            return true;
        }
        return false;
    }

    public void msg(Enum<? extends IMessages> iMessages, Object... placeholders) {
        if (((IMessages) iMessages).isArrayMessage()) {
            String[] arrayMessage = ((IMessages) iMessages).getArrayMessage();
            for (String message : arrayMessage) {
                msg(replacePlaceholders(message, placeholders));
            }
        } else {
            msg(replacePlaceholders(((IMessages) iMessages).getMessage(), placeholders));
        }
    }

    public void msgUsage() {
        msg(FactionsMessages.COMMAND_USAGE_MESSAGE, "{usage}", getUsage());
    }

    public void msgUnableToExecute(String reason) {
        msg(FactionsMessages.COMMAND_USAGE_UNABLE_TO_EXECUTE, "{reason}", reason);
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
