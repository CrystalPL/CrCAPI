package pl.crystalek.crcapi.message.impl.manager;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.api.MessageType;
import pl.crystalek.crcapi.message.impl.loader.LocalizedMessageLoader;
import pl.crystalek.crcapi.message.impl.user.UserCache;

import java.util.Map;
import java.util.Optional;

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
    Optional<Component> getComponent(final String messagePath, final Audience audience, final MessageType messageType) {
        return getComponent(messageLoader.getPlayerMessageMap(UserCache.getLocale(audience)), messagePath, messageType);
    }

    @Override
    public boolean init() {
        return messageLoader.init();
    }
}
