package pl.crystalek.crcapi.message.impl.mesage;

import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.LocaleUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.api.message.Message;
import pl.crystalek.crcapi.message.impl.config.Config;
import pl.crystalek.crcapi.message.impl.exception.MessageLoadException;
import pl.crystalek.crcapi.message.impl.mesage.impl.ActionBarMessage;
import pl.crystalek.crcapi.message.impl.mesage.impl.BossBarMessage;
import pl.crystalek.crcapi.message.impl.mesage.impl.ChatMessage;
import pl.crystalek.crcapi.message.impl.mesage.impl.TitleMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public class MessageLoader {
    Map<Locale, Map<String, List<Message>>> localeMessageMap = new HashMap<>();
    JavaPlugin plugin;
    Config config;

    public Map<String, List<Message>> getPlayerMessageMap(final Locale locale) {
        final Map<String, List<Message>> messageMap = localeMessageMap.get(locale);
        if (messageMap != null) {
            return messageMap;
        }

        return localeMessageMap.get(config.getDefaultLanguage());
    }

    private Map<String, List<Message>> loadMessage(final ConfigurationSection configurationSection) {
        final Map<String, List<Message>> messageMap = new HashMap<>();
        final Set<String> nameSettings = ImmutableSet.of("chat", "actionbar", "title", "bossbar");

        for (final String messageName : configurationSection.getKeys(false)) {
            if (!nameSettings.contains(messageName)) {
                messageMap.putAll(loadMessage(configurationSection.getConfigurationSection(messageName)));
                continue;
            }

            final Optional<Message> messageOptional = getMessage(configurationSection, messageName);
            if (!messageOptional.isPresent()) {
                continue;
            }

            final String currentPath = configurationSection.getCurrentPath();
            final Message message = messageOptional.get();
            if (messageMap.containsKey(currentPath)) {
                messageMap.get(currentPath).add(message);
            } else {
                final List<Message> messageList = new ArrayList<>();
                messageList.add(message);
                messageMap.put(currentPath, messageList);
            }
        }

        return messageMap;
    }

    private Optional<Message> getMessage(final ConfigurationSection messageConfiguration, final String settingsName) {
        //chat configurer
        if (settingsName.equals("chat")) {
            try {
                return Optional.of(ChatMessage.loadChatMessage(messageConfiguration));
            } catch (final MessageLoadException exception) {
                plugin.getLogger().severe("Wystąpił problem podczas ładowania wiadomości wysyłanej na czacie w sekcji: " + messageConfiguration.getName());
                plugin.getLogger().severe(exception.getMessage());
                return Optional.empty();
            }
        }

        //actionbar configurer
        if (settingsName.equals("actionbar")) {
            return Optional.of(ActionBarMessage.loadActionBar(messageConfiguration));
        }

        //title configurer
        if (settingsName.equals("title")) {
            try {
                return Optional.of(TitleMessage.loadTitleMessage(messageConfiguration));
            } catch (final MessageLoadException exception) {
                plugin.getLogger().severe("Wystąpił problem podczas ładowania wiadomości wyświetlanej na środku ekranu w sekcji: " + messageConfiguration.getName());
                plugin.getLogger().severe(exception.getMessage());
                return Optional.empty();
            }
        }

        //bossbar configurer
        if (settingsName.equals("bossbar")) {
            try {
                return Optional.of(BossBarMessage.loadBossBar(messageConfiguration, plugin));
            } catch (final MessageLoadException exception) {
                plugin.getLogger().severe(exception.getMessage());
                plugin.getLogger().severe("Wystąpił problem podczas ładowania wiadomości wyświetlanej na pasku smoka w sekcji: " + messageConfiguration.getName());
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    public boolean init() {
        final File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            plugin.getLogger().severe("Failed to create plugin directory!");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }

        final File messageFolder = new File(dataFolder, "lang");
        if (!messageFolder.exists() && !messageFolder.mkdirs()) {
            plugin.getLogger().severe("Failed to create lang directory!");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }

        final File[] languages = messageFolder.listFiles();
        if (languages == null || languages.length == 0) {
            plugin.getLogger().severe("Not found messages files!");
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
                plugin.getLogger().severe("Not found language: " + messageFile.getName());
                Bukkit.getPluginManager().disablePlugin(plugin);
                return false;
            }

            final Map<String, List<Message>> localeMessageMap = loadMessage(YamlConfiguration.loadConfiguration(messageFile));
            this.localeMessageMap.put(locale, localeMessageMap);
            Bukkit.getLogger().info("[CrCAPI] Loaded language: " + removeExtension(messageFile.getName()));
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
