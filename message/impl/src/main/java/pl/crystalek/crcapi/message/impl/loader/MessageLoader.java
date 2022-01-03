package pl.crystalek.crcapi.message.impl.loader;

import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.api.Message;
import pl.crystalek.crcapi.message.impl.exception.MessageLoadException;
import pl.crystalek.crcapi.message.impl.loader.message.ActionBarMessage;
import pl.crystalek.crcapi.message.impl.loader.message.BossBarMessage;
import pl.crystalek.crcapi.message.impl.loader.message.ChatMessage;
import pl.crystalek.crcapi.message.impl.loader.message.TitleMessage;

import java.util.*;

@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@RequiredArgsConstructor
abstract class MessageLoader {
    JavaPlugin plugin;

    abstract boolean init();

    Map<String, List<Message>> loadMessage(final FileConfiguration configuration) {
        final Set<String> configNameMessageSection = ImmutableSet.of("chat", "actionbar", "title", "bossbar");
        final Map<String, List<Message>> messageMap = new HashMap<>();

        for (final String configurationSectionName : configuration.getKeys(false)) {
            final ConfigurationSection messageConfigurationSection = configuration.getConfigurationSection(configurationSectionName);

            final Set<String> keys = messageConfigurationSection.getKeys(false);
            if (keys.stream().noneMatch(configNameMessageSection::contains)) {
                for (final String subMessageConfigurationSectionName : keys) {
                    final ConfigurationSection subMessageConfigurationSection = messageConfigurationSection.getConfigurationSection(subMessageConfigurationSectionName);

                    messageMap.put(configurationSectionName + "." + subMessageConfigurationSectionName, getMessage(subMessageConfigurationSection));
                }
            } else {
                messageMap.put(configurationSectionName, getMessage(messageConfigurationSection));
            }
        }

        return messageMap;
    }

    private List<Message> getMessage(final ConfigurationSection messageConfiguration) {
        final List<Message> messageList = new ArrayList<>();

        //chat configurer
        if (messageConfiguration.contains("chat")) {
            try {
                final ChatMessage chatMessage = ChatMessage.loadChatMessage(messageConfiguration);
                messageList.add(chatMessage);
            } catch (final MessageLoadException exception) {
                plugin.getLogger().severe("Wystąpił problem podczas ładowania wiadomości wysyłanej na czacie w sekcji: " + messageConfiguration.getName());
                plugin.getLogger().severe(exception.getMessage());
            }
        }

        //actionbar configurer
        if (messageConfiguration.contains("actionbar")) {
            final ActionBarMessage actionBarMessage = ActionBarMessage.loadActionBar(messageConfiguration);
            messageList.add(actionBarMessage);
        }

        //title configurer
        if (messageConfiguration.contains("title")) {
            try {
                final TitleMessage titleMessage = TitleMessage.loadTitleMessage(messageConfiguration);
                messageList.add(titleMessage);
            } catch (final MessageLoadException exception) {
                plugin.getLogger().severe("Wystąpił problem podczas ładowania wiadomości wyświetlanej na środku ekranu w sekcji: " + messageConfiguration.getName());
                plugin.getLogger().severe(exception.getMessage());
            }
        }

        //bossbar configurer
        if (messageConfiguration.contains("bossbar")) {
            try {
                final BossBarMessage bossBarMessage = BossBarMessage.loadBossBar(messageConfiguration, plugin);
                messageList.add(bossBarMessage);
            } catch (final MessageLoadException exception) {
                plugin.getLogger().severe(exception.getMessage());
                plugin.getLogger().severe("Wystąpił problem podczas ładowania wiadomości wyświetlanej na pasku smoka w sekcji: " + messageConfiguration.getName());
            }
        }

        return messageList;
    }
}
