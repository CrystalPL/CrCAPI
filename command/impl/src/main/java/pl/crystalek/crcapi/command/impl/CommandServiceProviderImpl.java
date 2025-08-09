package pl.crystalek.crcapi.command.impl;

import org.bukkit.configuration.ConfigurationSection;
import pl.crystalek.crcapi.command.api.CommandService;
import pl.crystalek.crcapi.command.api.CommandServiceProvider;
import pl.crystalek.crcapi.command.api.config.ConfigCommandData;
import pl.crystalek.crcapi.message.api.MessageAPI;

import java.util.List;
import java.util.logging.Logger;

public class CommandServiceProviderImpl implements CommandServiceProvider {

    @Override
    public CommandService getCommandService(final ClassLoader classLoader, final Logger logger, final ConfigurationSection commandsConfigurationSection,
                                            final MessageAPI messageAPI, final List<Object> commands
    ) {
        return new CommandServiceImpl(classLoader, logger, commandsConfigurationSection, messageAPI, commands);
    }

    @Override
    public CommandService getCommandService(final ClassLoader classLoader, final Logger logger, final List<ConfigCommandData> configCommandDataList,
                                            final MessageAPI messageAPI, final List<Object> commands
    ) {
        return new CommandServiceImpl(classLoader, logger, configCommandDataList, messageAPI, commands);
    }
}
