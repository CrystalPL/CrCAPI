package pl.crystalek.crcapi.command.loader;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.command.impl.SingleCommand;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.core.config.exception.ConfigLoadException;

import java.io.IOException;
import java.util.*;

@UtilityClass
public class CommandLoader {
    public Map<Class<? extends SingleCommand>, CommandData> loadCommands(final ConfigurationSection commandsConfigurationSection, final ClassLoader classLoader, final JavaPlugin plugin) throws ConfigLoadException {
        final Map<Class<? extends SingleCommand>, CommandData> commandDataMap = new HashMap<>();
        final Set<ClassInfo> classList;
        try {
            classList = ClassPath.from(classLoader).getAllClasses();
        } catch (final IOException exception) {
            throw new ConfigLoadException("Error while getting all classes from classloader");
        }

        for (final String defaultCommandName : commandsConfigurationSection.getKeys(false)) {
            final ConfigurationSection commandConfigurationSection = commandsConfigurationSection.getConfigurationSection(defaultCommandName);

            final Class<? extends SingleCommand> commandClassOptional = getCommand(defaultCommandName, classList, classLoader, plugin);

            final String commandName = commandConfigurationSection.getString("name");
            final List<String> commandAliases = commandConfigurationSection.contains("aliases")
                    ? getList(commandConfigurationSection.get("aliases"))
                    : new ArrayList<>();

            final Map<Class<? extends SingleCommand>, List<String>> subCommandMap = new HashMap<>();
            if (commandConfigurationSection.contains("subCommands")) {
                final ConfigurationSection subCommandConfigurationSection = commandConfigurationSection.getConfigurationSection("subCommands");

                for (final String defaultSubCommandName : subCommandConfigurationSection.getKeys(false)) {
                    final List<String> argumentList = getList(subCommandConfigurationSection.get(defaultSubCommandName));

                    final Class<? extends SingleCommand> subCommandClassOptional = getCommand(defaultSubCommandName, classList, classLoader, plugin);

                    subCommandMap.put(subCommandClassOptional, argumentList);
                }
            }

            commandDataMap.put(commandClassOptional, new CommandData(commandName, commandAliases, subCommandMap));
        }

        return commandDataMap;
    }

    private Class<? extends SingleCommand> getCommand(final String commandName, final Set<ClassInfo> classList, final ClassLoader classLoader, final JavaPlugin plugin) throws ConfigLoadException {
        for (final ClassInfo classInfo : classList) {
            if (classInfo.getSimpleName().equalsIgnoreCase(commandName)) {
                final Class<?> clazz;
                try {

                    clazz = Class.forName(classInfo.getName(), false, classLoader);
                } catch (final ClassNotFoundException exception) {
                    throw new ConfigLoadException("Not found class: " + commandName);
                }

                if (clazz.getSuperclass().equals(SingleCommand.class)) {
                    return (Class<? extends SingleCommand>) clazz;
                }
            }
        }

        throw new ConfigLoadException("Not found class: " + commandName);
    }

    private List<String> getList(final Object objectList) {
        return objectList instanceof String ? Arrays.asList((String) objectList) : (List<String>) objectList;
    }
}
