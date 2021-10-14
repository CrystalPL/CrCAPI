package pl.crystalek.crcapi.message;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.loader.MessageLoader;

import java.util.List;
import java.util.Map;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class MessageAPI {
    MessageLoader messageLoader;
    static Map<String, List<Message>> messageMap;

    public MessageAPI() {
        messageLoader = new MessageLoader(JavaPlugin.getProvidingPlugin(MessageAPI.class));
    }

    public void init() {
        messageLoader.init();
        messageMap = messageLoader.getMessageMap();
    }

    public static void sendMessage(final String messagePath, final Audience audience) {
        sendMessage(messagePath, audience, ImmutableMap.of());
    }

    public static void sendMessage(final String messagePath, final Audience audience, final Map<String, Object> replacements) {
        final List<Message> messageList = messageMap.get(messagePath);
        if (messageList == null) {
            audience.sendMessage(Component.text("Nie odnaleziono wiadomości: " + messagePath + ". Zgłoś błąd administratorowi."));
            return;
        }

        messageList.forEach(message -> message.sendMessage(audience, replacements));
    }
}
