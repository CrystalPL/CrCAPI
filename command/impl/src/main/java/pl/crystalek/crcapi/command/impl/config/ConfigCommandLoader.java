package pl.crystalek.crcapi.command.impl.config;

import com.google.common.reflect.ClassPath;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.ConfigurationSection;
import pl.crystalek.crcapi.command.api.CommandExecutor;
import pl.crystalek.crcapi.command.impl.util.ClassUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConfigCommandLoader {
    ClassLoader classLoader;
    Logger logger;
    ConfigurationSection commandsConfigurationSection;
    Set<ClassPath.ClassInfo> allClasses;

    public List<ConfigCommandData> loadCommands() {
        return commandsConfigurationSection.getKeys(false).stream()
                .map(this::getCommandData)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<ConfigCommandData> getCommandData(final String commandClassName) {
        final Optional<Class<? extends CommandExecutor>> commandClassOptional = getCommandClass(commandClassName);
        if (!commandClassOptional.isPresent()) {
            return Optional.empty();
        }

        final ConfigurationSection commandConfigurationSection = commandsConfigurationSection.getConfigurationSection(commandClassName);
        final Class<? extends CommandExecutor> commandClass = commandClassOptional.get();
        final String commandName = commandConfigurationSection.getString("name");
        final List<String> commandAliases = getCommandAliases(commandConfigurationSection);
        final List<ConfigBaseCommandData> subCommandDataList = loadSubCommands(commandClass, commandConfigurationSection);

        return Optional.of(new ConfigCommandData(commandName, commandClass, commandAliases, subCommandDataList));
    }

    private List<String> getCommandAliases(final ConfigurationSection commandConfigurationSection) {
        if (commandConfigurationSection.contains("aliases")) {
            final Object aliases = commandConfigurationSection.get("aliases");
            return aliases instanceof String ? Collections.singletonList((String) aliases) : (List<String>) aliases;
        }

        return new ArrayList<>();
    }

    private List<ConfigBaseCommandData> loadSubCommands(final Class<? extends CommandExecutor> commandClass, final ConfigurationSection commandConfigurationSection) {
        if (!commandConfigurationSection.contains("subCommands")) {
            return new ArrayList<>();
        }

        if (!CommandExecutor.class.isAssignableFrom(commandClass)) {
            logger.log(Level.SEVERE, "Head class must be extend CommandExecutor: " + commandClass.getSimpleName());
            return new ArrayList<>();
        }

        final ConfigurationSection subCommandConfigurationSection = commandConfigurationSection.getConfigurationSection("subCommands");
        return subCommandConfigurationSection.getKeys(false).stream()
                .map(subCommandName -> getSubCommand(subCommandName, subCommandConfigurationSection))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<ConfigBaseCommandData> getSubCommand(final String subCommandName, final ConfigurationSection subCommandConfigurationSection) {
        final String argumentName = subCommandConfigurationSection.getString(subCommandName);
        return getCommandClass(subCommandName).map(it -> new ConfigBaseCommandData(argumentName, it));
    }

    private Optional<Class<? extends CommandExecutor>> getCommandClass(final String commandClassName) {
        final Optional<? extends Class<?>> classOptional = allClasses.stream()
                .filter(it -> it.getSimpleName().equalsIgnoreCase(commandClassName))
                .map(clazz -> ClassUtils.getClassByClassInfo(clazz, classLoader))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(CommandExecutor.class::isAssignableFrom)
                .findFirst();

        if (!classOptional.isPresent()) {
            logger.log(Level.SEVERE, "Not found class: " + commandClassName);
            return Optional.empty();
        }

        final Class<?> clazz = classOptional.get();
        return Optional.of((Class<? extends CommandExecutor>) clazz);
    }
}
