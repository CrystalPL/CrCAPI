package pl.crystalek.crcapi.command.model;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public interface ICommand {

    void execute(final CommandSender sender, final String[] args);

    default List<String> tabComplete(final CommandSender sender, final String[] args) {
        return new ArrayList<>();
    }

    boolean isUseConsole();

    default String getCommandUsagePath() {
        return "";
    }

    int maxArgumentLength();

    int minArgumentLength();

    default String getPermission() {
        return "";
    }

}
