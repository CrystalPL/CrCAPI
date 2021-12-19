package pl.crystalek.crcapi.singlemessage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.Message;
import pl.crystalek.crcapi.message.MessageAPI;
import pl.crystalek.crcapi.singlemessage.loader.SingleMessageLoader;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SingleMessageAPI extends MessageAPI {
    final SingleMessageLoader messageLoader;
    Map<String, List<Message>> messageMap;

    public SingleMessageAPI(final JavaPlugin plugin) {
        this.messageLoader = new SingleMessageLoader(plugin);
    }

    @Override
    public void sendMessage(final String messagePath, final Audience audience, final Map<String, Object> replacements) {
        sendMessage(messageMap, messagePath, audience, replacements);
    }

    @Override
    public void sendMessageComponent(final String messagePath, final Audience audience, final Map<String, Component> replacements) {
        sendMessageComponent(messageMap, messagePath, audience, replacements);
    }

    @Override
    public Optional<Component> getComponent(final String messagePath, final Audience audience, final Class<? extends Message> clazz) {
        return getComponent(messageMap, messagePath, clazz);
    }

    @Override
    public boolean init() {
        final boolean init = messageLoader.init();
        messageMap = messageLoader.getMessageMap();

        return init;
    }
}
