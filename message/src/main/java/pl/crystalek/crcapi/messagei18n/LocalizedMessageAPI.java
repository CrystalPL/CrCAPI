package pl.crystalek.crcapi.messagei18n;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.Message;
import pl.crystalek.crcapi.message.MessageAPI;
import pl.crystalek.crcapi.messagei18n.loader.LocalizedMessageLoader;
import pl.crystalek.crcapi.messagei18n.user.UserCache;

import java.util.Map;
import java.util.Optional;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class LocalizedMessageAPI extends MessageAPI {
    LocalizedMessageLoader messageLoader;

    public LocalizedMessageAPI(final JavaPlugin plugin) {
        this.messageLoader = new LocalizedMessageLoader(plugin);
    }

    @Override
    public void sendMessage(final String messagePath, final Audience audience, final Map<String, Object> replacements) {
        sendMessage(messageLoader.getPlayerMessageMap(UserCache.getLocale(audience)), messagePath, audience, replacements);
    }

    @Override
    public void sendMessageComponent(final String messagePath, final Audience audience, final Map<String, Component> replacements) {
        sendMessageComponent(messageLoader.getPlayerMessageMap(UserCache.getLocale(audience)), messagePath, audience, replacements);
    }

    @Override
    public Optional<Component> getComponent(final String messagePath, final Audience audience, final Class<? extends Message> clazz) {
        return getComponent(messageLoader.getPlayerMessageMap(UserCache.getLocale(audience)), messagePath, clazz);
    }

    @Override
    public boolean init() {
        return messageLoader.init();
    }
}
