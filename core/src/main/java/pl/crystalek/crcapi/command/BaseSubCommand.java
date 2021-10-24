package pl.crystalek.crcapi.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface BaseSubCommand {

    void execute(final CommandSender sender, final String[] args);

    int maxArgumentLength();

    int minArgumentLength();

    String getPermission();

    List<String> tabComplete(final CommandSender sender, final String[] args);
}
