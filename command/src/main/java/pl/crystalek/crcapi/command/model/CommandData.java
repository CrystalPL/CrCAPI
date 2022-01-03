package pl.crystalek.crcapi.command.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.command.impl.SingleCommand;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class CommandData {
    String commandName;
    List<String> commandAliases;
    Map<Class<? extends SingleCommand>, List<String>> subCommandMap;

    public List<String> getArgumentList(final Class<? extends SingleCommand> subCommandClass) {
        return subCommandMap.get(subCommandClass);
    }
}
