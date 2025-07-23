package pl.crystalek.crcapi.command.impl.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.command.api.CommandExecutor;

import java.util.List;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConfigCommandData extends ConfigBaseCommandData {
    List<String> aliases;
    List<ConfigBaseCommandData> subCommands;

    public ConfigCommandData(final String name, final Class<? extends CommandExecutor> commandClass, final List<String> aliases, final List<ConfigBaseCommandData> subCommands) {
        super(name, commandClass);

        this.aliases = aliases;
        this.subCommands = subCommands;
    }
}
