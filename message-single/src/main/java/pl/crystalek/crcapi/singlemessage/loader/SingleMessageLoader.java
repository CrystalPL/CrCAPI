package pl.crystalek.crcapi.singlemessage.loader;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.config.FileHelper;
import pl.crystalek.crcapi.message.Message;
import pl.crystalek.crcapi.message.loader.MessageLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class SingleMessageLoader extends MessageLoader {
    FileHelper fileHelper;
    @Getter
    Map<String, List<Message>> messageMap = new HashMap<>();

    public SingleMessageLoader(final JavaPlugin plugin) {
        super(plugin);

        this.fileHelper = new FileHelper("messages.yml", plugin);
    }

    @Override
    public boolean init() {
        try {
            fileHelper.checkExist();
        } catch (final IOException exception) {
            plugin.getLogger().severe("Nie udało się utworzyć pliku lub folderu pluginu!");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }

        fileHelper.load();
        loadMessage(fileHelper.getConfiguration());
        return true;
    }
}
