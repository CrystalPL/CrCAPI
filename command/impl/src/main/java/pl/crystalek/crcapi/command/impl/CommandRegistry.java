package pl.crystalek.crcapi.command.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.command.api.BukkitCommandRegistry;
import pl.crystalek.crcapi.command.api.CommandExecutor;
import pl.crystalek.crcapi.command.impl.impl.BukkitCommand;
import pl.crystalek.crcapi.command.impl.impl.Command;
import pl.crystalek.crcapi.command.impl.impl.MultiCommand;
import pl.crystalek.crcapi.command.impl.model.CommandModel;
import pl.crystalek.crcapi.command.impl.model.SubCommandModel;
import pl.crystalek.crcapi.message.api.MessageAPI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommandRegistry {
    MessageAPI messageAPI;
    Map<Class<?>, CommandModel> commandModelMap;
    List<Object> commandObjectList;
    Logger logger;

    public void registerCommands() {
        commandModelMap.forEach((commandClass, commandModel) -> {
            final Map<String, Command> subCommandMap = getSubCommandMap(commandModel);
            if (subCommandMap.size() != commandModel.getSubCommands().size()) {
                return;
            }

            final CommandExecutor commandExecutor = getCommandExecutor(commandClass, commandModel, subCommandMap);
            final BukkitCommand bukkitCommand = new BukkitCommand(commandModel.getName(), commandModel.getAliases(), commandExecutor);
            BukkitCommandRegistry.register(bukkitCommand);
        });
    }

    private Map<String, Command> getSubCommandMap(final CommandModel commandModel) {
        final List<SubCommandModel> subCommands = commandModel.getSubCommands();
        final Map<String, Command> subCommandMap = new HashMap<>();
        for (final SubCommandModel subCommand : subCommands) {
            final Optional<CommandExecutor> subCommandObjectOptional = getObjectByClass(subCommand.getSubCommandClass()).map(obj -> (CommandExecutor) obj);
            if (!subCommandObjectOptional.isPresent()) {
                logger.severe("Not found object for: " + subCommand.getSubCommandClass().getName());
                break;
            }

            final CommandExecutor subcommandObject = subCommandObjectOptional.get();
            final Command command = new Command(messageAPI, subCommand, subcommandObject);
            subCommandMap.put(subCommand.getName(), command);
        }

        return subCommandMap;
    }

    private CommandExecutor getCommandExecutor(final Class<?> commandClass, final CommandModel commandModel, final Map<String, Command> subCommandMap) {
        final CommandExecutor commandExecutor;
        if (subCommandMap.isEmpty()) {
            final Optional<Object> objectByClassOptional = getObjectByClass(commandClass);
            if (!objectByClassOptional.isPresent()) {
                logger.severe("Not found object for: " + commandClass.getName());
                return null;
            }

            commandExecutor = (CommandExecutor) objectByClassOptional.get();
        } else {
            commandExecutor = getMultiCommand(commandModel, subCommandMap);
        }

        return commandExecutor;
    }

    private Optional<Object> getObjectByClass(final Class<?> clazz) {
        return commandObjectList.stream()
                .filter(obj -> obj.getClass().equals(clazz))
                .findFirst();
    }

    private Command getMultiCommand(final CommandModel commandModel, final Map<String, Command> subCommandMap) {
        final MultiCommand multiCommand = new MultiCommand(commandModel, messageAPI, subCommandMap);
        return new Command(messageAPI, commandModel, multiCommand);
    }
}
