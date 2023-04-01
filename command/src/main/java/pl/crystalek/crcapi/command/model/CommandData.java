package pl.crystalek.crcapi.command.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.command.impl.Command;

import java.util.List;
import java.util.Map;

/**
 * A class representing data about a command, including its name, aliases, and sub-commands.
 */
@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class CommandData {
    String commandName;
    List<String> commandAliases;
    Map<Class<? extends Command>, List<String>> subCommandMap;

    /**
     * Gets the list of sub-command arguments.
     *
     * @param subCommandClass the sub-command class to get arguments for
     * @return the list of arguments of given sub-command
     */
    public List<String> getArgumentList(final Class<? extends Command> subCommandClass) {
        return subCommandMap.get(subCommandClass);
    }
}
