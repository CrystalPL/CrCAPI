package pl.crystalek.crcapi.message.impl.manager;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.api.Message;
import pl.crystalek.crcapi.message.impl.loader.SingleMessageLoader;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SingleMessageAPI extends MessageAPIImpl {
    final SingleMessageLoader messageLoader;
    Map<String, List<Message>> messageMap;

    public SingleMessageAPI(final JavaPlugin plugin) {
        this.messageLoader = new SingleMessageLoader(plugin);
    }

    @Override
    void sendMessage(final String messagePath, final Audience audience, final Map<String, Object> replacements) {
        sendMessage(messageMap, messagePath, audience, replacements);
    }

    @Override
    void sendMessageComponent(final String messagePath, final Audience audience, final Map<String, Component> replacements) {
        sendMessageComponent(messageMap, messagePath, audience, replacements);
    }

    @Override
    Optional<Component> getComponent(final String messagePath, final Audience audience, final Class<? extends Message> clazz) {
        return getComponent(messageMap, messagePath, clazz);
    }

    @Override
    public boolean init() {
        final boolean init = messageLoader.init();
        messageMap = messageLoader.getMessageMap();

        return init;
    }
}
