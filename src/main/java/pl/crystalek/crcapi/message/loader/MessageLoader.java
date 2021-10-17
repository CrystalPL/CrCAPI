package pl.crystalek.crcapi.message.loader;

import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.config.FileHelper;
import pl.crystalek.crcapi.message.Message;
import pl.crystalek.crcapi.message.exception.MessageLoadException;
import pl.crystalek.crcapi.message.impl.ActionBarMessage;
import pl.crystalek.crcapi.message.impl.BossBarMessage;
import pl.crystalek.crcapi.message.impl.ChatMessage;
import pl.crystalek.crcapi.message.impl.TitleMessage;
import pl.crystalek.crcapi.util.LogUtil;

import java.io.IOException;
import java.util.*;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MessageLoader {
    JavaPlugin plugin;
    FileHelper fileHelper;
    @Getter
    Map<String, List<Message>> messageMap = new HashMap<>();

    public boolean init() {
        try {
            fileHelper.checkExist();
        } catch (final IOException exception) {
            LogUtil.error("Nie udało się utworzyć pliku lub folderu pluginu");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }

        fileHelper.load();
        loadMessage(fileHelper.getConfiguration());
        return true;
    }

    private void loadMessage(final FileConfiguration configuration) {
        final Set<String> configNameMessageSection = ImmutableSet.of("chat", "actionbar", "title", "bossbar");

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
    }

    private List<Message> getMessage(final ConfigurationSection messageConfiguration) {
        final List<Message> messageList = new ArrayList<>();

        //chat configurer
        if (messageConfiguration.contains("chat")) {
            try {
                final ChatMessage chatMessage = ChatMessage.loadChatMessage(messageConfiguration);
                messageList.add(chatMessage);
            } catch (final MessageLoadException exception) {
                LogUtil.error("Wystąpił problem podczas ładowania wiadomości wysyłanej na czacie w sekcji: " + messageConfiguration.getName());
                LogUtil.error(exception.getMessage());
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
                LogUtil.error("Wystąpił problem podczas ładowania wiadomości wyświetlanej na środku ekranu w sekcji: " + messageConfiguration.getName());
                LogUtil.error(exception.getMessage());
            }

        }

        //bossbar configurer
        if (messageConfiguration.contains("bossbar")) {
            try {
                final BossBarMessage bossBarMessage = BossBarMessage.loadBossBar(messageConfiguration, plugin);
                messageList.add(bossBarMessage);
            } catch (final MessageLoadException exception) {
                LogUtil.error("Wystąpił problem podczas ładowania wiadomości wyświetlanej na pasku smoka w sekcji: " + messageConfiguration.getName());
                LogUtil.error(exception.getMessage());
            }
        }

        return messageList;
    }
}
