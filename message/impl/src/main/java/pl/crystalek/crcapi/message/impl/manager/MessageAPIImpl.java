package pl.crystalek.crcapi.message.impl.manager;

import com.google.common.collect.ImmutableMap;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import pl.crystalek.crcapi.message.api.Message;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crcapi.message.api.MessageType;
import pl.crystalek.crcapi.message.impl.CrCAPIMessage;
import pl.crystalek.crcapi.message.impl.util.MessageUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;

abstract class MessageAPIImpl implements MessageAPI {
    abstract void sendMessage(final String messagePath, final Audience audience, final Map<String, Object> replacements);

    abstract void sendMessageComponent(final String messagePath, final Audience audience, final Map<String, Component> replacements);

    abstract Optional<Component> getComponent(final String messagePath, final Audience audience, final MessageType messageType);

    @Override
    public abstract boolean init();

    @Override
    public Optional<Component> getComponent(final String messagePath, final CommandSender messageReceiver, final MessageType messageType) {
        return this.getComponent(messagePath, CrCAPIMessage.getBukkitAudiences().sender(messageReceiver), messageType);
    }

    @Override
    public void sendMessage(final String messagePath, final CommandSender messageReceiver) {
        sendMessage(messagePath, CrCAPIMessage.getBukkitAudiences().sender(messageReceiver), ImmutableMap.of());
    }

    @Override
    public void sendMessage(final String messagePath, final CommandSender messageReceiver, final Map<String, Object> replacements) {
        sendMessage(messagePath, CrCAPIMessage.getBukkitAudiences().sender(messageReceiver), replacements);
    }

    @Override
    public void sendMessageComponent(final String messagePath, final CommandSender messageReceiver, final Map<String, Component> replacements) {
        sendMessageComponent(messagePath, CrCAPIMessage.getBukkitAudiences().sender(messageReceiver), replacements);
    }

    @Override
    public void sendMessage(final Component component, final CommandSender messageReceiver, final Map<String, Component> replacements) {
        CrCAPIMessage.getBukkitAudiences().sender(messageReceiver).sendMessage(MessageUtil.replaceComponent(component, replacements));
    }

    @Override
    public void sendMessageComponent(final Component component, final CommandSender messageReceiver, final Map<String, Object> replacements) {
        CrCAPIMessage.getBukkitAudiences().sender(messageReceiver).sendMessage(MessageUtil.replace(component, replacements));
    }

    @Override
    public void broadcastComponent(final String messagePath, final Map<String, Component> replacements) {
        sendMessageComponent(messagePath, CrCAPIMessage.getBukkitAudiences().players(), replacements);
    }

    @Override
    public void broadcast(final String messagePath, final Map<String, Object> replacements) {
        sendMessage(messagePath, CrCAPIMessage.getBukkitAudiences().players(), replacements);
    }

    @Override
    public void broadcast(final String messagePath) {
        sendMessage(messagePath, CrCAPIMessage.getBukkitAudiences().players(), ImmutableMap.of());
    }

    void sendMessage(final Map<String, List<Message>> messageMap, final String messagePath, final Audience audience, final Map<String, Object> replacements) {
        final List<Message> messageList = messageMap.get(messagePath);
        if (messageList == null) {
            audience.sendMessage(Component.text("Nie odnaleziono wiadomości: " + messagePath + ". Zgłoś błąd administratorowi."));
            return;
        }

        messageList.forEach(message -> message.sendMessage(audience, replacements));
    }

    void sendMessageComponent(final Map<String, List<Message>> messageMap, final String messagePath, final Audience audience, final Map<String, Component> replacements) {
        final List<Message> messageList = messageMap.get(messagePath);
        if (messageList == null) {
            audience.sendMessage(Component.text("Nie odnaleziono wiadomości: " + messagePath + ". Zgłoś błąd administratorowi."));
            return;
        }

        messageList.forEach(message -> message.sendMessageComponent(audience, replacements));
    }

    Optional<Component> getComponent(final Map<String, List<Message>> messageMap, final String messagePath, final MessageType messageType) {
        for (final Message message : messageMap.get(messagePath)) {
            if (message.getMessageType() == messageType) {
                return Optional.of(message.getComponent());
            }
        }

        return Optional.empty();
    }
}
