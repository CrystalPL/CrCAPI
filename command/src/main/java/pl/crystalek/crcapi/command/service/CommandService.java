package pl.crystalek.crcapi.command.service;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.command.model.ICommand;
import pl.crystalek.crcapi.message.api.MessageAPI;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommandService {
    ICommand command;
    MessageAPI messageAPI;

    public void execute(final CommandSender sender, final String[] args) {
        if (!sender.hasPermission(command.getPermission())) {
            messageAPI.sendMessage("noPermission", sender, ImmutableMap.of("{PERMISSION}", command.getPermission()));
            return;
        }

        if (!command.isUseConsole() && !(sender instanceof Player)) {
            messageAPI.sendMessage("noConsole", sender);
            return;
        }

        if (args.length < command.minArgumentLength() || args.length > command.maxArgumentLength()) {
            messageAPI.sendMessage(command.getCommandUsagePath(), sender);
            return;
        }

        command.execute(sender, args);
    }

    public List<String> tabComplete(final CommandSender sender, final String[] args) throws IllegalArgumentException {
        if (!sender.hasPermission(command.getPermission())) {
            return new ArrayList<>();
        }

        return command.tabComplete(sender, args);
    }
}
