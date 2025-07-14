package pl.crystalek.crcapi.command;

import com.google.common.reflect.ClassPath;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.command.annotation.Command;
import pl.crystalek.crcapi.command.annotation.SubCommand;
import pl.crystalek.crcapi.command.model.CommandModel;
import pl.crystalek.crcapi.command.model.SubCommandModel;
import pl.crystalek.crcapi.command.util.ClassUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommandModelLoader {
    Logger logger;
    Set<ClassPath.ClassInfo> allClasses;
    ClassLoader classLoader;

    public Map<Class<?>, CommandModel> loadCommandModel() {
        final Map<Class<?>, CommandModel> commandModelMap = new HashMap<>();
        final Map<Class<?>, List<SubCommandModel>> subCommandModelMap = new HashMap<>();

        for (final ClassPath.ClassInfo classInfo : allClasses) {
            final Optional<Class<?>> classOptional = ClassUtils.getClassByClassInfo(classInfo, classLoader);
            if (!classOptional.isPresent()) {
                continue;
            }

            final Class<?> clazz = classOptional.get();
            if (clazz.isAnnotationPresent(Command.class)) {
                final Command command = clazz.getAnnotation(Command.class);
                final CommandModel commandModel = getCommandModel(command);
                commandModelMap.put(clazz, commandModel);
                continue;
            }

            if (clazz.isAnnotationPresent(SubCommand.class)) {
                final SubCommand subCommand = clazz.getAnnotation(SubCommand.class);
                final SubCommandModel subCommandModel = getSubCommandModel(subCommand);
                if (subCommandModelMap.containsKey(subCommand.parentCommand())) {
                    subCommandModelMap.get(subCommand.parentCommand()).add(subCommandModel);
                } else {
                    subCommandModelMap.put(subCommand.parentCommand(), Collections.singletonList(subCommandModel));
                }
            }
        }

        for (final Map.Entry<Class<?>, List<SubCommandModel>> entry : subCommandModelMap.entrySet()) {
            final Class<?> subCommandClass = entry.getKey();
            if (!commandModelMap.containsKey(subCommandClass)) {
                logger.severe("Not found parent command for: " + subCommandClass.getName());
                continue;
            }

            final CommandModel commandModel = commandModelMap.get(subCommandClass);
            entry.getValue().forEach(commandModel::addSubCommand);
        }

        return commandModelMap;
    }

    private CommandModel getCommandModel(final Command command) {
        final String name = command.name();
        final String[] aliases = command.aliases();
        final int maxArgs = command.maxArgs();
        final int minArgs = command.minArgs();
        final String permission = command.permission();
        final boolean useConsole = command.useConsole();
        final String commandUsagePath = command.commandUsagePath();

        return new CommandModel(permission, useConsole, minArgs, maxArgs, commandUsagePath, Arrays.asList(aliases), name);
    }

    private SubCommandModel getSubCommandModel(final SubCommand subCommand) {
        final int maxArgs = subCommand.maxArgs();
        final int minArgs = subCommand.minArgs();
        final String name = subCommand.name();
        final String permission = subCommand.permission();
        final boolean useConsole = subCommand.useConsole();
        final String commandUsagePath = subCommand.commandUsagePath();

        return new SubCommandModel(permission, useConsole, minArgs, maxArgs, name, commandUsagePath, subCommand.getClass());
    }
}
