package pl.crystalek.crcapi.command.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.command.CommandExecutor;
import pl.crystalek.crcapi.command.model.BaseCommandModel;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crcapi.message.api.replacement.Replacement;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public class Command implements CommandExecutor {
    MessageAPI messageAPI;
    BaseCommandModel commandModel;
    CommandExecutor commandExecutor;

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final String permission = commandModel.getPermission();
        if (StringUtils.isNotEmpty(permission) && !sender.hasPermission(permission)) {
            messageAPI.sendMessage("noPermission", sender, Replacement.of("{PERMISSION}", permission));
            return;
        }

        if (!commandModel.isUseConsole() && !(sender instanceof Player)) {
            messageAPI.sendMessage("noConsole", sender);
            return;
        }

        if (args.length < commandModel.getMinArgs() || args.length > commandModel.getMaxArgs()) {
            messageAPI.sendMessage(commandModel.getCommandUsagePath(), sender);
            return;
        }

        commandExecutor.execute(sender, args);
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String[] args) throws IllegalArgumentException {
        final String permission = commandModel.getPermission();
        if (StringUtils.isNotEmpty(permission) && !sender.hasPermission(permission)) {
            return new ArrayList<>();
        }

        return commandExecutor.tabComplete(sender, args);
    }
}
