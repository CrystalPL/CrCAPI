package pl.crystalek.crcapi.command.impl;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.message.api.MessageAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public abstract class SingleCommand extends Command {
    MessageAPI messageAPI;
    CommandData commandData;

    public SingleCommand(final MessageAPI messageAPI, final Map<Class<? extends SingleCommand>, CommandData> commandDataMap) {
        super("");

        this.messageAPI = messageAPI;
        this.commandData = commandDataMap.get(getClass());

        setName(commandData.getCommandName());
        setAliases(commandData.getCommandAliases());
    }

    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if (!getPermission().equals("") && !sender.hasPermission(getPermission())) {
            messageAPI.sendMessage("noPermission", sender, ImmutableMap.of("{PERMISSION}", getPermission()));
            return true;
        }

        if (!isUseConsole() && !(sender instanceof Player)) {
            messageAPI.sendMessage("noConsole", sender);
            return true;
        }

        if (args.length < minArgumentLength() || args.length > maxArgumentLength()) {
            messageAPI.sendMessage(getCommandUsagePath(), sender);
            return true;
        }

        execute(sender, args);
        return true;
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) throws IllegalArgumentException {
        if (!sender.hasPermission(getPermission())) {
            return new ArrayList<>();
        }

        return tabComplete(sender, args);
    }

    public abstract void execute(final CommandSender sender, final String[] args);

    @Override
    public abstract List<String> tabComplete(final CommandSender sender, final String[] args);

    @Override
    public abstract String getPermission();

    public abstract boolean isUseConsole();

    public abstract String getCommandUsagePath();

    public abstract int maxArgumentLength();

    public abstract int minArgumentLength();
}
