package pl.crystalek.command;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.crystalek.command.model.CommandData;
import pl.crystalek.command.model.ICommand;
import pl.crystalek.command.service.CommandService;
import pl.crystalek.crcapi.message.MessageAPI;

import java.util.*;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MultiCommand extends Command {
    Map<String, CommandService> subCommandMap = new HashMap<>();
    MessageAPI messageAPI;
    String mainCommandUsagePath;
    @NonFinal
    Set<String> argumentList = new HashSet<>();

    public MultiCommand(final String commandName, final List<String> aliases, final MessageAPI messageAPI, final String mainCommandUsagePath) {
        super(commandName);
        setAliases(aliases);

        this.messageAPI = messageAPI;
        this.mainCommandUsagePath = mainCommandUsagePath;

        CommandRegistry.register(this);
    }

    public MultiCommand(final CommandData commandData, final MessageAPI messageAPI, final String mainCommandUsagePath) {
        this(commandData.getCommandName(), commandData.getCommandAliases(), messageAPI, mainCommandUsagePath);
    }

    public void registerSubCommand(final ICommand subCommand, final List<String> argumentList) {
        argumentList.forEach(argument -> subCommandMap.put(argument, new CommandService(subCommand, messageAPI)));
        this.argumentList = subCommandMap.keySet();
    }

    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if (!subCommandMap.containsKey(args[0].toLowerCase())) {
            messageAPI.sendMessage(mainCommandUsagePath, sender);
            return true;
        }

        subCommandMap.get(args[0].toLowerCase()).execute(sender, args);
        return true;
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) throws IllegalArgumentException {
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
}
