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

    /**
     * Temporarily field, not null when command is being processed.
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    public CommandContext commandContext;

    @NotNull
    @Getter(lazy = true)
    private final Map<String, FCommand> subCommands = new HashMap<>();

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

    public abstract void execute() throws ArgumentParseException;

    private static String replacePlaceholders(String message, String[] placeholders) {
        for (int i = 0; i < placeholders.length; i += 2) {
            message = message.replace(placeholders[i], placeholders[i + 1]);
        }
        return message;
    }

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
        return ((AtomicReference) subCommands).get() != null;
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
        System.out.println("getClass().getName() = " + getClass().getName());
        System.out.println("toString() = " + toString() + "\n");
        if (commandContext.getArguments().length > 0) {
            if (hasSubCommands()) {
                FCommand subCommand = getSubCommands().get(commandContext.getArguments()[0]);
                if (subCommand != null) {
                    if (fCommandCheck(commandContext.getSender(), subCommand, true)) {
                        @NotNull String[] args = Arrays.copyOfRange(commandContext.getArguments(), 0, commandContext.getArguments().length - 1);
                        commandContext.setLabel(commandContext.getArguments()[0]);
                        commandContext.setArguments(args);
                        subCommand.perform(commandContext);
                        return true;
                    }
                    return false;
                }
            }
        }
        if (fCommandCheck(commandContext.getSender(), this, true)) {
            this.commandContext = commandContext;
            try {
                execute();
            } catch (ArgumentParseException e) {
                e.getSenderConsumer().accept(commandContext.getSender());
            }
            this.commandContext = null;
            return true;
        }
        return false;
    }

    public void msg(Enum<? extends IMessages> iMessages, String... placeholders) {
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
