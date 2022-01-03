package pl.crystalek.crcapi.command.loader;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import pl.crystalek.crcapi.command.model.CommandData;

import java.util.*;

@UtilityClass
public class CommandLoader {

    //(default) command name -> command info
    public Map<String, CommandData> loadCommands(final ConfigurationSection commandsConfigurationSection) {
        final Map<String, CommandData> commandDataMap = new HashMap<>();

        for (final String defaultCommandName : commandsConfigurationSection.getKeys(false)) {
            final ConfigurationSection commandConfigurationSection = commandsConfigurationSection.getConfigurationSection(defaultCommandName);

            final String commandName = commandConfigurationSection.getString("name");
            final List<String> commandAliases = commandConfigurationSection.contains("aliases")
                    ? getList(commandConfigurationSection.get("aliases"))
                    : new ArrayList<>();

            final Map<String, List<String>> subCommandMap = new HashMap<>();
            if (commandConfigurationSection.contains("subCommands")) {
                final ConfigurationSection subCommandConfigurationSection = commandConfigurationSection.getConfigurationSection("subCommands");

                for (final String defaultSubCommandName : subCommandConfigurationSection.getKeys(false)) {
                    final List<String> argumentList = getList(subCommandConfigurationSection.get(defaultSubCommandName));

                    subCommandMap.put(defaultSubCommandName, argumentList);
                }
            }

            commandDataMap.put(defaultCommandName, new CommandData(commandName, commandAliases, subCommandMap));
        }

        return commandDataMap;
    }

    private List<String> getList(final Object objectList) {
        return objectList instanceof String ? Arrays.asList((String) objectList) : (List<String>) objectList;
    }
}
