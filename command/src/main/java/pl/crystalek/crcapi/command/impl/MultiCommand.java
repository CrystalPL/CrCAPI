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
    Map<String, SingleCommand> subCommandMap = new HashMap<>();
    Set<String> argumentList = new HashSet<>();
    CommandData commandData;

    public MultiCommand(final String commandName, final List<String> aliases, final MessageAPI messageAPI, final CommandData commandData) {
        super(commandName, aliases, messageAPI);

        this.commandData = commandData;
    }

    public MultiCommand(final CommandData commandData, final MessageAPI messageAPI) {
        this(commandData.getCommandName(), commandData.getCommandAliases(), messageAPI, commandData);
    }

    protected void registerSubCommand(final SingleCommand subCommand, final String defaultArgumentName) {
        registerSubCommand(subCommand, commandData.getArgumentList(defaultArgumentName));
    }

    protected void registerSubCommand(final SingleCommand subCommand, final List<String> argumentList) {
        argumentList.forEach(argument -> subCommandMap.put(argument, subCommand));
        this.argumentList.addAll(argumentList);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (!subCommandMap.containsKey(args[0].toLowerCase())) {
            messageAPI.sendMessage(getCommandUsagePath(), sender);
            return;
        }

        subCommandMap.get(args[0].toLowerCase()).execute(sender, args);
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String[] args) {
        if (args.length == 1) {
            return argumentList.stream().filter(argument -> argument.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }

        if (args.length > 1) {
            if (!subCommandMap.containsKey(args[0].toLowerCase())) {
                return new ArrayList<>();
            }

            return subCommandMap.get(args[0].toLowerCase()).tabComplete(sender, args);
        }

        return new ArrayList<>();
    }

    @Override
    public abstract String getPermission();

    @Override
    public abstract boolean isUseConsole();

    @Override
    public abstract String getCommandUsagePath();

    @Override
    public abstract int maxArgumentLength();

    @Override
    public abstract int minArgumentLength();
}
