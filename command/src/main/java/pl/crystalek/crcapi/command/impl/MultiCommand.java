package pl.crystalek.crcapi.command.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.CommandSender;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.message.api.MessageAPI;

import java.util.*;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class MultiCommand extends SingleCommand {
    Map<String, Command> subCommandMap = new HashMap<>();
    Set<String> argumentList = new HashSet<>();

    public MultiCommand(final MessageAPI messageAPI, final Map<Class<? extends Command>, CommandData> commandDataMap) {
        super(messageAPI, commandDataMap);
    }

    protected void registerSubCommand(final Command subCommand) {
        registerSubCommand(subCommand, true);
    }

    public void registerSubCommand(final Command subCommand, final boolean addToArgumentList) {
        final List<String> argumentList = commandData.getArgumentList(subCommand.getClass());
        argumentList.forEach(argument -> subCommandMap.put(argument, subCommand));

        if (addToArgumentList) {
            this.argumentList.addAll(argumentList);
        }
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (!argumentList.contains(args[0].toLowerCase())) {
            messageAPI.sendMessage(getCommandUsagePath(), sender);
            return;
        }

        subCommandMap.get(args[0].toLowerCase()).execute(sender, null, args);
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String[] args) {
        if (args.length == 1) {
            return argumentList.stream().filter(argument -> argument.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
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
