package pl.crystalek.crcapi.command.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import pl.crystalek.crcapi.command.CommandExecutor;
import pl.crystalek.crcapi.command.model.CommandModel;
import pl.crystalek.crcapi.message.api.MessageAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MultiCommand implements CommandExecutor {
    CommandModel mainCommandModel;
    MessageAPI messageAPI;
    Map<String, Command> subCommandMap;
    Set<String> argumentList;

    public MultiCommand(final CommandModel mainCommandModel, final MessageAPI messageAPI, final Map<String, Command> subCommandMap) {
        this.mainCommandModel = mainCommandModel;
        this.messageAPI = messageAPI;
        this.subCommandMap = subCommandMap;

        this.argumentList = subCommandMap.keySet();
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (!subCommandMap.containsKey(args[0].toLowerCase())) {
            messageAPI.sendMessage(mainCommandModel.getCommandUsagePath(), sender);
            return;
        }

        subCommandMap.get(args[0].toLowerCase()).execute(sender, args);
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String[] args) {
        if (args.length == 1) {
            return argumentList.stream()
                    .filter(argument -> StringUtils.startsWithIgnoreCase(argument, args[0]))
                    .collect(Collectors.toList());
        }

        if (args.length > 1) {
            if (!argumentList.contains(args[0].toLowerCase())) {
                return new ArrayList<>();
            }

            return subCommandMap.get(args[0].toLowerCase()).tabComplete(sender, args);
        }

        return new ArrayList<>();
    }
}
