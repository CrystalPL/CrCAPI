package pl.crystalek.crcapi.command.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.command.impl.config.ConfigBaseCommandData;
import pl.crystalek.crcapi.command.impl.config.ConfigCommandData;
import pl.crystalek.crcapi.command.impl.model.CommandModel;
import pl.crystalek.crcapi.command.impl.model.SubCommandModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommandConfigSynchronizer {
    List<ConfigCommandData> configCommandDataList;
    Map<Class<?>, CommandModel> commandModelMap;

    public void applyValueFromConfigToModel() {
        for (final Map.Entry<Class<?>, CommandModel> entry : commandModelMap.entrySet()) {
            final Optional<ConfigCommandData> configCommandDataOptional = getConfigCommandData(configCommandDataList, entry.getKey());
            if (!configCommandDataOptional.isPresent()) {
                continue;
            }

            final ConfigCommandData configCommandData = configCommandDataOptional.get();
            final CommandModel commandModel = entry.getValue();
            commandModel.setAliases(configCommandData.getAliases());
            commandModel.setName(configCommandData.getName());

            synchronizeSubCommands(configCommandData, commandModel);
        }
    }

    private void synchronizeSubCommands(final ConfigCommandData configCommandData, final CommandModel commandModel) {
        final List<ConfigBaseCommandData> configSubCommands = configCommandData.getSubCommands();
        for (final SubCommandModel subCommand : commandModel.getSubCommands()) {
            final Optional<ConfigBaseCommandData> configDataOptional = getConfigCommandData(configSubCommands, subCommand.getSubCommandClass());
            configDataOptional.ifPresent(configData -> subCommand.setName(configData.getName()));
        }
    }

    private <T extends ConfigBaseCommandData> Optional<T> getConfigCommandData(final List<T> configCommandDataList, final Class<?> clazz) {
        return configCommandDataList.stream()
                .filter(configCommandData -> configCommandData.getCommandClass().equals(clazz))
                .findFirst();
    }
}
