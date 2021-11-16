package pl.crystalek.crcapi.message;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.config.FileHelper;
import pl.crystalek.crcapi.message.loader.MessageLoader;
import pl.crystalek.crcapi.message.loader.MessageUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class MessageAPI {
    final BukkitAudiences bukkitAudiences;
    final MessageLoader messageLoader;
    Map<String, List<Message>> messageMap;

    public MessageAPI(final JavaPlugin plugin) {
        this.messageLoader = new MessageLoader(plugin, new FileHelper("messages.yml", plugin));
        this.bukkitAudiences = BukkitAudiences.create(plugin);
    }

    public void sendMessage(final String messagePath, final CommandSender messageReceiver) {
        sendMessage(messagePath, bukkitAudiences.sender(messageReceiver));
    }

    public void sendMessage(final String messagePath, final CommandSender messageReceiver, final Map<String, Object> replacements) {
        sendMessage(messagePath, bukkitAudiences.sender(messageReceiver), replacements);
    }

    public void sendMessage(final String messagePath, final Audience audience) {
        sendMessage(messagePath, audience, ImmutableMap.of());
    }

    public void sendMessage(final String messagePath, final Audience audience, final Map<String, Object> replacements) {
        final List<Message> messageList = messageMap.get(messagePath);
        if (messageList == null) {
            audience.sendMessage(Component.text("Nie odnaleziono wiadomości: " + messagePath + ". Zgłoś błąd administratorowi."));
            return;
        }

        messageList.forEach(message -> message.sendMessage(audience, replacements));
    }

    public void sendMessageComponent(final String messagePath, final CommandSender messageReceiver, final Map<String, Component> replacements) {
        sendMessageComponent(messagePath, bukkitAudiences.sender(messageReceiver), replacements);
    }

    public void sendMessageComponent(final String messagePath, final Audience audience, final Map<String, Component> replacements) {
        final List<Message> messageList = messageMap.get(messagePath);
        if (messageList == null) {
            audience.sendMessage(Component.text("Nie odnaleziono wiadomości: " + messagePath + ". Zgłoś błąd administratorowi."));
            return;
        }

        messageList.forEach(message -> message.sendMessageComponent(audience, replacements));
    }

    public void sendMessage(final Component component, final CommandSender messageReceiver, final Map<String, Component> replacements) {
        bukkitAudiences.sender(messageReceiver).sendMessage(MessageUtil.replaceComponent(component, replacements));
    }

    public void sendMessageComponent(final Component component, final CommandSender messageReceiver, final Map<String, Object> replacements) {
        bukkitAudiences.sender(messageReceiver).sendMessage(MessageUtil.replace(component, replacements));
    }

    public void broadcastComponent(final String messagePath, final Map<String, Component> replacements) {
        sendMessageComponent(messagePath, bukkitAudiences.players(), replacements);
    }

    public void broadcast(final String messagePath, final Map<String, Object> replacements) {
        sendMessage(messagePath, bukkitAudiences.players(), replacements);
    }

    public void broadcast(final String messagePath) {
        sendMessage(messagePath, bukkitAudiences.players());
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
