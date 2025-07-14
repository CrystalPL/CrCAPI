package pl.crystalek.crcapi.command;

import org.bukkit.configuration.ConfigurationSection;
import pl.crystalek.crcapi.message.api.MessageAPI;

import java.util.List;
import java.util.logging.Logger;

public interface CommandService {
    CommandService getCommandService(final ClassLoader classLoader, final Logger logger, final ConfigurationSection commandsConfigurationSection, final MessageAPI messageAPI, final List<Object> commands);

    void initializeCommands();
}
