package pl.crystalek.crcapi.command.loader;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import pl.crystalek.crcapi.command.impl.Command;
import pl.crystalek.crcapi.command.impl.MultiCommand;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.core.config.exception.ConfigLoadException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class CommandLoader {
    public Map<Class<? extends Command>, CommandData> loadCommands(final ConfigurationSection commandsConfigurationSection, final ClassLoader classLoader) throws ConfigLoadException {
        final Map<Class<? extends Command>, CommandData> commandDataMap = new HashMap<>();
        final Set<ClassInfo> classList;
        try {
            classList = ClassPath.from(classLoader).getAllClasses();
        } catch (final IOException exception) {
            throw new ConfigLoadException("Error while getting all classes from classloader");
        }

        for (final String defaultCommandName : commandsConfigurationSection.getKeys(false)) {
            final ConfigurationSection commandConfigurationSection = commandsConfigurationSection.getConfigurationSection(defaultCommandName);

            final Class<? extends Command> commandClass = getCommand(defaultCommandName, classList, classLoader);

            final String commandName = commandConfigurationSection.getString("name");
            final List<String> commandAliases = commandConfigurationSection.contains("aliases")
                    ? getList(commandConfigurationSection.get("aliases"))
                    : new ArrayList<>();

            final Map<Class<? extends Command>, List<String>> subCommandMap = new HashMap<>();
            if (commandConfigurationSection.contains("subCommands")) {
                if (!commandClass.getSuperclass().equals(MultiCommand.class)) {
                    throw new ConfigLoadException("Head class must be extend MultiCommand: " + commandClass.getSimpleName());
                }

                final ConfigurationSection subCommandConfigurationSection = commandConfigurationSection.getConfigurationSection("subCommands");

                for (final String defaultSubCommandName : subCommandConfigurationSection.getKeys(false)) {
                    final List<String> argumentList = getList(subCommandConfigurationSection.get(defaultSubCommandName));

                    final Class<? extends Command> subCommandClass = getCommand(defaultSubCommandName, classList, classLoader);

                    subCommandMap.put(subCommandClass, argumentList);
                }
            }

            commandDataMap.put(commandClass, new CommandData(commandName, commandAliases, subCommandMap));
        }

        return commandDataMap;
    }

    private Class<? extends Command> getCommand(final String commandName, final Set<ClassInfo> classList, final ClassLoader classLoader) throws ConfigLoadException {
        for (final ClassInfo classInfo : classList) {
            if (classInfo.getSimpleName().equalsIgnoreCase(commandName)) {
                final Class<?> clazz;
                try {
                    clazz = Class.forName(classInfo.getName(), false, classLoader);
                } catch (final ClassNotFoundException exception) {
                    throw new ConfigLoadException("Not found class: " + commandName);
                }

                if (Command.class.isAssignableFrom(clazz)) {
                    return (Class<? extends Command>) clazz;
                }
            }
        }

        throw new ConfigLoadException("Not found class: " + commandName);
    }

    private List<String> getList(final Object objectList) {
        return objectList instanceof String ? Collections.singletonList((String) objectList) : (List<String>) objectList;
    }
}
