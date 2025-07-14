package pl.crystalek.crcapi.command.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommandModel extends BaseCommandModel {
    List<SubCommandModel> subCommands = new ArrayList<>();

    public CommandModel(final String permission, final boolean useConsole, final int minArgs, final int maxArgs, final String commandUsagePath, final List<String> aliases, final String name) {
        super(permission, useConsole, minArgs, maxArgs, commandUsagePath, aliases, name);
    }

    public void addSubCommand(final SubCommandModel subCommandModel) {
        subCommands.add(subCommandModel);
    }
}
