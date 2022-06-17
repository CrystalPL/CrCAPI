package pl.crystalek.crcapi.command.impl;

import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.message.api.MessageAPI;

import java.util.Map;

public abstract class SingleCommand extends Command {

    public SingleCommand(final MessageAPI messageAPI, final Map<Class<? extends Command>, CommandData> commandDataMap) {
        super(messageAPI, commandDataMap);

        setName(commandData.getCommandName());
        setAliases(commandData.getCommandAliases());
    }
}
