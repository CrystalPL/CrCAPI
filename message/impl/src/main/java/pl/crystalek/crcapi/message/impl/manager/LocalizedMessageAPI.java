package pl.crystalek.crcapi.message.impl.manager;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.impl.loader.LocalizedMessageLoader;
import pl.crystalek.crcapi.message.impl.user.UserCache;

import java.util.*;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class LocalizedMessageAPI extends MessageAPIImpl {
    LocalizedMessageLoader messageLoader;

    public LocalizedMessageAPI(final JavaPlugin plugin) {
        this.messageLoader = new LocalizedMessageLoader(plugin);
    }

    @Override
    void sendMessage(final String messagePath, final Audience audience, final Map<String, Object> replacements) {
        sendMessage(messageLoader.getPlayerMessageMap(UserCache.getLocale(audience)), messagePath, audience, replacements);
    }

    @Override
    void sendMessageComponent(final String messagePath, final Audience audience, final Map<String, Component> replacements) {
        sendMessageComponent(messageLoader.getPlayerMessageMap(UserCache.getLocale(audience)), messagePath, audience, replacements);
    }

    @Override
    <T> Optional<T> getMessage(final String messagePath, final Audience audience, final Class<T> messageClass) {
        return getComponent(messageLoader.getPlayerMessageMap(UserCache.getLocale(audience)), messagePath, messageClass);
    }

    @Override
    public boolean init() {
        return messageLoader.init();
    }

    @Override
    public void broadcast(final String messagePath, final Map<String, Object> replacements) {
        for (final Map.Entry<Audience, Locale> entry : UserCache.getUserLocaleMap().entrySet()) {
            sendMessage(messageLoader.getPlayerMessageMap(entry.getValue()), messagePath, entry.getKey(), replacements);
        }
    }

    @Override
    public void broadcast(final String messagePath) {
        broadcast(messagePath, ImmutableMap.of());
    }

    @Override
    public List<Locale> getSupportedLanguages() {
        return new ArrayList<>(messageLoader.getLocaleMessageMap().keySet());
    }
}
