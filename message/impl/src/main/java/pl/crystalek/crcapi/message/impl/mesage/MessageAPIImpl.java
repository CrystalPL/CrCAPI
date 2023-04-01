package pl.crystalek.crcapi.message.impl.mesage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crcapi.message.api.message.Message;
import pl.crystalek.crcapi.message.api.replacement.Replacement;
import pl.crystalek.crcapi.message.api.util.MessageUtil;
import pl.crystalek.crcapi.message.impl.config.Config;
import pl.crystalek.crcapi.message.impl.locale.LocaleCache;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageAPIImpl implements MessageAPI {
    MessageLoader messageLoader;
    LocaleCache localeCache;
    BukkitAudiences bukkitAudiences;

    public MessageAPIImpl(final JavaPlugin plugin, final LocaleCache localeCache, final Config config, final BukkitAudiences bukkitAudiences) {
        this.bukkitAudiences = bukkitAudiences;
        this.messageLoader = new MessageLoader(plugin, config);
        this.localeCache = localeCache;
    }

    @Override
    public boolean init() {
        return messageLoader.init();
    }

    @Override
    public <T> Optional<T> getMessage(final String messagePath, final CommandSender messageReceiver, final Class<T> messageClass) {
        final Audience audience = bukkitAudiences.sender(messageReceiver);
        final Locale locale = localeCache.getLocale(audience);

        final List<Message> messageList = messageLoader.getPlayerMessageMap(locale).get(messagePath);
        for (final Message message : messageList) {
            if (!messageClass.isAssignableFrom(message.getClass())) {
                continue;
            }

            return Optional.of(messageClass.cast(message));
        }

        return Optional.empty();
    }

    @Override
    public void sendMessage(final String messagePath, final String messageReceiver, final Replacement... replacements) {
        final Player player = Bukkit.getPlayer(messageReceiver);
        if (player == null) {
            return;
        }

        sendMessage(messagePath, player, replacements);
    }

    @Override
    public void sendMessage(final Component component, final CommandSender messageReceiver, final Replacement... replacements) {
        final Audience audience = bukkitAudiences.sender(messageReceiver);
        sendMessage(component, audience, replacements);
    }

    @Override
    public void sendMessage(final Component component, final String messageReceiver, final Replacement... replacements) {
        final Player player = Bukkit.getPlayer(messageReceiver);
        if (player == null) {
            return;
        }

        sendMessage(component, player, replacements);
    }

    @Override
    public void sendMessage(final String messagePath, final CommandSender messageReceiver, final Replacement... replacements) {
        final Audience audience = bukkitAudiences.sender(messageReceiver);
        sendMessage(messagePath, audience, replacements);
    }

    @Override
    public void broadcast(final String messagePath, final Replacement... replacements) {
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            sendMessage(messagePath, onlinePlayer, replacements);
        }
    }

    @Override
    public void broadcast(final Component component, final Replacement... replacements) {
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            sendMessage(component, onlinePlayer, replacements);
        }
    }

    private void sendMessage(final Component component, final Audience audience, final Replacement... replacements) {
        audience.sendMessage(MessageUtil.replace(component, replacements));
    }

    private void sendMessage(final String messagePath, final Audience audience, final Replacement... replacements) {
        final Locale locale = localeCache.getLocale(audience);
        final Map<String, List<Message>> playerMessageMap = messageLoader.getPlayerMessageMap(locale);
        final List<Message> messageList = playerMessageMap.get(messagePath);
        if (messageList == null) {
            audience.sendMessage(Component.text("Nie odnaleziono wiadomości: " + messagePath + ". Zgłoś błąd administratorowi."));
            return;
        }

        messageList.forEach(message -> message.sendMessage(audience, replacements));
    }
}
