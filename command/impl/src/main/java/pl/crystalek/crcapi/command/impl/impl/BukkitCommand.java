package pl.crystalek.crcapi.command.impl.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.crystalek.crcapi.command.api.CommandExecutor;

import java.util.List;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BukkitCommand extends Command {
    CommandExecutor commandExecutor;

    public BukkitCommand(final String name, final List<String> aliases, final CommandExecutor commandExecutor) {
        super(name);

        setAliases(aliases);

        this.commandExecutor = commandExecutor;
    }

    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        commandExecutor.execute(sender, args);
        return true;
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) throws IllegalArgumentException {
        return commandExecutor.tabComplete(sender, args);
    }
}
