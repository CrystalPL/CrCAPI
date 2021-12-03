package pl.crystalek.crcapi.message;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.config.FileHelper;
import pl.crystalek.crcapi.message.loader.MessageLoader;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class MessageAPI extends AbstractMessageAPI {
    final MessageLoader messageLoader;
    Map<String, List<Message>> messageMap;

    public MessageAPI(final JavaPlugin plugin) {
        super(plugin);
        this.messageLoader = new MessageLoader(plugin, new FileHelper("messages.yml", plugin));
    }

    public void sendMessage(final String messagePath, final Audience audience, final Map<String, Object> replacements) {
        final List<Message> messageList = messageMap.get(messagePath);
        if (messageList == null) {
            audience.sendMessage(Component.text("Nie odnaleziono wiadomości: " + messagePath + ". Zgłoś błąd administratorowi."));
            return;
        }

        messageList.forEach(message -> message.sendMessage(audience, replacements));
    }

    public void sendMessageComponent(final String messagePath, final Audience audience, final Map<String, Component> replacements) {
        final List<Message> messageList = messageMap.get(messagePath);
        if (messageList == null) {
            audience.sendMessage(Component.text("Nie odnaleziono wiadomości: " + messagePath + ". Zgłoś błąd administratorowi."));
            return;
        }

        messageList.forEach(message -> message.sendMessageComponent(audience, replacements));
    }

    public Optional<Component> getComponent(final String messagePath, final Class<? extends Message> clazz) {
        for (final Message message : messageMap.get(messagePath)) {
            if (clazz.isInstance(message)) {
                return Optional.of(message.getComponent());
            }
        }

        return Optional.empty();
    }

    public boolean init() {
        final boolean init = messageLoader.init();
        messageMap = messageLoader.getMessageMap();

        return init;
    }
}
