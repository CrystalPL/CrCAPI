package pl.crystalek.crcapi.command;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.command.impl.BukkitCommand;
import pl.crystalek.crcapi.command.impl.Command;
import pl.crystalek.crcapi.command.impl.MultiCommand;
import pl.crystalek.crcapi.command.model.CommandModel;
import pl.crystalek.crcapi.command.model.SubCommandModel;
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
        for (final Map.Entry<Class<?>, CommandModel> entry : commandModelMap.entrySet()) {
            final Class<?> commandClass = entry.getKey();
            final Optional<CommandExecutor> objectByClassOptional = getObjectByClass(commandClass);
            if (!objectByClassOptional.isPresent()) {
                logger.severe("Not found object for: " + commandClass.getName());
                continue;
            }

            final CommandModel commandModel = entry.getValue();
            final Map<String, Command> subCommandMap = getSubCommandMap(commandModel);
            if (subCommandMap.size() != commandModel.getSubCommands().size()) {
                return;
            }

            final CommandExecutor commandExecutor = subCommandMap.isEmpty() ? objectByClassOptional.get() : new MultiCommand(commandModel, messageAPI, subCommandMap);
            final BukkitCommand bukkitCommand = new BukkitCommand(commandModel.getName(), commandModel.getAliases(), commandExecutor);
            BukkitCommandRegistry.register(bukkitCommand);
        }
    }

    private Map<String, Command> getSubCommandMap(final CommandModel commandModel) {
        final List<SubCommandModel> subCommands = commandModel.getSubCommands();
        final Map<String, Command> subCommandMap = new HashMap<>();
        for (final SubCommandModel subCommand : subCommands) {
            final Optional<CommandExecutor> subCommandObjectOptional = getObjectByClass(subCommand.getSubCommandClass());
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

    private Optional<CommandExecutor> getObjectByClass(final Class<?> clazz) {
        return commandObjectList.stream()
                .filter(obj -> obj.getClass().equals(clazz))
                .filter(obj -> obj.getClass().isAssignableFrom(CommandExecutor.class))
                .map(obj -> ((CommandExecutor) obj))
                .findFirst();
    }
}
