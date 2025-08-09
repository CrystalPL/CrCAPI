package pl.crystalek.crcapi.command.impl;

import com.google.common.reflect.ClassPath;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.ConfigurationSection;
import pl.crystalek.crcapi.command.api.CommandService;
import pl.crystalek.crcapi.command.api.config.ConfigCommandData;
import pl.crystalek.crcapi.command.impl.config.ConfigCommandLoader;
import pl.crystalek.crcapi.command.impl.model.CommandModel;
import pl.crystalek.crcapi.command.impl.util.ClassUtils;
import pl.crystalek.crcapi.message.api.MessageAPI;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Logger;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommandServiceImpl implements CommandService {
    ClassLoader classLoader;
    Logger logger;
    Function<Set<ClassPath.ClassInfo>, List<ConfigCommandData>> configCommandDataFunction;
    MessageAPI messageAPI;
    List<Object> commands;

    public CommandServiceImpl(final ClassLoader classLoader, final Logger logger,
                              final ConfigurationSection commandsConfigurationSection, final MessageAPI messageAPI,
                              final List<Object> commands
    ) {
        this.classLoader = classLoader;
        this.logger = logger;
        this.commands = commands;
        this.messageAPI = messageAPI;
        this.configCommandDataFunction = allClasses -> {
            final ConfigCommandLoader configCommandLoader = new ConfigCommandLoader(classLoader, logger, commandsConfigurationSection, allClasses);
            return configCommandLoader.loadCommands();
        };
    }

    public CommandServiceImpl(final ClassLoader classLoader, final Logger logger,
                              final List<ConfigCommandData> configCommandDataList, final MessageAPI messageAPI,
                              final List<Object> commands
    ) {
        this.classLoader = classLoader;
        this.logger = logger;
        this.commands = commands;
        this.messageAPI = messageAPI;
        this.configCommandDataFunction = allClasses -> configCommandDataList;
    }

    @Override
    public void initializeCommands() {
        final Set<ClassPath.ClassInfo> allClasses = ClassUtils.getAllClasses(classLoader);
        if (allClasses.isEmpty()) {
            logger.severe("Error while loading class from classLoader");
            return;
        }

        final List<ConfigCommandData> configCommandData = configCommandDataFunction.apply(allClasses);

        final CommandModelLoader commandModelLoader = new CommandModelLoader(logger, allClasses, classLoader);
        final Map<Class<?>, CommandModel> commandModelMap = commandModelLoader.loadCommandModel();
        final CommandConfigSynchronizer commandConfigSynchronizer = new CommandConfigSynchronizer(configCommandData, commandModelMap);
        commandConfigSynchronizer.applyValueFromConfigToModel();

        final CommandRegistry commandRegistry = new CommandRegistry(messageAPI, commandModelMap, commands, logger);
        commandRegistry.registerCommands();
    }
}
