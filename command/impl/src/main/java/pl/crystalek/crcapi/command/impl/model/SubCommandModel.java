package pl.crystalek.crcapi.command.impl.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SubCommandModel extends BaseCommandModel {
    Class<?> subCommandClass;

    public SubCommandModel(final String permission, final boolean useConsole, final int minArgs, final int maxArgs, final String name, final String commandUsagePath, final Class<?> subCommandClass) {
        super(permission, useConsole, minArgs, maxArgs, name, new ArrayList<>(), commandUsagePath);

        this.subCommandClass = subCommandClass;
    }
}
