package pl.crystalek.crcapi.command.impl.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BaseCommandModel {
    String permission;
    boolean useConsole;
    int minArgs;
    int maxArgs;
    String commandUsagePath;
    @NonFinal
    List<String> aliases;
    @NonFinal
    String name;
}
