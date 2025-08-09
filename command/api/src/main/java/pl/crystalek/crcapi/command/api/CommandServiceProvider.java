package pl.crystalek.crcapi.command.api;

import org.bukkit.configuration.ConfigurationSection;
import pl.crystalek.crcapi.command.api.config.ConfigCommandData;
import pl.crystalek.crcapi.message.api.MessageAPI;

import java.util.List;
import java.util.logging.Logger;

/**
 * Factory interface for creating {@link CommandService} instances.
 */
public interface CommandServiceProvider {

    /**
     * Creates a new {@link CommandService} with the provided dependencies.
     *
     * @param classLoader                  the class loader to use for command discovery
     * @param logger                       the logger for logging messages
     * @param commandsConfigurationSection the configuration section containing command settings
     * @param messageAPI                   the message API for handling messages
     * @param commands                     the list of command objects to register
     * @return a new instance of {@link CommandService}
     */
    CommandService getCommandService(final ClassLoader classLoader, final Logger logger, final ConfigurationSection commandsConfigurationSection,
                                     final MessageAPI messageAPI, final List<Object> commands);

    /**
     * Creates a new {@link CommandService} with the provided dependencies.
     *
     * @param classLoader           the class loader to use for command discovery
     * @param logger                the logger for logging messages
     * @param configCommandDataList the command settings
     * @param messageAPI            the message API for handling messages
     * @param commands              the list of command objects to register
     * @return a new instance of {@link CommandService}
     */
    CommandService getCommandService(final ClassLoader classLoader, final Logger logger, final List<ConfigCommandData> configCommandDataList,
                                     final MessageAPI messageAPI, final List<Object> commands);
}