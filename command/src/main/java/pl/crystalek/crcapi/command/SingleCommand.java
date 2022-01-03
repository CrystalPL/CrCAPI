package pl.crystalek.crcapi.command;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.command.model.ICommand;
import pl.crystalek.crcapi.command.service.CommandService;
import pl.crystalek.crcapi.message.api.MessageAPI;

import java.util.List;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SingleCommand extends Command {
    CommandService commandService;

    public SingleCommand(final String commandName, final List<String> aliases, final CommandService commandService) {
        super(commandName);
        setAliases(aliases);

        this.commandService = commandService;

        CommandRegistry.register(this);
    }

    public SingleCommand(final CommandData commandData, final CommandService commandService) {
        this(commandData.getCommandName(), commandData.getCommandAliases(), commandService);
    }

    public SingleCommand(final CommandData commandData, final ICommand command, final MessageAPI messageAPI) {
        this(commandData, new CommandService(command, messageAPI));
    }

    public SingleCommand(final String commandName, final List<String> aliases, final ICommand command, final MessageAPI messageAPI) {
        this(commandName, aliases, new CommandService(command, messageAPI));
    }

    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        commandService.execute(sender, args);
        return true;
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) throws IllegalArgumentException {
        return commandService.tabComplete(sender, args);
    }
}
