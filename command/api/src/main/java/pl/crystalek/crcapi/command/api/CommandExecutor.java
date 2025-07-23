package pl.crystalek.crcapi.command.api;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Interface defining basic methods for commands and subcommands
 */
public interface CommandExecutor {
    /**
     * Executes a command
     *
     * @param sender the command sender
     * @param args   command arguments
     */
    void execute(CommandSender sender, String[] args);

    /**
     * Returns tab completion suggestions for command arguments
     *
     * @param sender the command sender
     * @param args   command arguments
     * @return list of tab completion suggestions
     */
    List<String> tabComplete(CommandSender sender, String[] args);
}
