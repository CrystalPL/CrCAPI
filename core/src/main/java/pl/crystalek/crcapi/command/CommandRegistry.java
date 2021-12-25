package pl.crystalek.crcapi.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;

@UtilityClass
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommandRegistry {
    @Getter
    CommandMap commandMap;

    static {
        try {
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (final NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    public void register(final Command command) {
        commandMap.register(command.getName(), command);
    }
}
