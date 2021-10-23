package pl.crystalek.crcapi.message;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.config.ConfigHelper;
import pl.crystalek.crcapi.config.FileHelper;
import pl.crystalek.crcapi.message.impl.ChatMessage;
import pl.crystalek.crcapi.message.loader.MessageLoader;

import java.util.List;
import java.util.Map;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class MessageAPI {
    static Map<String, List<Message>> messageMap;
    static BukkitAudiences bukkitAudiences;
    MessageLoader messageLoader;

    public MessageAPI() {
        final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(ConfigHelper.class);
        this.messageLoader = new MessageLoader(plugin, new FileHelper("messages.yml"));
        bukkitAudiences = BukkitAudiences.create(plugin);
    }

    public static void sendMessage(final String messagePath, final CommandSender messageReceiver) {
        sendMessage(messagePath, bukkitAudiences.sender(messageReceiver));
    }

    public static void sendMessage(final String messagePath, final CommandSender messageReceiver, final Map<String, Object> replacements) {
        sendMessage(messagePath, bukkitAudiences.sender(messageReceiver), replacements);
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

    public static void sendMessageComponent(final String messagePath, final CommandSender messageReceiver, final Map<String, Component> replacements) {
        sendMessageComponent(messagePath, bukkitAudiences.sender(messageReceiver), replacements);
    }

    public static void sendMessageComponent(final String messagePath, final Audience audience, final Map<String, Component> replacements) {
        final List<Message> messageList = messageMap.get(messagePath);
        if (messageList == null) {
            audience.sendMessage(Component.text("Nie odnaleziono wiadomości: " + messagePath + ". Zgłoś błąd administratorowi."));
            return;
        }

        messageList.forEach(message -> message.sendMessageComponent(audience, replacements));
    }

    public static Component getChatComponent(final String messagePath) {
        for (final Message message : messageMap.get(messagePath)) {
            if (message instanceof ChatMessage) {
                return message.getComponent();
            }
        }

        return null;
    }

    public boolean init() {
        final boolean init = messageLoader.init();
        messageMap = messageLoader.getMessageMap();

        return init;
    }
}
