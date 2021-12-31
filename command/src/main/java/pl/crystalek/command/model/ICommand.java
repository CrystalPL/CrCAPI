package pl.crystalek.command.model;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface ICommand {

    void execute(final CommandSender sender, final String[] args);

    List<String> tabComplete(final CommandSender sender, final String[] args);

    boolean isUseConsole();

    String getCommandUsagePath();

    int maxArgumentLength();

    int minArgumentLength();

    String getPermission();

}
