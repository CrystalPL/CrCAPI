package pl.crystalek.crcapi.command.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.command.CommandExecutor;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConfigBaseCommandData {
    String name;
    Class<? extends CommandExecutor> commandClass;
}
