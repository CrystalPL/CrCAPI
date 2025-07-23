package pl.crystalek.crcapi.command.impl;

import com.google.common.reflect.ClassPath;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.ConfigurationSection;
import pl.crystalek.crcapi.command.api.CommandService;
import pl.crystalek.crcapi.command.impl.config.ConfigCommandData;
import pl.crystalek.crcapi.command.impl.config.ConfigCommandLoader;
import pl.crystalek.crcapi.command.impl.model.CommandModel;
import pl.crystalek.crcapi.command.impl.util.ClassUtils;
import pl.crystalek.crcapi.message.api.MessageAPI;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommandServiceImpl implements CommandService {
    ClassLoader classLoader;
    Logger logger;
    ConfigurationSection commandsConfigurationSection;
    MessageAPI messageAPI;
    List<Object> commands;

    @Override
    public void initializeCommands() {
        final Set<ClassPath.ClassInfo> allClasses = ClassUtils.getAllClasses(classLoader);
        if (allClasses.isEmpty()) {
            logger.severe("Error while loading class from classLoader");
            return;
        }

        final ConfigCommandLoader configCommandLoader = new ConfigCommandLoader(classLoader, logger, commandsConfigurationSection, allClasses);
        final List<ConfigCommandData> configCommandData = configCommandLoader.loadCommands();

        final CommandModelLoader commandModelLoader = new CommandModelLoader(logger, allClasses, classLoader);
        final Map<Class<?>, CommandModel> commandModelMap = commandModelLoader.loadCommandModel();
        final CommandConfigSynchronizer commandConfigSynchronizer = new CommandConfigSynchronizer(configCommandData, commandModelMap);
        commandConfigSynchronizer.applyValueFromConfigToModel();

        final CommandRegistry commandRegistry = new CommandRegistry(messageAPI, commandModelMap, commands, logger);
        commandRegistry.registerCommands();
    }
}
