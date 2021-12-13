package pl.crystalek.crcapi.messagei18n.loader;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.LocaleUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.Message;
import pl.crystalek.crcapi.message.loader.MessageLoader;
import pl.crystalek.crcapi.messagei18n.config.Config;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class LocalizedMessageLoader extends MessageLoader {
    Map<Locale, Map<String, List<Message>>> localeMessageMap = new HashMap<>();

    public LocalizedMessageLoader(final JavaPlugin plugin) {
        super(plugin);
    }

    public Map<String, List<Message>> getPlayerMessageMap(final Locale locale) {
        final Map<String, List<Message>> messageMap = localeMessageMap.get(locale);
        if (messageMap != null) {
            return messageMap;
        }

        return localeMessageMap.get(Config.getDefaultLocale());
    }

    @Override
    public boolean init() {
        final File dataFolder = this.plugin.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            plugin.getLogger().severe("Nie udało się utworzyć folderu pluginu!");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }

        final File messageFolder = new File(dataFolder, "lang");
        if (!messageFolder.exists() && !messageFolder.mkdirs()) {
            plugin.getLogger().severe("Nie udało się utworzyć folderu lang!");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }

        final File[] languages = messageFolder.listFiles();
        if (languages == null || languages.length == 0) {
            plugin.getLogger().severe("Nie odnaleziono plików z wiadomościami");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }

        for (final File messageFile : languages) {
            final Locale locale = LocaleUtils.toLocale(removeExtension(messageFile.getName()));
            try {
                if (locale.getCountry().isEmpty() || locale.getCountry().equals(" ")) {
                    throw new IllegalArgumentException();
                }
            } catch (final IllegalArgumentException exception) {
                plugin.getLogger().severe("Nie odnaleziono języka: " + messageFile.getName());
                Bukkit.getPluginManager().disablePlugin(plugin);
                return false;
            }

            final Map<String, List<Message>> localeMessageMap = loadMessage(YamlConfiguration.loadConfiguration(messageFile));
            this.localeMessageMap.put(locale, localeMessageMap);
            plugin.getLogger().info("Załadowano język: " + messageFile.getName());
        }

        return true;
    }

    private String removeExtension(final String name) {

        String separator = System.getProperty("file.separator");
        String filename;

        // Remove the path upto the filename.
        int lastSeparatorIndex = name.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = name;
        } else {
            filename = name.substring(lastSeparatorIndex + 1);
        }

        // Remove the extension.
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex == -1)
            return filename;

        return filename.substring(0, extensionIndex);
    }
}
