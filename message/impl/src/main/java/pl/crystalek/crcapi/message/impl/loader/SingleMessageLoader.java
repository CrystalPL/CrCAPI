package pl.crystalek.crcapi.message.impl.loader;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.core.config.FileHelper;
import pl.crystalek.crcapi.message.api.message.Message;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SingleMessageLoader extends MessageLoader {
    final FileHelper fileHelper;
    @Getter
    Map<String, List<Message>> messageMap;

    public SingleMessageLoader(final JavaPlugin plugin) {
        super(plugin);

        this.fileHelper = new FileHelper(plugin, "messages.yml");
    }

    @Override
    public boolean init() {
        try {
            fileHelper.checkExist();
        } catch (final IOException exception) {
            plugin.getLogger().severe("Failed to create file or plugin directory!");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }

        fileHelper.load();
        this.messageMap = loadMessage(fileHelper.getConfiguration());
        return true;
    }
}
