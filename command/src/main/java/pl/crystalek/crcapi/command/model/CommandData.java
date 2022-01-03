package pl.crystalek.crcapi.command.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class CommandData {
    String commandName;
    List<String> commandAliases;
    //(default) subcommand name -> new subcommand names
    Map<String, List<String>> subCommandMap;

    public List<String> getArgumentList(final String defaultArgumentName) {
        return subCommandMap.get(defaultArgumentName);
    }
}
